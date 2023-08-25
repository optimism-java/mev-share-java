package net.flashbots.models.bundle;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.flashbots.provider.json.BigIntToHexStringSerializer;

/**
 * The type PrivateTxParams
 *
 * @author kaichen
 * @since 0.1.0
 */
public class PrivateTxParams {

    private String tx;

    @JsonSerialize(using = BigIntToHexStringSerializer.class)
    private BigInteger maxBlockNumber;

    private Preferences preferences;

    /**
     * Instantiates a new private transaction params.
     */
    public PrivateTxParams() {}

    /**
     * Gets transaction
     *
     * @return the transaction
     */
    public String getTx() {
        return tx;
    }

    /**
     * Sets transaction
     *
     * @param tx signed transaction hex string
     * @return this private transaction params
     */
    public PrivateTxParams setTx(String tx) {
        this.tx = tx;
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
     * Sets the max block number
     *
     * @param maxBlockNumber the max block number
     * @return this private transaction params
     */
    public PrivateTxParams setMaxBlockNumber(BigInteger maxBlockNumber) {
        this.maxBlockNumber = maxBlockNumber;
        return this;
    }

    /**
     * Gets preferences
     *
     * @return the preferences
     */
    public Preferences getPreferences() {
        return preferences;
    }

    /**
     * Sets the preferences
     *
     * @param preferences the preferences
     * @return this private transaction params
     */
    public PrivateTxParams setPreferences(Preferences preferences) {
        this.preferences = preferences;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrivateTxParams that)) {
            return false;
        }
        return Objects.equals(tx, that.tx)
                && Objects.equals(maxBlockNumber, that.maxBlockNumber)
                && Objects.equals(preferences, that.preferences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tx, maxBlockNumber, preferences);
    }

    @Override
    public String toString() {
        return "PrivateTxParams{" + "tx='"
                + tx + '\'' + ", maxBlockNumber="
                + maxBlockNumber + ", preferences="
                + preferences + '}';
    }

    /**
     * Create private tx params from signed tx and options.
     *
     * @param tx      signed tx
     * @param options private tx options
     * @return the private tx params
     */
    public static PrivateTxParams from(String tx, PrivateTxOptions options) {
        var params = new PrivateTxParams();
        params.setTx(tx);

        Preferences preferences = new Preferences();
        preferences.setFast(true);

        if (options != null) {
            params.setMaxBlockNumber(options.getMaxBlockNumber());

            preferences.setBuilders(options.getBuilders());
            preferences.setPrivacy(new BundlePrivacy().setHints(options.getHints()));
        }
        params.setPreferences(preferences);
        return params;
    }

    /**
     * the type preferences
     */
    public static class Preferences {

        private boolean fast;

        private BundlePrivacy privacy;

        private List<String> builders;

        /**
         * Instantiates a new preferences
         */
        public Preferences() {}

        /**
         * Gets fast flag.
         *
         * @return the fast flag
         */
        public boolean getFast() {
            return fast;
        }

        /**
         * Sets the fast flag
         *
         * @param fast the fast flag
         * @return this preferences
         */
        public Preferences setFast(boolean fast) {
            this.fast = fast;
            return this;
        }

        /**
         * Gets bundle privacy
         *
         * @return the bundle privacy
         */
        public BundlePrivacy getPrivacy() {
            return privacy;
        }

        /**
         * Sets the bundle privacy
         *
         * @param privacy the bundle privacy
         * @return this preferences
         */
        public Preferences setPrivacy(BundlePrivacy privacy) {
            this.privacy = privacy;
            return this;
        }

        /**
         * Gets the builders
         *
         * @return the builders
         */
        public List<String> getBuilders() {
            return builders;
        }

        /**
         * Sets the builders
         *
         * @param builders the buidlers
         * @return this preferences
         */
        public Preferences setBuilders(List<String> builders) {
            this.builders = builders;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Preferences that)) {
                return false;
            }
            return fast == that.fast
                    && Objects.equals(privacy, that.privacy)
                    && Objects.equals(builders, that.builders);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fast, privacy, builders);
        }

        @Override
        public String toString() {
            return "Preferences{" + "fast=" + fast + ", privacy=" + privacy + ", builders=" + builders + '}';
        }
    }
}
