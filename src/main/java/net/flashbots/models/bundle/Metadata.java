package net.flashbots.models.bundle;

import java.util.Objects;

/**
 * The type Metadata.
 */
public class Metadata {

    private String originId;

    /**
     * Instantiates a new Metadata.
     */
    public Metadata() {}

    /**
     * Gets origin id.
     *
     * @return the origin id
     */
    public String getOriginId() {
        return originId;
    }

    /**
     * Sets origin id.
     *
     * @param originId the origin id
     * @return the origin id
     */
    public Metadata setOriginId(String originId) {
        this.originId = originId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Metadata metadata)) return false;
        return Objects.equals(originId, metadata.originId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originId);
    }

    @Override
    public String toString() {
        return "Metadata{" + "originId='" + originId + '\'' + '}';
    }
}
