package net.flashbots.models.bundle;

import java.util.Objects;

/**
 * The type RefundConfig.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class RefundConfig {

    private String address;

    private Integer percent;

    /**
     * Instantiates a new Refund config.
     */
    public RefundConfig() {}

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     * @return the address
     */
    public RefundConfig setAddress(String address) {
        this.address = address;
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
    public RefundConfig setPercent(Integer percent) {
        this.percent = percent;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefundConfig refundConfig)) return false;
        return Objects.equals(address, refundConfig.address) && Objects.equals(percent, refundConfig.percent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, percent);
    }

    @Override
    public String toString() {
        return "RefundConfig{" + "address='" + address + '\'' + ", percent=" + percent + '}';
    }
}
