package event;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import net.flashbots.MevShareClient;
import net.flashbots.models.common.Network;
import net.flashbots.models.event.EventHistoryEntry;
import net.flashbots.models.event.EventHistoryInfo;
import net.flashbots.models.event.EventHistoryParams;

/**
 * SSE historical example
 *
 * @author kaichen
 * @since 0.1.0
 */
public class SseHistorical {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var mevShareClient = new MevShareClient(Network.GOERLI, null, null);

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
