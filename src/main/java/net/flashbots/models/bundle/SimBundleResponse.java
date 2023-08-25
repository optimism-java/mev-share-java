package net.flashbots.models.bundle;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.flashbots.provider.json.HexStringToBigIntDeserializer;

/**
 * The type SimBundleResponse
 *
 * @author kaichen
 * @since 0.1.0
 */
public class SimBundleResponse {

    private Boolean success;

    private String error;

    @JsonDeserialize(using = HexStringToBigIntDeserializer.class)
    private BigInteger stateBlock;

    @JsonDeserialize(using = HexStringToBigIntDeserializer.class)
    private BigInteger mevGasPrice;

    @JsonDeserialize(using = HexStringToBigIntDeserializer.class)
    private BigInteger profit;

    @JsonDeserialize(using = HexStringToBigIntDeserializer.class)
    private BigInteger refundableValue;

    @JsonDeserialize(using = HexStringToBigIntDeserializer.class)
    private BigInteger gasUsed;

    private List<SimBundleLogs> logs;

    /**
     * Instantiates a new simulate bundle response
     */
    public SimBundleResponse() {}

    /**
     * Gets the success flag
     *
     * @return the success flag
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * Sets success flag
     *
     * @param success the success flag
     * @return this simulates bundle response
     */
    public SimBundleResponse setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    /**
     * Gets the error info
     *
     * @return the error info
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error info
     *
     * @param error the error info
     * @return this simulates bundle response
     */
    public SimBundleResponse setError(String error) {
        this.error = error;
        return this;
    }

    /**
     * Gets the state block
     *
     * @return the state block
     */
    public BigInteger getStateBlock() {
        return stateBlock;
    }

    /**
     * Sets state block
     *
     * @param stateBlock the state block
     * @return this simulates bundle response
     */
    public SimBundleResponse setStateBlock(BigInteger stateBlock) {
        this.stateBlock = stateBlock;
        return this;
    }

    /**
     * Gets the mev-share gas price
     *
     * @return the mev-share gas price
     */
    public BigInteger getMevGasPrice() {
        return mevGasPrice;
    }

    /**
     * Sets mev-share gas price
     *
     * @param mevGasPrice the mev-share gas price
     * @return this simulates bundle response
     */
    public SimBundleResponse setMevGasPrice(BigInteger mevGasPrice) {
        this.mevGasPrice = mevGasPrice;
        return this;
    }

    /**
     * Gets the profit
     *
     * @return the profit
     */
    public BigInteger getProfit() {
        return profit;
    }

    /**
     * Sets profit
     *
     * @param profit the profit
     * @return this simulates bundle response
     */
    public SimBundleResponse setProfit(BigInteger profit) {
        this.profit = profit;
        return this;
    }

    /**
     * Gets the refundable value
     *
     * @return the refundable value
     */
    public BigInteger getRefundableValue() {
        return refundableValue;
    }

    /**
     * Sets refundable value
     *
     * @param refundableValue the refundable value
     * @return this simulates bundle response
     */
    public SimBundleResponse setRefundableValue(BigInteger refundableValue) {
        this.refundableValue = refundableValue;
        return this;
    }

    /**
     * Gets the gas used
     *
     * @return the gas used
     */
    public BigInteger getGasUsed() {
        return gasUsed;
    }

    /**
     * Sets gas used
     *
     * @param gasUsed the gas used
     * @return this simulates bundle response
     */
    public SimBundleResponse setGasUsed(BigInteger gasUsed) {
        this.gasUsed = gasUsed;
        return this;
    }

    /**
     * Gets the list of simulate bundle logs
     *
     * @return the list of simulate bundle logs
     */
    public List<SimBundleLogs> getLogs() {
        return logs;
    }

    /**
     * Sets list of simulate bundle logs
     *
     * @param logs the list of simulate bundle logs
     * @return this simulates bundle response
     */
    public SimBundleResponse setLogs(List<SimBundleLogs> logs) {
        this.logs = logs;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimBundleResponse that)) {
            return false;
        }
        return Objects.equals(success, that.success)
                && Objects.equals(error, that.error)
                && Objects.equals(stateBlock, that.stateBlock)
                && Objects.equals(mevGasPrice, that.mevGasPrice)
                && Objects.equals(profit, that.profit)
                && Objects.equals(refundableValue, that.refundableValue)
                && Objects.equals(gasUsed, that.gasUsed)
                && Objects.equals(logs, that.logs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, error, stateBlock, mevGasPrice, profit, refundableValue, gasUsed, logs);
    }

    @Override
    public String toString() {
        return "SimBundleResponse{" + "success="
                + success + ", error='"
                + error + '\'' + ", stateBlock="
                + stateBlock + ", mevGasPrice="
                + mevGasPrice + ", profit="
                + profit + ", refundableValue="
                + refundableValue + ", gasUsed="
                + gasUsed + ", logs="
                + logs + '}';
    }
}
