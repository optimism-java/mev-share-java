package net.flashbots.models.bundle;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

/**
 * The type PrivateTxOptions
 *
 * @author kaichen
 * @since 0.1.0
 */
public class PrivateTxOptions {

    private HintPreferences hints;

    private BigInteger maxBlockNumber;

    private List<String> builders;

    /**
     * Instantiates a new private transaction options.
     */
    public PrivateTxOptions() {}

    /**
     * Gets hints
     *
     * @return the hints
     */
    public HintPreferences getHints() {
        return hints;
    }

    /**
     * Sets hints
     *
     * @param hints the hints
     * @return this private transaction options
     */
    public PrivateTxOptions setHints(HintPreferences hints) {
        this.hints = hints;
        return this;
    }

    /**
     * Gets max block number
     *
     * @return the max block number
     */
    public BigInteger getMaxBlockNumber() {
        return maxBlockNumber;
    }

    /**
     * Sets max block number
     *
     * @param maxBlockNumber the max block number
     * @return this private transaction options
     */
    public PrivateTxOptions setMaxBlockNumber(BigInteger maxBlockNumber) {
        this.maxBlockNumber = maxBlockNumber;
        return this;
    }

    /**
     * Gets builders
     *
     * @return the builders
     */
    public List<String> getBuilders() {
        return builders;
    }

    /**
     * Sets builders
     *
     * @param builders the builders
     * @return this private transaction options
     */
    public PrivateTxOptions setBuilders(List<String> builders) {
        this.builders = builders;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrivateTxOptions that)) {
            return false;
        }
        return Objects.equals(hints, that.hints)
                && Objects.equals(maxBlockNumber, that.maxBlockNumber)
                && Objects.equals(builders, that.builders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hints, maxBlockNumber, builders);
    }

    @Override
    public String toString() {
        return "PrivateTxOptions{" + "hints="
                + hints + ", maxBlockNumber="
                + maxBlockNumber + ", builders="
                + builders + '}';
    }
}
