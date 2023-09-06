package bundle;

import io.github.cdimascio.dotenv.Dotenv;
import io.reactivex.disposables.Disposable;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import net.flashbots.MevShareClient;
import net.flashbots.models.bundle.BundleItemType;
import net.flashbots.models.bundle.BundleParams;
import net.flashbots.models.bundle.HintPreferences;
import net.flashbots.models.bundle.Inclusion;
import net.flashbots.models.bundle.PrivateTxOptions;
import net.flashbots.models.bundle.SendBundleResponse;
import net.flashbots.models.bundle.SimBundleOptions;
import net.flashbots.models.bundle.SimBundleResponse;
import net.flashbots.models.common.Network;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

/**
 * @author thinkAfCod
 * @since 0.1.1
 */
public class RpcSimulateBundle {

  private final Credentials senderSigner;

  private final Web3j web3j;

  private final MevShareClient shareClient;

  public RpcSimulateBundle(Credentials senderSigner,
                           Web3j web3j, MevShareClient shareClient) {
    this.senderSigner = senderSigner;
    this.web3j = web3j;
    this.shareClient = shareClient;
  }

  private void simulateBundle() throws InterruptedException {
    var pendingHash = new ConcurrentHashMap<String, Object>(1);
    CountDownLatch latch = new CountDownLatch(1);
    Disposable txListener = shareClient.subscribe(mevShareEvent -> {
      System.out.println("receive mevShareEvent:" + mevShareEvent);
      if (mevShareEvent.getHash() == null) {
        return;
      }
      if (!pendingHash.containsKey(mevShareEvent.getHash())) {
        System.out.println("pendingHash not contains the hash:" + mevShareEvent.getHash());
        return;
      }
      try {
        var bundleParams = sendBundle(mevShareEvent.getHash());
        boolean success = simulate(mevShareEvent.getHash(), bundleParams);
        pendingHash.remove(mevShareEvent.getHash());
        if (success) {
          latch.countDown();
        }
      } catch (IOException | ExecutionException | InterruptedException |
               InvalidAlgorithmParameterException
               | NoSuchAlgorithmException | NoSuchProviderException e) {
        throw new RuntimeException(e);
      }
    });

    Disposable blockListener = web3j.blockFlowable(false).subscribe(block -> {
      if (pendingHash.size() != 0 || latch.getCount() == 0) {
        return;
      }
      var tuple = createExampleTx(block.getBlock(), senderSigner, null, null);
      PrivateTxOptions txOptions = txOptions(block.getBlock().getNumber().add(BigInteger.valueOf(3L)));
      final String txHash = shareClient.sendPrivateTransaction(tuple.component1(), txOptions).get();
      System.out.println("put to pendingHash:" + txHash);
      pendingHash.put(txHash, "");
    });
    latch.await();
    blockListener.dispose();
    txListener.dispose();
  }

  private BundleParams sendBundle(
      String hash) throws IOException, ExecutionException, InterruptedException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
    Tuple2<String, BigInteger> sendBundle = createExampleTx(null,
        senderSigner,
        BigInteger.valueOf(1_000_000_000).multiply(BigInteger.valueOf(1000L)),
        "im backrunniiiiing");

    var targetNumber = web3j.ethBlockNumber().send().getBlockNumber();
    Inclusion inclusion =
        new Inclusion().setBlock(targetNumber.add(BigInteger.ONE))
            .setMaxBlock(targetNumber.add(BigInteger.valueOf(10).add(BigInteger.ONE)));
    System.out.println("sendBundle inclusion:" + inclusion.toString());

    BundleItemType.HashItem hashItem = new BundleItemType.HashItem().setHash(hash);
    BundleItemType.TxItem txItem =
        new BundleItemType.TxItem().setTx(sendBundle.component1()).setCanRevert(false);

