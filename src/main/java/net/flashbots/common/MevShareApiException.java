package net.flashbots.common;

import net.flashbots.models.common.JsonRpc20Response;

/**
 * The type MevShareApiException
 *
 * @author kaichen
 * @since 0.1.0
 */
public class MevShareApiException extends RuntimeException {

    /**
     * The jsonRpcError
     */
    private JsonRpc20Response.JsonRpcError jsonRpcError;

    /**
     * Instantiates a new MevShareApiException.
     *
     * @param jsonRpcError the jsonRpcError
     */
    public MevShareApiException(JsonRpc20Response.JsonRpcError jsonRpcError) {
        super();
        this.jsonRpcError = jsonRpcError;
    }

    /**
     * Instantiates a new MevShareApiException.
     *
     * @param cause the cause
     */
    public MevShareApiException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new MevShareApiException.
     *
     * @param message the message
     * @param throwable the throwable
     */
    public MevShareApiException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Instantiates a new MevShareApiException.
     *
     * @param message the message
     */
    public MevShareApiException(String message) {
        super(message);
    }

    /**
     * Gets jsonRpcError.
     *
     * @return the jsonRpcError
     */
    public JsonRpc20Response.JsonRpcError getError() {
        return jsonRpcError;
    }

    /**
     * Sets jsonRpcError.
     *
     * @param jsonRpcError the jsonRpcError
     */
    public void setError(JsonRpc20Response.JsonRpcError jsonRpcError) {
        this.jsonRpcError = jsonRpcError;
    }
}
