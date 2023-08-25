package net.flashbots.models.event;

import java.math.BigInteger;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.flashbots.provider.json.HexStringToBigIntDeserializer;

/**
 * The type Transaction.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class Transaction {
    private String to;

    private String callData;

    private String functionSelector;

    @JsonDeserialize(using = HexStringToBigIntDeserializer.class)
    private BigInteger mevGasPrice;

    @JsonDeserialize(using = HexStringToBigIntDeserializer.class)
    private BigInteger gasUsed;

    /**
     * Instantiates a new Transaction.
     */
    public Transaction() {}

    /**
     * Gets to.
     *
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets to.
     *
     * @param to the to
     * @return the to
     */
    public Transaction setTo(String to) {
        this.to = to;
        return this;
    }

    /**
     * Gets call data.
     *
     * @return the call data
     */
    public String getCallData() {
        return callData;
    }

    /**
     * Sets call data.
     *
     * @param callData the call data
     * @return the call data
     */
    public Transaction setCallData(String callData) {
        this.callData = callData;
        return this;
    }

    /**
     * Gets function selector.
     *
     * @return the function selector
     */
    public String getFunctionSelector() {
        return functionSelector;
    }

    /**
     * Sets function selector.
     *
     * @param functionSelector the function selector
     * @return the function selector
     */
    public Transaction setFunctionSelector(String functionSelector) {
        this.functionSelector = functionSelector;
        return this;
    }

    /**
     * Gets mev gas price.
     *
     * @return the mev gas price
     */
    public BigInteger getMevGasPrice() {
        return mevGasPrice;
    }

    /**
     * Sets mev gas price.
     *
     * @param mevGasPrice the mev gas price
     * @return the mev gas price
     */
    public Transaction setMevGasPrice(BigInteger mevGasPrice) {
        this.mevGasPrice = mevGasPrice;
        return this;
    }

    /**
     * Gets gas used.
     *
     * @return the gas used
     */
    public BigInteger getGasUsed() {
        return gasUsed;
    }

    /**
     * Sets gas used.
     *
     * @param gasUsed the gas used
     * @return the gas used
     */
    public Transaction setGasUsed(BigInteger gasUsed) {
        this.gasUsed = gasUsed;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(to, that.to)
                && Objects.equals(callData, that.callData)
                && Objects.equals(functionSelector, that.functionSelector)
                && Objects.equals(mevGasPrice, that.mevGasPrice)
                && Objects.equals(gasUsed, that.gasUsed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to, callData, functionSelector, mevGasPrice, gasUsed);
    }

    @Override
    public String toString() {
        return "Transaction{" + "to='"
                + to + '\'' + ", callData='"
                + callData + '\'' + ", functionSelector='"
                + functionSelector + '\'' + ", mevGasPrice="
                + mevGasPrice + ", gasUsed="
                + gasUsed + '}';
    }
}
