package net.flashbots;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import io.reactivex.disposables.Disposable;
import net.flashbots.models.bundle.BundleParams;
import net.flashbots.models.bundle.PrivateTxOptions;
import net.flashbots.models.bundle.SendBundleResponse;
import net.flashbots.models.bundle.SimBundleOptions;
import net.flashbots.models.bundle.SimBundleResponse;
import net.flashbots.models.event.EventHistoryEntry;
import net.flashbots.models.event.EventHistoryInfo;
import net.flashbots.models.event.EventHistoryParams;
import net.flashbots.models.event.MevShareEvent;

/**
 * The interface of Mev-Share API.
 *
 * @author kaichen
 * @since 0.1.0
 */
public interface MevShareApi {

    /**
     * Gets event history info.
     *
     * @return the event history info {@link EventHistoryInfo}
     */
    CompletableFuture<EventHistoryInfo> getEventHistoryInfo();

    /**
     * Gets the list of event history entries.
     *
     * @param params the event history params {@link EventHistoryParams}
     * @return the list of event history entries {@link EventHistoryEntry}
     */
    CompletableFuture<List<EventHistoryEntry>> getEventHistory(EventHistoryParams params);

    /**
     * Subscribe to All MEV-Share events with flowable.
     *
     * @param consumer the consumer for the event
     * @return the disposable, call {@link Disposable#dispose()} to unsubscribe
     */
    Disposable subscribe(Consumer<MevShareEvent> consumer);

    /**
     * Subscribe to Tx MEV-Share events with flowable.
     *
     * @param consumer the consumer
     * @return the disposable
     */
    Disposable subscribeTx(Consumer<MevShareEvent> consumer);

    /**
     * Subscribe to bundle MEV-Share events with flowable.
     *
     * @param consumer the consumer
     * @return the disposable
     */
    Disposable subscribeBundle(Consumer<MevShareEvent> consumer);

    /**
     * Send MEV-Share bundle to the network.
     *
     * @param params the bundle params  {@link BundleParams}
     * @return the bundle response {@link SendBundleResponse} otherwise return the error
     */
    CompletableFuture<SendBundleResponse> sendBundle(BundleParams params);

    /**
     * Simulate bundle
     *
     * @param params simulate bundle param instance
     * @param options simulate bundle options instance
     * @return the simulate bundle response {@link SimBundleResponse} otherwise return an error
     */
    CompletableFuture<SimBundleResponse> simBundle(BundleParams params, SimBundleOptions options);

    /**
     * Bundles containing pending transactions (specified by `{hash}` instead of `{tx}` in `params.body`) may
     * only be simulated after those transactions have landed on chain. If the bundle contains
     * pending transactions, this method will wait for the transactions to land before simulating.
     *
     * @param params simulate bundle param instance
     * @param options simulate bundle options instance
     * @return the simulate bundle response {@link SimBundleResponse} otherwise return an error
     */
    CompletableFuture<SimBundleResponse> simulateBundle(BundleParams params, SimBundleOptions options);

    /**
     * Send private transaction
     *
     * @param signedRawTx string of signed raw transaction
     * @param options private transaction options
     * @return the private transaction hash otherwise return an error
     */
    CompletableFuture<String> sendPrivateTransaction(String signedRawTx, PrivateTxOptions options);
}
