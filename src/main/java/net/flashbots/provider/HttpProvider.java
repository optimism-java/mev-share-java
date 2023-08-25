package net.flashbots.provider;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.flashbots.common.MevShareApiException;
import net.flashbots.models.common.JsonRpc20Request;
import net.flashbots.models.common.JsonRpc20Response;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.slf4j.Logger;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

/**
 * The type HttpProvider.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class HttpProvider {

    private static final Logger LOGGER = getLogger(HttpProvider.class);
    private final OkHttpClient httpClient;

    private final EventSource.Factory eventSourceFactory;
    private final ObjectMapper objectMapper;

    private final AtomicLong nextId = new AtomicLong();

    /**
     * Instantiates a new HttpProvider.
     * @param objectMapper the object mapper
     */
    public HttpProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .writeTimeout(Duration.ofSeconds(30))
                .readTimeout(Duration.ofSeconds(30))
                .build();
        this.eventSourceFactory = EventSources.createFactory(this.httpClient);
    }

    /**
     * Send CompletableFuture.
     *
     * @param <T>         the type parameter
     * @param request the request
     * @param respType the respType
     * @return the completable future
     */
    public <T> CompletableFuture<T> send(Request request, JavaType respType) {
        LOGGER.trace("Sending request: {}", request);
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        this.httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LOGGER.error("JsonRpcError sending request", e);
                completableFuture.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.body() != null) {
                        String respBody = response.body().string();
                        LOGGER.trace("Received response: {}", respBody);
                        T res = objectMapper.readValue(respBody, respType);
                        completableFuture.complete(res);
                    }
                } catch (IOException | IllegalStateException e) {
                    LOGGER.error("JsonRpcError parsing response", e);
                    completableFuture.completeExceptionally(e);
                }
            }
        });

        return completableFuture;
    }

    /**
     * Send completable future.
     *
     * @param <T>  the type parameter
     * @param url the url
     * @param request the request
     * @param authSigner the auth signer
     * @param respType the resp type
     * @return the completable future
     */
    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<T> send(
            String url, JsonRpc20Request request, Credentials authSigner, JavaType respType) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        String requestBodyJson;
        try {
            requestBodyJson = objectMapper.writeValueAsString(request);
            LOGGER.trace("request body: {}", requestBodyJson);
        } catch (JsonProcessingException e) {
            LOGGER.error("JsonRpcError serializing request", e);
            future.completeExceptionally(e);
            return future;
        }

        Sign.SignatureData signatureData = Sign.signPrefixedMessage(
                Hash.sha3String(requestBodyJson).getBytes(StandardCharsets.UTF_8), authSigner.getEcKeyPair());
        byte[] signatureBytes = new byte[65];
        System.arraycopy(signatureData.getR(), 0, signatureBytes, 0, 32);
        System.arraycopy(signatureData.getS(), 0, signatureBytes, 32, 32);
        signatureBytes[64] = signatureData.getV()[0];
        String signature = String.format(
                "%s:%s",
                Numeric.prependHexPrefix(
                        Keys.getAddress(authSigner.getEcKeyPair().getPublicKey())),
                Numeric.toHexString(signatureBytes));
        LOGGER.trace("signature: {}", signature);
        final RequestBody requestBody =
                RequestBody.create(requestBodyJson, MediaType.get("application/json; charset=utf-8"));
        Request okhttpRequest = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("X-Flashbots-Signature", signature)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build();
        return send(
                        okhttpRequest,
                        objectMapper.getTypeFactory().constructParametricType(JsonRpc20Response.class, respType))
                .thenCompose(jsonRpc20Response -> {
                    JsonRpc20Response<T> resp = (JsonRpc20Response<T>) jsonRpc20Response;
                    if (resp.getError() != null) {
                        final MevShareApiException e;
                        if (resp.getThrowable() != null) {
                            e = new MevShareApiException(resp.getThrowable());
                            e.setError(resp.getError());
                        } else {
                            e = new MevShareApiException(resp.getError());
                        }
                        future.completeExceptionally(e);
                    } else {
                        future.complete(resp.getResult());
                    }
                    return future;
                })
                .exceptionallyCompose(throwable -> {
                    MevShareApiException e = new MevShareApiException(throwable);
                    future.completeExceptionally(e);
                    return future;
                });
    }

    /**
     * Event source factory eventSourceFactory
     *
     * @return the eventSourceFactory
     */
    public EventSource.Factory eventSourceFactory() {
        return eventSourceFactory;
    }

    /**
     * Create json rpc 20 request createJsonRpc20Request.
     *
     * @param method the method
     * @param params the params
     * @return the createJsonRpc20Request
     */
    public JsonRpc20Request createJsonRpc20Request(String method, List<?> params) {
        final JsonRpc20Request request = new JsonRpc20Request();
        request.setId(nextId());
        request.setMethod(method);
        request.setParams(params);
        return request;
    }

    private long nextId() {
        return nextId.incrementAndGet();
    }
}
