package net.flashbots;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import net.flashbots.common.MevShareApiException;
import net.flashbots.common.MevShareEventListener;
import net.flashbots.models.bundle.BundleItemType;
import net.flashbots.models.bundle.BundleParams;
import net.flashbots.models.bundle.PrivateTxOptions;
import net.flashbots.models.bundle.PrivateTxParams;
import net.flashbots.models.bundle.SendBundleResponse;
import net.flashbots.models.bundle.SimBundleOptions;
import net.flashbots.models.bundle.SimBundleResponse;
import net.flashbots.models.common.JsonRpc20Request;
import net.flashbots.models.common.Network;
import net.flashbots.models.event.EventHistoryEntry;
import net.flashbots.models.event.EventHistoryInfo;
import net.flashbots.models.event.EventHistoryParams;
import net.flashbots.models.event.MevShareEvent;
import net.flashbots.provider.HttpProvider;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.sse.EventSource;
import org.slf4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthTransaction;

/**
 * The type MevShareClient.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class MevShareClient implements MevShareApi {

    private static final Logger LOGGER = getLogger(MevShareClient.class);

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private final Credentials authSigner;

    private final HttpProvider provider;

    private final Network network;

    private final Web3j web3j;

    /**
     * Instantiates a new Mev share client.
     *
     * @param network the network
     * @param authSigner the auth signer
     * @param web3j the web3j client
     */
    public MevShareClient(Network network, Credentials authSigner, Web3j web3j) {
        this.network = network;
        this.provider = new HttpProvider(objectMapper);
        this.authSigner = authSigner;
        this.web3j = web3j;
    }

    @Override
    public CompletableFuture<EventHistoryInfo> getEventHistoryInfo() {
        Request request = new Request.Builder()
                .url(network.streamUrl() + "/api/v1/history/info")
                .get()
                .build();
        return provider.send(request, objectMapper.constructType(EventHistoryInfo.class));
    }

    @Override
    public CompletableFuture<List<EventHistoryEntry>> getEventHistory(EventHistoryParams params) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(network.streamUrl() + "/api/v1/history"))
                .newBuilder();
        if (params != null) {
            if (params.getBlockStart() != null) {
                urlBuilder.addQueryParameter(
                        "blockStart", params.getBlockStart().toString());
            }
            if (params.getBlockEnd() != null) {
                urlBuilder.addQueryParameter("blockEnd", params.getBlockEnd().toString());
            }
            if (params.getTimestampStart() != null) {
                urlBuilder.addQueryParameter(
                        "timestampStart", params.getTimestampStart().toString());
            }
            if (params.getTimestampEnd() != null) {
                urlBuilder.addQueryParameter(
                        "timestampEnd", params.getTimestampEnd().toString());
            }
            if (params.getLimit() != null) {
                urlBuilder.addQueryParameter("limit", params.getLimit().toString());
            }
            if (params.getOffset() != null) {
                urlBuilder.addQueryParameter("offset", params.getOffset().toString());
            }
        }
        Request request =
                new Request.Builder().url(urlBuilder.build().url()).get().build();
        return provider.send(
                request, objectMapper.getTypeFactory().constructCollectionType(List.class, EventHistoryEntry.class));
    }

    @Override
    public Disposable subscribe(Consumer<MevShareEvent> consumer) {
        return Flowable.<MevShareEvent>create(
                        subscriber -> {
                            Request request = new Request.Builder()
                                    .url(network.streamUrl())
                                    .get()
                                    .build();
                            MevShareEventListener eventListener = new MevShareEventListener(subscriber, objectMapper);
                            final EventSource eventSource =
                                    provider.eventSourceFactory().newEventSource(request, eventListener);
                            subscriber.setCancellable(eventSource::cancel);
                        },
                        BackpressureStrategy.MISSING)
                .subscribe(consumer::accept);
    }

    @Override
    public Disposable subscribeTx(Consumer<MevShareEvent> consumer) {
        return this.subscribe(mevShareEvent -> {
            if (mevShareEvent.getTxs() == null || mevShareEvent.getTxs().size() == 1) {
                consumer.accept(mevShareEvent);
            }
        });
    }

    @Override
    public Disposable subscribeBundle(Consumer<MevShareEvent> consumer) {
        return this.subscribe(mevShareEvent -> {
            if (mevShareEvent.getTxs() != null && mevShareEvent.getTxs().size() > 1) {
                consumer.accept(mevShareEvent);
            }
        });
    }

    @Override
    public CompletableFuture<SendBundleResponse> sendBundle(BundleParams params) {
        JsonRpc20Request request = provider.createJsonRpc20Request("mev_sendBundle", List.of(params));
        return provider.send(
                network.rpcUrl(), request, authSigner, objectMapper.constructType(SendBundleResponse.class));
    }

    @Override
    public CompletableFuture<SimBundleResponse> simBundle(BundleParams params, SimBundleOptions options) {
        var realOptions = options == null ? new SimBundleOptions() : options;
        JsonRpc20Request request = provider.createJsonRpc20Request("mev_simBundle", List.of(params, realOptions));
        return provider.send(
                network.rpcUrl(), request, authSigner, objectMapper.constructType(SimBundleResponse.class));
    }

    @Override
    public CompletableFuture<SimBundleResponse> simulateBundle(
            final BundleParams params, final SimBundleOptions options) {
        if (!(params.getBody().get(0) instanceof BundleItemType.HashItem firstTx)) {
            return this.simBundle(params, options);
        }
        return this.createSimulateBundle(firstTx, params, options);
    }

    @Override
    public CompletableFuture<String> sendPrivateTransaction(String signedRawTx, PrivateTxOptions options) {
        var tx = PrivateTxParams.from(signedRawTx, options);
        JsonRpc20Request request = provider.createJsonRpc20Request("eth_sendPrivateTransaction", List.of(tx));
        return provider.send(network.rpcUrl(), request, authSigner, objectMapper.constructType(String.class));
    }

    private CompletableFuture<SimBundleResponse> createSimulateBundle(
            final BundleItemType.HashItem firstTx, final BundleParams params, final SimBundleOptions options) {
        return getTransaction(firstTx.getHash()).thenComposeAsync(tx -> {
            if (tx.getTransaction().isEmpty()) {
                throw new MevShareApiException("Target transaction did not appear on chain");
            }
            var simBlock = options != null
                    ? options.getParentBlock()
                    : tx.getTransaction().get().getBlockNumber().subtract(BigInteger.ONE);

            var body = new ArrayList<>(params.getBody());
            body.set(
                    0,
                    new BundleItemType.TxItem()
                            .setTx(tx.getTransaction().get().getInput())
                            .setCanRevert(false));
            var paramsWithSignedTx = params.clone().setBody(body);

            var newOptions = options == null ? new SimBundleOptions() : options.clone();
            newOptions.setParentBlock(simBlock);

            return this.simBundle(paramsWithSignedTx, newOptions);
        });
    }

    private CompletableFuture<EthTransaction> getTransaction(final String hash) {
        return CompletableFuture.supplyAsync(() -> {
            Disposable subscribe = null;
            try {
                // try to get tx first
                EthTransaction res = web3j.ethGetTransactionByHash(hash).send();
                if (res.getTransaction().isPresent()) {
                    return res;
                }

                final CompletableFuture<EthTransaction> txFuture = new CompletableFuture<>();
                subscribe = web3j.blockFlowable(false).subscribe(block -> {
                    EthTransaction hashTx = web3j.ethGetTransactionByHash(hash).send();
                    if (hashTx.getTransaction().isPresent()) {
                        txFuture.complete(hashTx);
                    }
                });
                return txFuture.get(5, TimeUnit.MINUTES);
            } catch (InterruptedException | ExecutionException | TimeoutException | IOException e) {
                LOGGER.error("Failed to get transaction by hash", e);
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                throw new MevShareApiException("Failed to get transaction by hash", e);
            } finally {
                if (subscribe != null) {
                    subscribe.dispose();
                }
            }
        });
    }
}
