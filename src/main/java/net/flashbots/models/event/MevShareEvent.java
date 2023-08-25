package net.flashbots.models.event;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.flashbots.provider.json.HexStringToBigIntDeserializer;
import org.web3j.protocol.core.methods.response.EthLog;

/**
 * The type MevShareEvent.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class MevShareEvent {
    private List<Transaction> txs;

    private String hash;

    @JsonDeserialize(using = EthLog.LogResultDeserialiser.class)
    private List<EthLog.LogResult<?>> logs;

    @JsonDeserialize(using = HexStringToBigIntDeserializer.class)
    private BigInteger gasUsed;

    @JsonDeserialize(using = HexStringToBigIntDeserializer.class)
    private BigInteger mevGasPrice;

    /**
     * Instantiates a new MevShareEvent.
     */
    public MevShareEvent() {}

    /**
     * Gets txs.
     *
     * @return the txs
     */
    public List<Transaction> getTxs() {
        return txs;
    }

    /**
     * Sets txs.
     *
     * @param txs the txs
     * @return the txs
     */
    public MevShareEvent setTxs(List<Transaction> txs) {
        this.txs = txs;
        return this;
    }

    /**
     * Gets hash.
     *
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets hash.
     *
     * @param hash the hash
     * @return the hash
     */
    public MevShareEvent setHash(String hash) {
        this.hash = hash;
        return this;
    }

    /**
     * Gets logs.
     *
     * @return the logs
     */
    public List<EthLog.LogResult<?>> getLogs() {
        return logs;
    }

    /**
     * Sets logs.
     *
     * @param logs the logs
     * @return the logs
     */
    public MevShareEvent setLogs(List<EthLog.LogResult<?>> logs) {
        this.logs = logs;
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
    public MevShareEvent setGasUsed(BigInteger gasUsed) {
        this.gasUsed = gasUsed;
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
    public MevShareEvent setMevGasPrice(BigInteger mevGasPrice) {
        this.mevGasPrice = mevGasPrice;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MevShareEvent mevShareEvent)) return false;
        return Objects.equals(gasUsed, mevShareEvent.gasUsed)
                && Objects.equals(mevGasPrice, mevShareEvent.mevGasPrice)
                && Objects.equals(txs, mevShareEvent.txs)
                && Objects.equals(hash, mevShareEvent.hash)
                && Objects.equals(logs, mevShareEvent.logs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(txs, hash, logs, gasUsed, mevGasPrice);
    }

    @Override
    public String toString() {
        return "MevShareEvent{" + "txs="
                + txs + ", hash='"
                + hash + '\'' + ", logs="
                + logs + ", gasUsed="
                + gasUsed + ", mevGasPrice="
                + mevGasPrice + '}';
    }
}
