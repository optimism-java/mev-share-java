package net.flashbots.models.bundle;

import java.math.BigInteger;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.flashbots.provider.json.BigIntToHexStringSerializer;

/**
 * The type SimBundleOptions
 *
 * @author kaichen
 * @since 0.1.0
 */
public class SimBundleOptions {

    /**
     * Block used for simulation state. Defaults to latest block.
     */
    @JsonSerialize(using = BigIntToHexStringSerializer.class)
    private BigInteger parentBlock;

    /**
     * Block number used for simulation, defaults to parentBlock.number + 1.
     */
    @JsonSerialize(using = BigIntToHexStringSerializer.class)
    private BigInteger blockNumber;

    /**
     * Coinbase used for simulation, defaults to parentBlock.coinbase.
     */
    private String coinbase;

    /**
     * Timestamp used for simulation, defaults to parentBlock.timestamp + 12.
     */
    @JsonSerialize(using = BigIntToHexStringSerializer.class)
    private BigInteger timestamp;

    /**
     * Gas limit used for simulation, defaults to parentBlock.gasLimit.
     */
    @JsonSerialize(using = BigIntToHexStringSerializer.class)
    private BigInteger gasLimit;

    /**
     * Base fee used for simulation, defaults to parentBlock.baseFeePerGas.
     */
    @JsonSerialize(using = BigIntToHexStringSerializer.class)
    private BigInteger baseFee;

    /**
     * Timeout in seconds, defaults to 5.
     */
    private Integer timeout;

    /**
     * Instantiates a new simulate bundle options.
     */
    public SimBundleOptions() {}

    /**
     * Gets parent block number
     *
     * @return the parent block number
     */
    public BigInteger getParentBlock() {
        return parentBlock;
    }

    /**
     * Sets parent block number
     *
     * @param parentBlock the parent block number
     * @return this simulates bundle options
     */
    public SimBundleOptions setParentBlock(BigInteger parentBlock) {
        this.parentBlock = parentBlock;
        return this;
    }

    /**
     * Gets block number
     *
     * @return the block number
     */
    public BigInteger getBlockNumber() {
        return blockNumber;
    }

    /**
     * Sets block number
     *
     * @param blockNumber the block number
     * @return this simulates bundle options
     */
    public SimBundleOptions setBlockNumber(BigInteger blockNumber) {
        this.blockNumber = blockNumber;
        return this;
    }

    /**
     * Gets the coinbase
     *
     * @return the coinbase
     */
    public String getCoinbase() {
        return coinbase;
    }

    /**
     * Sets coinbase
     *
     * @param coinbase the coinbase
     * @return this simulates bundle options
     */
    public SimBundleOptions setCoinbase(String coinbase) {
        this.coinbase = coinbase;
        return this;
    }

    /**
     * Gets the timestamp
     *
     * @return the timestamp
     */
    public BigInteger getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp
     *
     * @param timestamp the timestamp
     * @return this simulates bundle options
     */
    public SimBundleOptions setTimestamp(BigInteger timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Gets gas limit
     *
     * @return the gas limit
     */
    public BigInteger getGasLimit() {
        return gasLimit;
    }

    /**
     * Sets gas limit
     *
     * @param gasLimit the gas limit
     * @return this simulates bundle options
     */
    public SimBundleOptions setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
        return this;
    }

    /**
     * Gets the base fee
     *
     * @return the base fee
     */
    public BigInteger getBaseFee() {
        return baseFee;
    }

    /**
     * Sets base fee
     *
     * @param baseFee the base fee
     * @return this simulates bundle options
     */
    public SimBundleOptions setBaseFee(BigInteger baseFee) {
        this.baseFee = baseFee;
        return this;
    }

    /**
     * Gets the timeout
     *
     * @return the timeout
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * Sets timeout
     *
     * @param timeout the timeout
     * @return this simulates bundle options
     */
    public SimBundleOptions setTimeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimBundleOptions)) {
            return false;
        }
        SimBundleOptions that = (SimBundleOptions) o;
        return Objects.equals(parentBlock, that.parentBlock)
                && Objects.equals(blockNumber, that.blockNumber)
                && Objects.equals(coinbase, that.coinbase)
                && Objects.equals(timestamp, that.timestamp)
                && Objects.equals(gasLimit, that.gasLimit)
                && Objects.equals(baseFee, that.baseFee)
                && Objects.equals(timeout, that.timeout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentBlock, blockNumber, coinbase, timestamp, gasLimit, baseFee, timeout);
    }

    @Override
    public String toString() {
        return "SimBundleoptions{" + "parentBlock="
                + parentBlock + ", blockNumber="
                + blockNumber + ", coinbase='"
                + coinbase + '\'' + ", timestamp="
                + timestamp + ", gasLimit="
                + gasLimit + ", baseFee="
                + baseFee + ", timeout="
                + timeout + '}';
    }

    @Override
    public SimBundleOptions clone() {
        final SimBundleOptions clone = new SimBundleOptions();
        clone.setParentBlock(this.parentBlock)
                .setBlockNumber(this.blockNumber)
                .setCoinbase(this.coinbase)
                .setTimestamp(this.timestamp)
                .setGasLimit(this.gasLimit)
                .setBaseFee(this.baseFee)
                .setTimeout(this.timeout);
        return clone;
    }
}
