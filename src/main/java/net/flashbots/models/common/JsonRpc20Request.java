package net.flashbots.models.common;

import java.util.List;
import java.util.Objects;

/**
 * The type JsonRpc20Request.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class JsonRpc20Request {

    private String jsonrpc = "2.0";

    private String method;

    private List<?> params;

    private Long id;

    /**
     * Instantiates a new Json rpc 20 request.
     */
    public JsonRpc20Request() {}

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
    public JsonRpc20Request setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
        return this;
    }

    /**
     * Gets method.
     *
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets method.
     *
     * @param method the method
     * @return the method
     */
    public JsonRpc20Request setMethod(String method) {
        this.method = method;
        return this;
    }

    /**
     * Gets params.
     *
     * @return the params
     */
    public List<?> getParams() {
        return params;
    }

    /**
     * Sets params.
     *
     * @param params the params
     * @return the params
     */
    public JsonRpc20Request setParams(List<?> params) {
        this.params = params;
        return this;
    }

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
    public JsonRpc20Request setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JsonRpc20Request request)) return false;
        return Objects.equals(jsonrpc, request.jsonrpc)
                && Objects.equals(method, request.method)
                && Objects.equals(params, request.params)
                && Objects.equals(id, request.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jsonrpc, method, params, id);
    }

    @Override
    public String toString() {
        return "JsonRpc20Request{" + "jsonrpc='"
                + jsonrpc + '\'' + ", method='"
                + method + '\'' + ", params="
                + params + ", id="
                + id + '}';
    }
}
