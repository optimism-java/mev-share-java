package net.flashbots.models.event;

import java.math.BigInteger;
import java.util.Objects;

/**
 * The type EventHistoryParams.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class EventHistoryParams {

    private BigInteger blockStart;

    private BigInteger blockEnd;

    private BigInteger timestampStart;

    private BigInteger timestampEnd;

    private Integer limit;

    private Integer offset;

    /**
     * Instantiates a new EventHistoryParams.
     */
    public EventHistoryParams() {}

    /**
     * Gets block start.
     *
     * @return the block start
     */
    public BigInteger getBlockStart() {
        return blockStart;
    }

    /**
     * Sets block start.
     *
     * @param blockStart the block start
     * @return the block start
     */
    public EventHistoryParams setBlockStart(BigInteger blockStart) {
        this.blockStart = blockStart;
        return this;
    }

    /**
     * Gets block end.
     *
     * @return the block end
     */
    public BigInteger getBlockEnd() {
        return blockEnd;
    }

    /**
     * Sets block end.
     *
     * @param blockEnd the block end
     * @return the block end
     */
    public EventHistoryParams setBlockEnd(BigInteger blockEnd) {
        this.blockEnd = blockEnd;
        return this;
    }

    /**
     * Gets timestamp start.
     *
     * @return the timestamp start
     */
    public BigInteger getTimestampStart() {
        return timestampStart;
    }

    /**
     * Sets timestamp start.
     *
     * @param timestampStart the timestamp start
     * @return the timestamp start
     */
    public EventHistoryParams setTimestampStart(BigInteger timestampStart) {
        this.timestampStart = timestampStart;
        return this;
    }

    /**
     * Gets timestamp end.
     *
     * @return the timestamp end
     */
    public BigInteger getTimestampEnd() {
        return timestampEnd;
    }

    /**
     * Sets timestamp end.
     *
     * @param timestampEnd the timestamp end
     * @return the timestamp end
     */
    public EventHistoryParams setTimestampEnd(BigInteger timestampEnd) {
        this.timestampEnd = timestampEnd;
        return this;
    }

    /**
     * Gets limit.
     *
     * @return the limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Sets limit.
     *
     * @param limit the limit
     * @return the limit
     */
    public EventHistoryParams setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Gets offset.
     *
     * @return the offset
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * Sets offset.
     *
     * @param offset the offset
     * @return the offset
     */
    public EventHistoryParams setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventHistoryParams that)) return false;
        return Objects.equals(blockStart, that.blockStart)
                && Objects.equals(blockEnd, that.blockEnd)
                && Objects.equals(timestampStart, that.timestampStart)
                && Objects.equals(timestampEnd, that.timestampEnd)
                && Objects.equals(limit, that.limit)
                && Objects.equals(offset, that.offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockStart, blockEnd, timestampStart, timestampEnd, limit, offset);
    }

    @Override
    public String toString() {
        return "EventHistoryParams{" + "blockStart="
                + blockStart + ", blockEnd="
                + blockEnd + ", timestampStart="
                + timestampStart + ", timestampEnd="
                + timestampEnd + ", limit="
                + limit + ", offset="
                + offset + '}';
    }
}
