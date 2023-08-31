[![mev-share-java CI](https://github.com/optimism-java/mev-share-java/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/optimism-java/mev-share-java/actions/workflows/build.yml)
[![License](https://img.shields.io/badge/license-MIT-blue)](https://opensource.org/licenses/MIT)
![Coverage](.github/badges/jacoco.svg)
![Branches](.github/badges/branches.svg)

# mev-share-java

Mev-share-java is a Java library for working with MEV-share.

Based on [Specs](https://github.com/flashbots/mev-share)

## Using

Download the latest jar from maven central.

### Maven

```xml
<dependency>
<groupId>me.grapebaba</groupId>
<artifactId>mev-share-java</artifactId>
<version>0.1.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'me.grapebaba:mev-share-java:0.1.0'
```

## Building Locally

To get started you need to install JDK17+, then run the following command:

```bash
GOERLI_RPC_URL=<GOERLI_RPC_URL> SIGNER_PRIVATE_KEY=<SIGNER_PRIVATE_KEY> ./gradlew clean build
```

## Javadoc

For the latest javadocs for the `main` branch, run `./gradlew javadoc` and open
the document under the `build/docs/javadoc/index.html` in your browser.

## Testing

### To run unit tests

```
GOERLI_RPC_URL=<GOERLI_RPC_URL> SIGNER_PRIVATE_KEY=<SIGNER_PRIVATE_KEY> ./gradlew test
```

## How to use it

### Create a MEVShare instance

```java
// Create a credential instance for authentication using Web3j
Credentials authSigner = Credentials.create("<hex string of privateKey>");

// Create an ethereum provider instance using Web3j
Web3j web3j = Web3j.build(new HttpService("<ethereum network rpc url>"));

// Create a Mev share network options instance or use built-in options
Network network = new Network()
		.setName("<network name>")
		.setChainId("<chain id>")
		.setRpcUrl("<mev share network rpc url>")
		.setStreamUrl("<mev share network stream url>");

Network network = Network.GOERLI;
Network network = Network.MAINNET;

// Create a Mev share instance
MevShareClient mevShareClient = new MevShareClient(network, authSigner, web3j);
```

### History events Query

```java
// Query the Event info
CompletableFuture<EventHistoryInfo> historyInfoFuture = mevShareClient.getEventHistoryInfo();

// Query the Event List by params
EventHistoryParams historyParams = new EventHistoryParams().setLimit(20).setBlockStart(BigInteger.valueOf(1_000_000L));
CompletableFuture<List<EventHistoryEntry>> eventHistory = mevShareClient.getEventHistory(historyParams);
```

### Subscribe to events stream

```java
// Create an event listener which handles the event
Consumer<MevShareEvent> eventListener = mevShareEvent -> {
			// do something and do not block here...
	};

// Subscribe to events stream
Disposable disposable = mevShareClient.subscribe(eventListener);

// remember to release when no longer to subscribe events
disposable.dispose();
```

### Send bundle

#### Send a bundle including a hash item

```java

// Listen to the event stream and capture the bundle hash
CompletableFuture<MevShareEvent> future = new CompletableFuture<>();
Disposable eventSource = mevShareClient.subscribe(mevShareEvent -> {
	if (mevShareEvent.getHash() != null) {
		future.complete(mevShareEvent);
	}
});
MevShareEvent mevShareEvent = future.get();
eventSource.dispose();

BigInteger number = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
		.send()
		.getBlock()
		.getNumber();

Inclusion inclusion =
		new Inclusion().setBlock(number.add(BigInteger.ONE)).setMaxBlock(number.add(BigInteger.valueOf(4)));

BundleItemType.HashItem bundleItem = new BundleItemType.HashItem().setHash(mevShareEvent.getHash());

// Create a backrun transaction
Credentials signer = Credentials.create("<private key>");
BigInteger nonce = web3j.ethGetTransactionCount(signer.getAddress(), DefaultBlockParameterName.PENDING)
.send()
.getTransactionCount();
BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
BigInteger gasLimit = DefaultGasProvider.GAS_LIMIT;
final String to = "<to address>";
final String amount = "<ether amount>";
RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
nonce,
gasPrice,
gasLimit,
to,
Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger());
byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Network.GOERLI.chainId(), signer);
String hexValue = Numeric.toHexString(signedMessage);

BundleItemType.TxItem txItem =
new BundleItemType.TxItem().setTx(hexValue).setCanRevert(true);

// Construct the bundle with bundle hash item and backrun transaction item
BundleParams bundleParams = new BundleParams().setInclusion(inclusion).setBody(List.of(bundleItem, txItem));

// Send the bundle
CompletableFuture<SendBundleResponse> res = mevShareClient.sendBundle(bundleParams);
```

#### Send a bundle with privacy
Bundles that only contain signed transactions can share hints about the transactions in their bundle by setting the privacy parameter
```java
// Create a transaction
Credentials signer = Credentials.create("<private key>");
BigInteger nonce = web3j.ethGetTransactionCount(signer.getAddress(), DefaultBlockParameterName.PENDING)
.send()
.getTransactionCount();
BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
BigInteger gasLimit = DefaultGasProvider.GAS_LIMIT;
final String to = "<to address>";
final String amount = "<ether amount>";
RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
nonce,
gasPrice,
gasLimit,
to,
Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger());
byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Network.GOERLI.chainId(), signer);
String hexValue = Numeric.toHexString(signedMessage);

BundleItemType.TxItem txItem =
new BundleItemType.TxItem().setTx(hexValue).setCanRevert(true);

// Set privacy parameter
HintPreferences hintPreferences = new HintPreferences()
.setCalldata(true)
.setContractAddress(true)
.setFunctionSelector(true)
.setLogs(true)
.setTxHash(true);
List<String> builders = new ArrayList<>();
builders.add("flashbots");

BundlePrivacy bundlePrivacy =
new BundlePrivacy().setHints(hintPreferences).setBuilders(builders);

// Construct the bundle with transaction item
BundleParams bundleParams = new BundleParams()
.setInclusion(inclusion)
.setBody(List.of(txItem))
.setPrivacy(bundlePrivacy);

// Send the bundle
CompletableFuture<SendBundleResponse> res = MEV_SHARE_CLIENT.sendBundle(bundleParams);
```

### Simulate bundle

```java
// Create a transaction
var latestBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
		.send()
		.getBlock();
var parentBlock = web3j.ethGetBlockByNumber(
				DefaultBlockParameter.valueOf(latestBlock.getNumber().subtract(BigInteger.ONE)), false)
		.send()
		.getBlock();

Inclusion inclusion = new Inclusion()
		.setBlock(latestBlock.getNumber().subtract(BigInteger.ONE))
		.setMaxBlock(latestBlock.getNumber().add(BigInteger.valueOf(10)));

Credentials signer = Credentials.create("<private key>");
BigInteger nonce = web3j.ethGetTransactionCount(signer.getAddress(), DefaultBlockParameterName.PENDING)
		.send()
		.getTransactionCount();
final String to = "<to address>";
RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
		nonce,
		web3j.ethGasPrice().send().getGasPrice(),
		DefaultGasProvider.GAS_LIMIT,
		to,
		Convert.toWei("0", Convert.Unit.ETHER).toBigInteger());
byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Network.GOERLI.chainId(), signer);
String hexValue = Numeric.toHexString(signedMessage);

// Construct the bundle params with transaction item
BundleItemType.TxItem bundleItem =
		new BundleItemType.TxItem().setTx(hexValue).setCanRevert(true);

BundleParams bundleParams = new BundleParams().setInclusion(inclusion).setBody(List.of(bundleItem));

// Create a simbundle options
SimBundleOptions options = new SimBundleOptions()
		.setParentBlock(latestBlock.getNumber().subtract(BigInteger.ONE))
		.setBlockNumber(latestBlock.getNumber())
		.setTimestamp(parentBlock.getTimestamp().add(BigInteger.valueOf(12)))
		.setGasLimit(parentBlock.getGasLimit())
		.setBaseFee(parentBlock.getBaseFeePerGas())
		.setTimeout(30);

// Simulate the bundle
CompletableFuture<SimBundleResponse> res = mevShareClient.simBundle(bundleParams, options);
```

### Send private transaction

```java
// Create a transaction
EthBlock.Block latest = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
		.send()
		.getBlock();

BigInteger maxPriorityFeePerGas = BigInteger.valueOf(1_000_000_000L);

Credentials signer = Credentials.create("<private key>");
BigInteger nonce = web3j.ethGetTransactionCount(signer.getAddress(), DefaultBlockParameterName.PENDING)
		.send()
		.getTransactionCount();
final String to = "<to address>";

RawTransaction rawTransaction = RawTransaction.createTransaction(
		5L,
		nonce,
		latest.getGasLimit(),
		to,
		Convert.toWei("0", Convert.Unit.ETHER).toBigInteger(),
		Numeric.toHexString("<data>".getBytes(StandardCharsets.UTF_8)),
		maxPriorityFeePerGas,
		latest.getBaseFeePerGas().multiply(BigInteger.TWO).add(maxPriorityFeePerGas));
byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Network.GOERLI.chainId(), signer);
String signRawTx = Numeric.toHexString(signedMessage);

// Create a private transaction options
PrivateTxOptions txOptions = new PrivateTxOptions()
		.setHints(new HintPreferences()
				.setCalldata(true)
				.setContractAddress(true)
				.setFunctionSelector(true)
				.setLogs(true));

// Send the private transaction
CompletableFuture<String> res = mevShareClient.sendPrivateTransaction(signRawTx, txOptions);
```

## Examples
For more examples, you can see [example](https://github.com/optimism-java/mev-share-java/tree/main/example)

> Examples require a `.env` file (or that you populate your environment directly with the appropriate variables).

```sh
cd src/examples
cp .env.example .env
vim .env
```

## Contribution
To help mev-share-java grow, follow [Contributing to mev-share-java](CONTRIBUTING.md).
