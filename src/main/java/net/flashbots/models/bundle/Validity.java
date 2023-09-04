package net.flashbots.models.bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Validity.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class Validity {

    private List<RefundConfig> refundConfig = new ArrayList<>();

    private List<Refund> refund = new ArrayList<>();

    /**
     * Instantiates a new Validity.
     */
    public Validity() {}

    /**
     * Gets refund config.
     *
     * @return the refund config
     */
    public List<RefundConfig> getRefundConfig() {
        return refundConfig;
    }

    /**
     * Sets refund config.
     *
     * @param refundConfig the refund config
     * @return the refund config
     */
    public Validity setRefundConfig(List<RefundConfig> refundConfig) {
        this.refundConfig = refundConfig;
        return this;
    }

    /**
     * Gets refund.
     *
     * @return the refund
     */
    public List<Refund> getRefund() {
        return refund;
    }

    /**
     * Sets refund.
     *
     * @param refund the refund
     * @return the refund
     */
    public Validity setRefund(List<Refund> refund) {
        this.refund = refund;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Validity validity)) return false;
        return Objects.equals(refundConfig, validity.refundConfig) && Objects.equals(refund, validity.refund);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refundConfig, refund);
    }

    @Override
    public String toString() {
        return "Validity{" + "refundConfig=" + refundConfig + ", refund=" + refund + '}';
    }
}
