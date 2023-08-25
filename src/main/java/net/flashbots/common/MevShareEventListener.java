package net.flashbots.common;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.FlowableEmitter;
import net.flashbots.models.event.MevShareEvent;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * The type MevShareEventListener.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class MevShareEventListener extends EventSourceListener {

    private static final Logger LOGGER = getLogger(MevShareEventListener.class);

    private final FlowableEmitter<MevShareEvent> emitter;

    private final ObjectMapper objectMapper;

    /**
     * Instantiates a new MevShareEventListener.
     *
     * @param emitter the events emitter
     * @param objectMapper the object mapper
     */
    public MevShareEventListener(FlowableEmitter<MevShareEvent> emitter, ObjectMapper objectMapper) {
        this.emitter = emitter;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        LOGGER.trace("EventSource received event: id={}, type={}, data={}", id, type, data);
        if (StringUtils.isEmpty(data) || ":ping".equals(data)) {
            return;
        }

        try {
            MevShareEvent mevShareEvent = objectMapper.readValue(data, MevShareEvent.class);
            if (!this.emitter.isCancelled()) {
                this.emitter.onNext(mevShareEvent);
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("JsonRpcError parsing response", e);
            MevShareApiException mse = new MevShareApiException(e);
            if (this.emitter != null && !this.emitter.isCancelled()) {
                this.emitter.onError(mse);
            }
            throw mse;
        }
    }

    @Override
    public void onClosed(EventSource eventSource) {
        LOGGER.trace("EventSource closed");
        this.emitter.onComplete();
    }

    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        LOGGER.error("EventSource failed", t);
        this.emitter.onComplete();
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        LOGGER.trace("EventSource opened");
    }
}
