package net.flashbots.models.bundle;

import java.util.List;
import java.util.Objects;

/**
 * The type Bundle params.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class BundleParams {

    private String version = "v0.1";

    private Inclusion inclusion;

    private List<BundleItemType> body;

    private Validity validity;

    private BundlePrivacy privacy;

    private Metadata metadata;

    /**
     * Instantiates a new Bundle params.
     */
    public BundleParams() {}

    /**
     * Gets version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version the version
     * @return the version
     */
    public BundleParams setVersion(String version) {
        this.version = version;
        return this;
    }

    /**
     * Gets inclusion.
     *
     * @return the inclusion
     */
    public Inclusion getInclusion() {
        return inclusion;
    }

    /**
     * Sets inclusion.
     *
     * @param inclusion the inclusion
     * @return the inclusion
     */
    public BundleParams setInclusion(Inclusion inclusion) {
        this.inclusion = inclusion;
        return this;
    }

    /**
     * Gets body.
     *
     * @return the body
     */
    public List<BundleItemType> getBody() {
        return body;
    }

    /**
     * Sets body.
     *
     * @param body the body
     * @return the body
     */
    public BundleParams setBody(List<BundleItemType> body) {
        this.body = body;
        return this;
    }

    /**
     * Gets validity.
     *
     * @return the validity
     */
    public Validity getValidity() {
        return validity;
    }

    /**
     * Sets validity.
     *
     * @param validity the validity
     * @return the validity
     */
    public BundleParams setValidity(Validity validity) {
        this.validity = validity;
        return this;
    }

    /**
     * Gets privacy.
     *
     * @return the privacy
     */
    public BundlePrivacy getPrivacy() {
        return privacy;
    }

    /**
     * Sets privacy.
     *
     * @param privacy the privacy
     * @return the privacy
     */
    public BundleParams setPrivacy(BundlePrivacy privacy) {
        this.privacy = privacy;
        return this;
    }

    /**
     * Gets metadata.
     *
     * @return the metadata
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Sets metadata.
     *
     * @param metadata the metadata
     * @return the metadata
     */
    public BundleParams setMetadata(Metadata metadata) {
        this.metadata = metadata;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BundleParams that)) return false;
        return Objects.equals(version, that.version)
                && Objects.equals(inclusion, that.inclusion)
                && Objects.equals(body, that.body)
                && Objects.equals(validity, that.validity)
                && Objects.equals(privacy, that.privacy)
                && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, inclusion, body, validity, privacy, metadata);
    }

    @Override
    public String toString() {
        return "BundleParams{" + "version='"
                + version + '\'' + ", inclusion="
                + inclusion + ", body="
                + body + ", validity="
                + validity + ", privacy="
                + privacy + ", metadata="
                + metadata + '}';
    }

    @Override
    public BundleParams clone() {
        BundleParams clone = new BundleParams();
        clone.setVersion(version);
        clone.setInclusion(inclusion);
        clone.setBody(body);
        clone.setValidity(validity);
        clone.setPrivacy(privacy);
        clone.setMetadata(metadata);
        return clone;
    }
}
