package event;

import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.github.cdimascio.dotenv.Dotenv;
import net.flashbots.MevShareClient;
import net.flashbots.models.common.Network;
import net.flashbots.models.event.EventHistoryEntry;
import net.flashbots.models.event.EventHistoryInfo;
import net.flashbots.models.event.EventHistoryParams;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * SSE historical example
 *
 * @author kaichen
 * @since 0.1.0
 */
public class SseHistorical {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Dotenv dotenv = Dotenv.configure()
                .directory(Paths.get("", "example").toAbsolutePath().toString())
                .filename(".env")
                .load();
        Credentials authSigner = Credentials.create(dotenv.get("AUTH_PRIVATE_KEY"));
        Web3j web3j = Web3j.build(new HttpService(dotenv.get("PROVIDER_URL")));
        var mevShareClient = new MevShareClient(Network.GOERLI, authSigner, web3j);

        // get event history info
        CompletableFuture<EventHistoryInfo> historyInfoFuture = mevShareClient.getEventHistoryInfo();
        EventHistoryInfo eventHistoryInfo = historyInfoFuture.get();
        System.out.println(eventHistoryInfo);

        // get event history entry
        var historyParams = new EventHistoryParams().setLimit(20).setBlockStart(BigInteger.valueOf(1_000_000L));
        CompletableFuture<List<EventHistoryEntry>> eventHistory = mevShareClient.getEventHistory(historyParams);
        List<EventHistoryEntry> eventHistoryEntries = eventHistory.get();
        System.out.println(eventHistoryEntries);
    }
}
