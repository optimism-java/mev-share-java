package net.flashbots.models.common;

import java.util.Objects;

/**
 * The type Network.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class Network {

    /**
     * The constant MAINNET.
     */
    public static final Network MAINNET = new Network()
            .setName("mainnet")
            .setChainId(1)
            .setRpcUrl("https://relay.flashbots.net")
            .setStreamUrl("https://mev-share.flashbots.net");

    /**
     * The constant GOERLI.
     */
    public static final Network GOERLI = new Network()
            .setName("goerli")
            .setChainId(5)
            .setRpcUrl("https://relay-goerli.flashbots.net")
            .setStreamUrl("https://mev-share-goerli.flashbots.net");

    /**
     * Instantiates a new Network.
     */
    public Network() {}

    private String name;

    private long chainId;

    private String rpcUrl;

    private String streamUrl;

    /**
     * Name string.
     *
     * @return the string
     */
    public String name() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     * @return the name
     */
    public Network setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Chain id long.
     *
     * @return the long
     */
    public long chainId() {
        return chainId;
    }

    /**
     * Sets chain id.
     *
     * @param chainId the chain id
     * @return the chain id
     */
    public Network setChainId(long chainId) {
        this.chainId = chainId;
        return this;
    }

    /**
     * Rpc url string.
     *
     * @return the string
     */
    public String rpcUrl() {
        return rpcUrl;
    }

    /**
     * Sets rpc url.
     *
     * @param rpcUrl the rpc url
     * @return the rpc url
     */
    public Network setRpcUrl(String rpcUrl) {
        this.rpcUrl = rpcUrl;
        return this;
    }

    /**
     * Stream url string.
     *
     * @return the string
     */
    public String streamUrl() {
        return streamUrl;
    }

    /**
     * Sets stream url.
     *
     * @param streamUrl the stream url
     * @return the stream url
     */
    public Network setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Network that)) return false;
        return chainId == that.chainId
                && Objects.equals(name, that.name)
                && Objects.equals(rpcUrl, that.rpcUrl)
                && Objects.equals(streamUrl, that.streamUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, chainId, rpcUrl, streamUrl);
    }

    @Override
    public String toString() {
        return "Network{" + "name='"
                + name + '\'' + ", chainId="
                + chainId + ", rpcUrl='"
                + rpcUrl + '\'' + ", streamUrl='"
                + streamUrl + '\'' + '}';
    }
}
