package bundle;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.github.cdimascio.dotenv.Dotenv;
import net.flashbots.MevShareClient;
import net.flashbots.models.bundle.BundleItemType;
import net.flashbots.models.bundle.BundleParams;
import net.flashbots.models.bundle.Inclusion;
import net.flashbots.models.bundle.SimBundleOptions;
import net.flashbots.models.common.Network;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

/**
 * Rpc mev_simBundle method example
 *
 * @author kaichen
 * @since 0.1.0
 */
public class RpcMevSimBundle {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Dotenv dotenv = Dotenv.configure()
                .directory(Paths.get("", "example").toAbsolutePath().toString())
                .filename(".env")
                .load();
        Credentials authSigner = Credentials.create(dotenv.get("AUTH_PRIVATE_KEY"));
        Web3j web3j = Web3j.build(new HttpService(dotenv.get("PROVIDER_URL")));
        var mevShareClient = new MevShareClient(Network.GOERLI, authSigner, web3j);

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

        Credentials signer = Credentials.create(dotenv.get("SENDER_PRIVATE_KEY"));
        BigInteger nonce = web3j.ethGetTransactionCount(signer.getAddress(), DefaultBlockParameterName.PENDING)
                .send()
                .getTransactionCount();
        final String to = "0x56EdF679B0C80D528E17c5Ffe514dc9a1b254b9c";
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce,
                web3j.ethGasPrice().send().getGasPrice(),
                DefaultGasProvider.GAS_LIMIT,
                to,
                Convert.toWei("0", Convert.Unit.ETHER).toBigInteger());
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Network.GOERLI.chainId(), signer);
        String hexValue = Numeric.toHexString(signedMessage);

        BundleItemType.TxItem bundleItem =
                new BundleItemType.TxItem().setTx(hexValue).setCanRevert(true);

        BundleParams bundleParams = new BundleParams().setInclusion(inclusion).setBody(List.of(bundleItem));
        SimBundleOptions options = new SimBundleOptions()
                .setParentBlock(latestBlock.getNumber().subtract(BigInteger.ONE))
                .setBlockNumber(latestBlock.getNumber())
                .setTimestamp(parentBlock.getTimestamp().add(BigInteger.valueOf(12)))
                .setGasLimit(parentBlock.getGasLimit())
                .setBaseFee(parentBlock.getBaseFeePerGas())
                .setTimeout(30);

        var res = mevShareClient.simBundle(bundleParams, options);
        System.out.println(res.get().toString());
    }
}
