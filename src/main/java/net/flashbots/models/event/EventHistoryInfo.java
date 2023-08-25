package net.flashbots.models.event;

import java.math.BigInteger;
import java.util.Objects;

/**
 * The type EventHistoryInfo.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class EventHistoryInfo {

    private BigInteger count;

    private BigInteger minBlock;

    private BigInteger maxBlock;

    private BigInteger minTimestamp;

    private BigInteger maxTimestamp;

    private BigInteger maxLimit;

    /**
     * Instantiates a new EventHistoryInfo.
     */
    public EventHistoryInfo() {}

    /**
     * Gets count.
     *
     * @return the count
     */
    public BigInteger getCount() {
        return count;
    }

    /**
     * Sets count.
     *
     * @param count the count
     * @return the count
     */
    public EventHistoryInfo setCount(BigInteger count) {
        this.count = count;
        return this;
    }

    /**
     * Gets min block.
     *
     * @return the min block
     */
    public BigInteger getMinBlock() {
        return minBlock;
    }

    /**
     * Sets min block.
     *
     * @param minBlock the min block
     * @return the min block
     */
    public EventHistoryInfo setMinBlock(BigInteger minBlock) {
        this.minBlock = minBlock;
        return this;
    }

    /**
     * Gets max block.
     *
     * @return the max block
     */
    public BigInteger getMaxBlock() {
        return maxBlock;
    }

    /**
     * Sets max block.
     *
     * @param maxBlock the max block
     * @return the max block
     */
    public EventHistoryInfo setMaxBlock(BigInteger maxBlock) {
        this.maxBlock = maxBlock;
        return this;
    }

    /**
     * Gets min timestamp.
     *
     * @return the min timestamp
     */
    public BigInteger getMinTimestamp() {
        return minTimestamp;
    }

    /**
     * Sets min timestamp.
     *
     * @param minTimestamp the min timestamp
     * @return the min timestamp
     */
    public EventHistoryInfo setMinTimestamp(BigInteger minTimestamp) {
        this.minTimestamp = minTimestamp;
        return this;
    }

    /**
     * Gets max timestamp.
     *
     * @return the max timestamp
     */
    public BigInteger getMaxTimestamp() {
        return maxTimestamp;
    }

    /**
     * Sets max timestamp.
     *
     * @param maxTimestamp the max timestamp
     * @return the max timestamp
     */
    public EventHistoryInfo setMaxTimestamp(BigInteger maxTimestamp) {
        this.maxTimestamp = maxTimestamp;
        return this;
    }

    /**
     * Gets max limit.
     *
     * @return the max limit
     */
    public BigInteger getMaxLimit() {
        return maxLimit;
    }

    /**
     * Sets max limit.
     *
     * @param maxLimit the max limit
     * @return the max limit
     */
    public EventHistoryInfo setMaxLimit(BigInteger maxLimit) {
        this.maxLimit = maxLimit;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventHistoryInfo that)) return false;
        return Objects.equals(count, that.count)
                && Objects.equals(minBlock, that.minBlock)
                && Objects.equals(maxBlock, that.maxBlock)
                && Objects.equals(minTimestamp, that.minTimestamp)
                && Objects.equals(maxTimestamp, that.maxTimestamp)
                && Objects.equals(maxLimit, that.maxLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, minBlock, maxBlock, minTimestamp, maxTimestamp, maxLimit);
    }

    @Override
    public String toString() {
        return "EventHistoryInfo{" + "count="
                + count + ", minBlock="
                + minBlock + ", maxBlock="
                + maxBlock + ", minTimestamp="
                + minTimestamp + ", maxTimestamp="
                + maxTimestamp + ", maxLimit="
                + maxLimit + '}';
    }
}
