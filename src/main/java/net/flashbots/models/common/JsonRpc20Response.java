package net.flashbots.models.common;

import java.util.Objects;

/**
 * The type JsonRpc20Response.
 *
 * @param <T>   the type parameter
 * @author kaichen
 * @since 0.1.0
 */
public class JsonRpc20Response<T> {

    /** The type JsonRpcError. */
    public static class JsonRpcError {

        private Integer code;

        private String message;

        /**
         * Instantiates a new JsonRpcError.
         */
        public JsonRpcError() {}

        /**
         * Instantiates a new JsonRpcError.
         *
         * @param message error message
         */
        public JsonRpcError(String message) {
            this.message = message;
        }

        /**
         * Gets code.
         *
         * @return the code
         */
        public Integer getCode() {
            return code;
        }

        /**
         * Sets code.
         *
         * @param code the code
         */
        public void setCode(Integer code) {
            this.code = code;
        }

        /**
         * Gets message.
         *
         * @return the message
         */
        public String getMessage() {
            return message;
        }

        /**
         * Sets message.
         *
         * @param message the message
         */
        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof JsonRpcError that)) return false;
            return Objects.equals(code, that.code) && Objects.equals(message, that.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, message);
        }

        @Override
        public String toString() {
            return "JsonRpcError{" + "code=" + code + ", message='" + message + '\'' + '}';
        }
    }

    private Long id;

    private String jsonrpc = "2.0";

    private T result;

    private JsonRpcError jsonRpcError;

    private Throwable throwable;

    /**
     * Instantiates a new Json rpc 20 response.
     */
    public JsonRpc20Response() {}

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     * @return the id
     */
    public JsonRpc20Response<T> setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Gets jsonrpc.
     *
     * @return the jsonrpc
     */
    public String getJsonrpc() {
        return jsonrpc;
    }

    /**
     * Sets jsonrpc.
     *
     * @param jsonrpc the jsonrpc
     * @return the jsonrpc
     */
    public JsonRpc20Response<T> setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
        return this;
    }

    /**
     * Gets result.
     *
     * @return the result
     */
    public T getResult() {
        return result;
    }

    /**
     * Sets result.
     *
     * @param result the result
     * @return the result
     */
    public JsonRpc20Response<T> setResult(T result) {
        this.result = result;
        return this;
    }

    /**
     * Gets jsonRpcError.
     *
     * @return the jsonRpcError
     */
    public JsonRpcError getError() {
        return jsonRpcError;
    }

    /**
     * Sets jsonRpcError.
     *
     * @param jsonRpcError the jsonRpcError
     * @return the jsonRpcError
     */
    public JsonRpc20Response<T> setError(JsonRpcError jsonRpcError) {
        this.jsonRpcError = jsonRpcError;
        return this;
    }

    /**
     * Gets throwable.
     *
     * @return the throwable
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /**
     * Sets throwable.
     *
     * @param throwable the throwable
     * @return the throwable
     */
    public JsonRpc20Response<T> setThrowable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonRpc20Response<?> that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(jsonrpc, that.jsonrpc)
                && Objects.equals(result, that.result)
                && Objects.equals(jsonRpcError, that.jsonRpcError)
                && Objects.equals(throwable, that.throwable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, jsonrpc, result, jsonRpcError, throwable);
    }

    @Override
    public String toString() {
        return "JsonRpc20Response{"
                + "id="
                + id
                + ", jsonrpc='"
                + jsonrpc
                + '\''
                + ", result="
                + result
                + ", jsonRpcError="
                + jsonRpcError
                + ", throwable="
                + throwable
                + '}';
    }
}
