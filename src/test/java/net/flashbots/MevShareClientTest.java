package net.flashbots;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.reactivex.disposables.Disposable;
import net.flashbots.models.bundle.BundleItemType;
import net.flashbots.models.bundle.BundleParams;
import net.flashbots.models.bundle.BundlePrivacy;
import net.flashbots.models.bundle.HintPreferences;
import net.flashbots.models.bundle.Inclusion;
import net.flashbots.models.bundle.PrivateTxOptions;
import net.flashbots.models.bundle.SendBundleResponse;
import net.flashbots.models.bundle.SimBundleOptions;
import net.flashbots.models.common.Network;
import net.flashbots.models.event.EventHistoryEntry;
import net.flashbots.models.event.EventHistoryInfo;
import net.flashbots.models.event.EventHistoryParams;
import net.flashbots.models.event.MevShareEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

/**
 * The type MevShareClientTest.
 *
 * @author kaichen
 * @since 0.1.0
 */
class MevShareClientTest {

    private static Credentials AUTH_SIGNER;

    private static Credentials SIGNER;

    private static MevShareClient MEV_SHARE_CLIENT;

    private static Web3j WEB3J;

    @BeforeAll
    static void beforeAll()
            throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        AUTH_SIGNER = Credentials.create(Keys.createEcKeyPair());
        SIGNER = Credentials.create(System.getenv("SIGNER_PRIVATE_KEY"));
        WEB3J = Web3j.build(new HttpService(System.getenv("GOERLI_RPC_URL")));
        MEV_SHARE_CLIENT = new MevShareClient(Network.GOERLI, AUTH_SIGNER, WEB3J);
    }

    @AfterAll
    static void afterAll() {
        MEV_SHARE_CLIENT.close();
    }

    /**
     * Gets event history info.
     *
     * @throws ExecutionException   the execution exception
     * @throws InterruptedException the interrupted exception
     */
    @Test
    @DisplayName("Get event history info")
    void getEventHistoryInfo() throws ExecutionException, InterruptedException {
        EventHistoryInfo eventHistoryInfo =
                MEV_SHARE_CLIENT.getEventHistoryInfo().get();
        System.out.println(eventHistoryInfo);
        assertTrue(eventHistoryInfo.getCount().compareTo(BigInteger.ZERO) > 0);
        assertTrue(eventHistoryInfo.getMinBlock().compareTo(BigInteger.ZERO) > 0);
        assertTrue(eventHistoryInfo.getMaxBlock().compareTo(BigInteger.ZERO) > 0);
        assertTrue(eventHistoryInfo.getMinTimestamp().compareTo(BigInteger.ZERO) > 0);
        assertTrue(eventHistoryInfo.getMaxTimestamp().compareTo(BigInteger.ZERO) > 0);
        assertTrue(eventHistoryInfo.getMaxLimit().compareTo(BigInteger.ZERO) > 0);
    }

    @Test
    @DisplayName("Get event history")
    void getEventHistory() throws ExecutionException, InterruptedException {
        List<EventHistoryEntry> eventHistoryEntries = MEV_SHARE_CLIENT
                .getEventHistory(new EventHistoryParams().setLimit(20).setBlockStart(BigInteger.valueOf(1000000)))
                .get();
        System.out.println(eventHistoryEntries);
        assertEquals(20, eventHistoryEntries.size());
    }

    @Test
    @DisplayName("Subscribe event")
    @Disabled
    void subscribeEvent() throws InterruptedException, ExecutionException {
        final CountDownLatch latch = new CountDownLatch(3);
        var ref = new AtomicReference<MevShareEvent>();
        Disposable disposable = MEV_SHARE_CLIENT.subscribe(mevShareEvent -> {
            if (mevShareEvent.getLogs() != null) {
                ref.getAndSet(mevShareEvent);
                latch.countDown();
            }
        });
        latch.await();
        disposable.dispose();
        assertNotNull(ref.get().getHash());
    }

    @Test
    @DisplayName("Send bundle with hash")
    @Disabled
    void sendBundle()
            throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException,
                    ExecutionException, InterruptedException, IOException {
        CompletableFuture<MevShareEvent> future = new CompletableFuture<>();
        Disposable disposable = MEV_SHARE_CLIENT.subscribe(mevShareEvent -> {
            if (mevShareEvent.getHash() != null) {
                future.complete(mevShareEvent);
            }
        });
        MevShareEvent mevShareEvent = future.get();
        disposable.dispose();

        BigInteger number = WEB3J.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
                .send()
                .getBlock()
                .getNumber();

        Inclusion inclusion =
                new Inclusion().setBlock(number.add(BigInteger.ONE)).setMaxBlock(number.add(BigInteger.valueOf(4)));

        BundleItemType.HashItem bundleItem = new BundleItemType.HashItem().setHash(mevShareEvent.getHash());

        ECKeyPair senderKeyPair = Keys.createEcKeyPair();
        Credentials sender = Credentials.create(senderKeyPair);
        BigInteger nonce = WEB3J.ethGetTransactionCount(sender.getAddress(), DefaultBlockParameterName.PENDING)
                .send()
                .getTransactionCount();
        BigInteger gasPrice = WEB3J.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = DefaultGasProvider.GAS_LIMIT;
        final String to = "0x56EdF679B0C80D528E17c5Ffe514dc9a1b254b9c";
        final String amount = "0.01";
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger());
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Network.GOERLI.chainId(), sender);
        String hexValue = Numeric.toHexString(signedMessage);

        BundleItemType.TxItem bundleItem1 =
                new BundleItemType.TxItem().setTx(hexValue).setCanRevert(true);

        BundleParams bundleParams =
                new BundleParams().setInclusion(inclusion).setBody(List.of(bundleItem, bundleItem1));

        CompletableFuture<SendBundleResponse> res = MEV_SHARE_CLIENT.sendBundle(bundleParams);

        System.out.println(res.get().getBundleHash());
        assertNotNull(res.get().getBundleHash());
    }

    @Test
    @DisplayName("Send bundle without hash")
    void sendBundleWithoutHash()
            throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException,
                    ExecutionException, InterruptedException, IOException {

        BigInteger number = WEB3J.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
                .send()
                .getBlock()
                .getNumber();

        Inclusion inclusion =
                new Inclusion().setBlock(number.add(BigInteger.ONE)).setMaxBlock(number.add(BigInteger.valueOf(4)));

        ECKeyPair senderKeyPair = Keys.createEcKeyPair();
        Credentials sender = Credentials.create(senderKeyPair);
        BigInteger nonce = WEB3J.ethGetTransactionCount(sender.getAddress(), DefaultBlockParameterName.PENDING)
                .send()
                .getTransactionCount();
        BigInteger gasPrice = WEB3J.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = DefaultGasProvider.GAS_LIMIT;
        final String to = "0x56EdF679B0C80D528E17c5Ffe514dc9a1b254b9c";
        final String amount = "0.01";
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger());
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Network.GOERLI.chainId(), sender);
        String hexValue = Numeric.toHexString(signedMessage);

        BundleItemType.TxItem bundleItem =
                new BundleItemType.TxItem().setTx(hexValue).setCanRevert(true);

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

        BundleParams bundleParams = new BundleParams()
                .setInclusion(inclusion)
                .setBody(List.of(bundleItem))
                .setPrivacy(bundlePrivacy);

        CompletableFuture<SendBundleResponse> res = MEV_SHARE_CLIENT.sendBundle(bundleParams);

        System.out.println(res.get().getBundleHash());
        assertNotNull(res.get().getBundleHash());
    }

    @Test
    @DisplayName("Simulate bundle")
    void simBundle() throws ExecutionException, InterruptedException, IOException {
        var latestBlock = WEB3J.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
                .send()
                .getBlock();
        var parentBlock = WEB3J.ethGetBlockByNumber(
                        DefaultBlockParameter.valueOf(latestBlock.getNumber().subtract(BigInteger.ONE)), false)
                .send()
                .getBlock();

        Inclusion inclusion = new Inclusion()
                .setBlock(latestBlock.getNumber().subtract(BigInteger.ONE))
                .setMaxBlock(latestBlock.getNumber().add(BigInteger.valueOf(10)));

        BigInteger nonce = WEB3J.ethGetTransactionCount(SIGNER.getAddress(), DefaultBlockParameterName.PENDING)
                .send()
                .getTransactionCount();
        final String to = "0x56EdF679B0C80D528E17c5Ffe514dc9a1b254b9c";
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce,
                WEB3J.ethGasPrice().send().getGasPrice(),
                DefaultGasProvider.GAS_LIMIT,
                to,
                Convert.toWei("0", Convert.Unit.ETHER).toBigInteger());
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Network.GOERLI.chainId(), SIGNER);
        String hexValue = Numeric.toHexString(signedMessage);

        BundleItemType.TxItem bundleItem =
                new BundleItemType.TxItem().setTx(hexValue).setCanRevert(true);

        BundleParams bundleParams = new BundleParams().setInclusion(inclusion).setBody(List.of(bundleItem));
        SimBundleOptions options = new SimBundleOptions();

        options.setParentBlock(latestBlock.getNumber().subtract(BigInteger.ONE));
        options.setBlockNumber(latestBlock.getNumber());
        options.setTimestamp(parentBlock.getTimestamp().add(BigInteger.valueOf(12)));
        options.setGasLimit(parentBlock.getGasLimit());
        options.setBaseFee(parentBlock.getBaseFeePerGas());
        options.setTimeout(30);

        var res = MEV_SHARE_CLIENT.simBundle(bundleParams, options);

        System.out.println(res.get().toString());
        assertTrue(res.get().getSuccess());
    }

    @Test
    @DisplayName("Send private transaction")
    void sendPrivateTransaction()
            throws IOException, InterruptedException, ExecutionException, InvalidAlgorithmParameterException,
                    NoSuchAlgorithmException, NoSuchProviderException {
        EthBlock.Block latest = WEB3J.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
                .send()
                .getBlock();

        BigInteger maxPriorityFeePerGas = BigInteger.valueOf(1_000_000_000L);

        Credentials sender = Credentials.create(Keys.createEcKeyPair());
        BigInteger nonce = WEB3J.ethGetTransactionCount(sender.getAddress(), DefaultBlockParameterName.PENDING)
                .send()
                .getTransactionCount();
        final String to = "0x56EdF679B0C80D528E17c5Ffe514dc9a1b254b9c";

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                5L,
                nonce,
                latest.getGasLimit(),
                to,
                Convert.toWei("0", Convert.Unit.ETHER).toBigInteger(),
                Numeric.toHexString("im shariiiiiing".getBytes(StandardCharsets.UTF_8)),
                maxPriorityFeePerGas,
                latest.getBaseFeePerGas().multiply(BigInteger.TWO).add(maxPriorityFeePerGas));
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Network.GOERLI.chainId(), sender);
        String signRawTx = Numeric.toHexString(signedMessage);

        PrivateTxOptions txOptions = new PrivateTxOptions()
                .setHints(new HintPreferences()
                        .setCalldata(true)
                        .setContractAddress(true)
                        .setFunctionSelector(true)
                        .setLogs(true));

        CompletableFuture<String> res = MEV_SHARE_CLIENT.sendPrivateTransaction(signRawTx, txOptions);
        System.out.println(res.get());
        assertNotNull(res.get());
    }

    @Test
    @DisplayName("Subscribe tx event")
    @Disabled
    void subscribeTx() throws ExecutionException, InterruptedException {
        CompletableFuture<MevShareEvent> future = new CompletableFuture<>();
        Disposable disposable = MEV_SHARE_CLIENT.subscribeTx(mevShareEvent -> {
            if (mevShareEvent.getLogs() != null) {
                future.complete(mevShareEvent);
            }
        });
        MevShareEvent mevShareEvent = future.get();
        disposable.dispose();
        assertTrue(mevShareEvent.getTxs() == null || mevShareEvent.getTxs().size() == 1);
    }

    @Disabled("No bundle event in goerli")
    @Test
    @DisplayName("Subscribe bundle event")
    void subscribeBundle() throws ExecutionException, InterruptedException {
        CompletableFuture<MevShareEvent> future = new CompletableFuture<>();
        Disposable disposable = MEV_SHARE_CLIENT.subscribeBundle(mevShareEvent -> {
            if (mevShareEvent.getLogs() != null) {
                future.complete(mevShareEvent);
            }
        });

        MevShareEvent mevShareEvent = future.get();
        disposable.dispose();
        assertTrue(mevShareEvent.getTxs().size() > 1);
    }
}
