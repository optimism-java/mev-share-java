package net.flashbots.models.bundle;

import java.util.Objects;

/**
 * The type Refund.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class Refund {

    private Integer bodyIdx;

    private Integer percent;

    /**
     * Instantiates a new Refund.
     */
    public Refund() {}

    /**
     * Gets body idx.
     *
     * @return the body idx
     */
    public Integer getBodyIdx() {
        return bodyIdx;
    }

    /**
     * Sets body idx.
     *
     * @param bodyIdx the body idx
     * @return the body idx
     */
    public Refund setBodyIdx(Integer bodyIdx) {
        this.bodyIdx = bodyIdx;
        return this;
    }

    /**
     * Gets percent.
     *
     * @return the percent
     */
    public Integer getPercent() {
        return percent;
    }

    /**
     * Sets percent.
     *
     * @param percent the percent
     * @return the percent
     */
    public Refund setPercent(Integer percent) {
        this.percent = percent;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Refund that)) return false;
        return Objects.equals(bodyIdx, that.bodyIdx) && Objects.equals(percent, that.percent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bodyIdx, percent);
    }

    @Override
    public String toString() {
        return "Refund{" + "bodyIdx=" + bodyIdx + ", percent=" + percent + '}';
    }
}
