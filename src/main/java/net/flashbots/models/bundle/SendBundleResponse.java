package net.flashbots.models.bundle;

import java.util.Objects;

/**
 * The type SendBundleResponse.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class SendBundleResponse {

    private String bundleHash;

    /**
     * Instantiates a new Send bundle response.
     */
    public SendBundleResponse() {}

    /**
     * Gets bundle hash.
     *
     * @return the bundle hash
     */
    public String getBundleHash() {
        return bundleHash;
    }

    /**
     * Sets bundle hash.
     *
     * @param bundleHash the bundle hash
     * @return the bundle hash
     */
    public SendBundleResponse setBundleHash(String bundleHash) {
        this.bundleHash = bundleHash;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SendBundleResponse that)) return false;
        return Objects.equals(bundleHash, that.bundleHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bundleHash);
    }

    @Override
    public String toString() {
        return "SendBundleResponse{" + "bundleHash='" + bundleHash + '\'' + '}';
    }
}
