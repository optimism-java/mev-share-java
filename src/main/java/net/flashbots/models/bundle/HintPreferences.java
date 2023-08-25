package net.flashbots.models.bundle;

import java.util.Objects;

/**
 * The type Hint preferences.
 */
public class HintPreferences {
    private boolean calldata;

    private boolean contractAddress;

    private boolean functionSelector;

    private boolean logs;

    private boolean txHash;

    /**
     * Instantiates a new Hint preferences.
     */
    public HintPreferences() {}

    /**
     * Is calldata boolean.
     *
     * @return the boolean
     */
    public boolean isCalldata() {
        return calldata;
    }

    /**
     * Sets calldata.
     *
     * @param calldata the calldata
     * @return the calldata
     */
    public HintPreferences setCalldata(boolean calldata) {
        this.calldata = calldata;
        return this;
    }

    /**
     * Is contract address boolean.
     *
     * @return the boolean
     */
    public boolean isContractAddress() {
        return contractAddress;
    }

    /**
     * Sets contract address.
     *
     * @param contractAddress the contract address
     * @return the contract address
     */
    public HintPreferences setContractAddress(boolean contractAddress) {
        this.contractAddress = contractAddress;
        return this;
    }

    /**
     * Is function selector boolean.
     *
     * @return the boolean
     */
    public boolean isFunctionSelector() {
        return functionSelector;
    }

    /**
     * Sets function selector.
     *
     * @param functionSelector the function selector
     * @return the function selector
     */
    public HintPreferences setFunctionSelector(boolean functionSelector) {
        this.functionSelector = functionSelector;
        return this;
    }

    /**
     * Is logs boolean.
     *
     * @return the boolean
     */
    public boolean isLogs() {
        return logs;
    }

    /**
     * Sets logs.
     *
     * @param logs the logs
     * @return the logs
     */
    public HintPreferences setLogs(boolean logs) {
        this.logs = logs;
        return this;
    }

    /**
     * Is tx hash boolean.
     *
     * @return the boolean
     */
    public boolean isTxHash() {
        return txHash;
    }

    /**
     * Sets tx hash.
     *
     * @param txHash the tx hash
     * @return the tx hash
     */
    public HintPreferences setTxHash(boolean txHash) {
        this.txHash = txHash;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HintPreferences that)) return false;
        return calldata == that.calldata
                && contractAddress == that.contractAddress
                && functionSelector == that.functionSelector
                && logs == that.logs
                && txHash == that.txHash;
    }

    @Override
    public int hashCode() {
        return Objects.hash(calldata, contractAddress, functionSelector, logs, txHash);
    }

    @Override
    public String toString() {
        return "HintPreferences{" + "calldata="
                + calldata + ", contractAddress="
                + contractAddress + ", functionSelector="
                + functionSelector + ", logs="
                + logs + ", txHash="
                + txHash + '}';
    }
}
