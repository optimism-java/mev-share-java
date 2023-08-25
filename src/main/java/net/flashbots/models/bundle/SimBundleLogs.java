package net.flashbots.models.bundle;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.web3j.protocol.core.methods.response.EthLog;

/**
 * The type SimBundleLogs
 *
 * @author kaichen
 * @since 0.1.0
 */
public class SimBundleLogs {

    /**
     * Logs inside transactions.
     */
    @JsonDeserialize(using = EthLog.LogResultDeserialiser.class)
    private List<EthLog.LogResult<?>> txLogs;

    /**
     * Logs for bundles inside bundle.
     */
    private SimBundleLogs bundleLogs;

    /**
     * Instantiates a new simulate bundle logs.
     */
    public SimBundleLogs() {}

    /**
     * Gets list of tx logs
     *
     * @return list of tx logs
     */
    public List<EthLog.LogResult<?>> getTxLogs() {
        return txLogs;
    }

    /**
     * Sets the list of tx logs
     *
     * @param txLogs the list of tx logs
     * @return this simulates bundle logs
     */
    public SimBundleLogs setTxLogs(List<EthLog.LogResult<?>> txLogs) {
        this.txLogs = txLogs;
        return this;
    }

    /**
     * Gets the simulates bundle logs
     *
     * @return the simulates bundle logs
     */
    public SimBundleLogs getBundleLogs() {
        return bundleLogs;
    }

    /**
     * Sets the simulates bundle logs
     *
     * @param bundleLogs the simulates bundle logs
     * @return this simulates bundle logs
     */
    public SimBundleLogs setBundleLogs(SimBundleLogs bundleLogs) {
        this.bundleLogs = bundleLogs;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimBundleLogs that)) {
            return false;
        }
        return Objects.equals(txLogs, that.txLogs) && Objects.equals(bundleLogs, that.bundleLogs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(txLogs, bundleLogs);
    }

    @Override
    public String toString() {
        return "SimBundleLogs{" + "txLogs=" + txLogs + ", bundleLogs=" + bundleLogs + '}';
    }
}