    BundleParams bundleParams =
        new BundleParams().setInclusion(inclusion).setBody(List.of(hashItem, txItem));
    CompletableFuture<SendBundleResponse> res = shareClient.sendBundle(bundleParams);
    SendBundleResponse sendBundleResponse = res.get();
    System.out.println("sendBundleResponse: " + sendBundleResponse);
    return bundleParams;
  }

  private boolean simulate(String txHash, BundleParams bundleParams)
      throws IOException, InterruptedException, ExecutionException {
    long targetBlock = web3j.ethBlockNumber().send().getBlockNumber().longValue();
    for (long i = 1; i < 10; i++) {
      var currentBlock = targetBlock + i;
      System.out.println("tx {" + txHash + "} wating for block " + currentBlock);
      while (web3j.ethBlockNumber().send().getBlockNumber().longValue() < currentBlock) {
        Thread.sleep(10000);
      }
      var txItem = (BundleItemType.TxItem) bundleParams.getBody().get(1);
      String bundleTxHash = Hash.sha3(txItem.getTx());
      EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(bundleTxHash).send();
      if (receipt.getTransactionReceipt().isEmpty()) {
        System.out.println("backrun tx {" + bundleTxHash + "} not included in block " + currentBlock);
        continue;
      }
      if (!receipt.getResult().isStatusOK()) {
        continue;
      }
      System.out.println("bundle included! found tx " + receipt.getResult().getTransactionHash());
      SimBundleOptions options = new SimBundleOptions()
          .setParentBlock(receipt.getResult().getBlockNumber().subtract(BigInteger.ONE));
      SimBundleResponse simulateRes = shareClient.simulateBundle(bundleParams, options).get();
      System.out.println("simBundleOptions:" + options);
      System.out.println("simBundleOptions:" + simulateRes);
      return true;
    }
    return false;
  }

  private Tuple2<String, BigInteger> createExampleTx(EthBlock.Block latest, Credentials sender, BigInteger tip, String data)
      throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
    if (latest == null) {
      latest = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
          .send()
          .getBlock();
    }

    BigInteger maxPriorityFeePerGas = BigInteger.valueOf(1_000_000_000L);
    BigInteger nonce = web3j.ethGetTransactionCount(sender.getAddress(), DefaultBlockParameterName.PENDING)
        .send()
        .getTransactionCount();
    final String realData = StringUtils.isEmpty(data)
        ? "im shariiiiiing" : data;

    BigInteger actualTip = tip == null ? BigInteger.ZERO : tip;
    RawTransaction rawTransaction = RawTransaction.createTransaction(
        5L,
        nonce.add(BigInteger.ONE),
        DefaultGasProvider.GAS_LIMIT,
        Credentials.create(Keys.createEcKeyPair()).getAddress(),
        BigInteger.ZERO,
        Numeric.toHexString(realData.getBytes(StandardCharsets.UTF_8)),
        maxPriorityFeePerGas.add(actualTip),
        BigInteger.valueOf(3_000_000_000L).add(actualTip)
    );
    System.out.println("------------------------tx field start--------------------------");
    System.out.println("chainId:" + 5L);
    System.out.println("to:" + rawTransaction.getTo());
    System.out.println("nonce:" + rawTransaction.getNonce());
    System.out.println("value:" + rawTransaction.getValue());
    System.out.println("gasLimit:" + rawTransaction.getGasLimit());
    System.out.println("data:" + rawTransaction.getData());
    System.out.println("maxFeePerGas:" + latest.getBaseFeePerGas().multiply(BigInteger.TWO).add(maxPriorityFeePerGas).add(actualTip));
    System.out.println("maxPriorityFeePerGas:" + maxPriorityFeePerGas.add(actualTip));
    System.out.println("------------------------tx field end--------------------------");

    byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, sender);
    return new Tuple2<>(Numeric.toHexString(signedMessage), latest.getNumber());
  }

  private PrivateTxOptions txOptions(BigInteger maxBlock) {
    return new PrivateTxOptions()
        .setHints(new HintPreferences()
            .setCalldata(true)
            .setContractAddress(true)
            .setFunctionSelector(true)
            .setLogs(true))
        .setMaxBlockNumber(maxBlock);
  }

  private void sendToEth() throws InvalidAlgorithmParameterException, IOException, NoSuchAlgorithmException, NoSuchProviderException {
    Tuple2<String, BigInteger> exampleTx = createExampleTx(null, senderSigner, null, null);
    EthSendTransaction sendRes = web3j.ethSendRawTransaction(exampleTx.component1()).send();
    System.out.println(sendRes.getTransactionHash());
  }


  public static void main(String[] args) throws IOException, ExecutionException, InterruptedException,
      InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
    Dotenv dotenv = Dotenv.configure()
        .directory(Paths.get("", "example").toAbsolutePath().toString())
        .filename(".env")
        .load();

    Credentials authSigner = Credentials.create(dotenv.get("AUTH_PRIVATE_KEY"));
    Credentials senderSigner = Credentials.create(dotenv.get("SENDER_PRIVATE_KEY"));
    Web3j web3j = Web3j.build(new HttpService(dotenv.get("PROVIDER_URL")));
    var mevShareClient = new MevShareClient(Network.GOERLI, authSigner, web3j);
    RpcSimulateBundle tool = new RpcSimulateBundle(senderSigner, web3j, mevShareClient);
    tool.simulateBundle();
//    tool.sendToEth();
  }

}
