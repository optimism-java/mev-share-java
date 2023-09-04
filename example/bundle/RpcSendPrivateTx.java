package bundle;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.github.cdimascio.dotenv.Dotenv;
import net.flashbots.MevShareClient;
import net.flashbots.models.bundle.HintPreferences;
import net.flashbots.models.bundle.PrivateTxOptions;
import net.flashbots.models.common.Network;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

/**
 * Rpc eth_sendPrivateTransaction method example
 *
 * @author kaichen
 * @since 0.1.0
 */
public class RpcSendPrivateTx {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        Dotenv dotenv = Dotenv.configure()
                .directory(Paths.get("", "example").toAbsolutePath().toString())
                .filename(".env")
                .load();
        Credentials authSigner = Credentials.create(dotenv.get("AUTH_PRIVATE_KEY"));
        Web3j web3j = Web3j.build(new HttpService(dotenv.get("PROVIDER_URL")));
        var mevShareClient = new MevShareClient(Network.GOERLI, authSigner, web3j);

        EthBlock.Block latest = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false)
                .send()
                .getBlock();

        BigInteger maxPriorityFeePerGas = BigInteger.valueOf(1_000_000_000L);

        Credentials signer = Credentials.create(dotenv.get("SENDER_PRIVATE_KEY"));
        BigInteger nonce = web3j.ethGetTransactionCount(signer.getAddress(), DefaultBlockParameterName.PENDING)
                .send()
                .getTransactionCount();
        final String to = "0x56EdF679B0C80D528E17c5Ffe514dc9a1b254b9c";

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

        PrivateTxOptions txOptions = new PrivateTxOptions()
                .setHints(new HintPreferences()
                        .setCalldata(true)
                        .setContractAddress(true)
                        .setFunctionSelector(true)
                        .setLogs(true));

        CompletableFuture<String> res = mevShareClient.sendPrivateTransaction(signRawTx, txOptions);
        System.out.println(res.get());

        mevShareClient.close();
    }
}
