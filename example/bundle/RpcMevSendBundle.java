package bundle;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.github.cdimascio.dotenv.Dotenv;
import io.reactivex.disposables.Disposable;
import net.flashbots.MevShareClient;
import net.flashbots.models.bundle.BundleItemType;
import net.flashbots.models.bundle.BundleParams;
import net.flashbots.models.bundle.Inclusion;
import net.flashbots.models.bundle.SendBundleResponse;
import net.flashbots.models.common.Network;
import net.flashbots.models.event.MevShareEvent;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

/**
 * Rpc mev_sendBundle method example
 *
 * @author kaichen
 * @since 0.1.0
 */
public class RpcMevSendBundle {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        Dotenv dotenv = Dotenv.configure()
                .directory(Paths.get("", "example").toAbsolutePath().toString())
                .filename(".env")
                .load();
        Credentials authSigner = Credentials.create(dotenv.get("AUTH_PRIVATE_KEY"));
        Web3j web3j = Web3j.build(new HttpService(dotenv.get("PROVIDER_URL")));
        var mevShareClient = new MevShareClient(Network.GOERLI, authSigner, web3j);

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

        Credentials signer = Credentials.create(dotenv.get("SENDER_PRIVATE_KEY"));
        BigInteger nonce = web3j.ethGetTransactionCount(signer.getAddress(), DefaultBlockParameterName.PENDING)
                .send()
                .getTransactionCount();
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = DefaultGasProvider.GAS_LIMIT;
        final String to = "0x56EdF679B0C80D528E17c5Ffe514dc9a1b254b9c";
        final String amount = "0.001";
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger());
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, signer);
        String hexValue = Numeric.toHexString(signedMessage);

        BundleItemType.TxItem txItem =
                new BundleItemType.TxItem().setTx(hexValue).setCanRevert(true);

        // body must include a tx
        BundleParams bundleParams = new BundleParams().setInclusion(inclusion).setBody(List.of(bundleItem, txItem));

        CompletableFuture<SendBundleResponse> res = mevShareClient.sendBundle(bundleParams);
        System.out.println(res.get().getBundleHash());

        mevShareClient.close();
    }
}
