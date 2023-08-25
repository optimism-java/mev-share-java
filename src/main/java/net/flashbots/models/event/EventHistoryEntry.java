package net.flashbots.models.event;

import java.math.BigInteger;
import java.util.Objects;

/**
 * The type EventHistoryEntry.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class EventHistoryEntry {

    private BigInteger block;

    private BigInteger timestamp;

    private MevShareEvent hint;

    /**
     * Instantiates a new EventHistoryEntry.
     */
    public EventHistoryEntry() {}

    /**
     * Gets block.
     *
     * @return the block
     */
    public BigInteger getBlock() {
        return block;
    }

    /**
     * Sets block.
     *
     * @param block the block
     * @return the block
     */
    public EventHistoryEntry setBlock(BigInteger block) {
        this.block = block;
        return this;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public BigInteger getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     * @return the timestamp
     */
    public EventHistoryEntry setTimestamp(BigInteger timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Gets hint.
     *
     * @return the hint
     */
    public MevShareEvent getHint() {
        return hint;
    }

    /**
     * Sets hint.
     *
     * @param hint the hint
     * @return the hint
     */
    public EventHistoryEntry setHint(MevShareEvent hint) {
        this.hint = hint;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventHistoryEntry that)) return false;
        return Objects.equals(block, that.block)
                && Objects.equals(timestamp, that.timestamp)
                && Objects.equals(hint, that.hint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(block, timestamp, hint);
    }

    @Override
    public String toString() {
        return "EventHistoryEntry{" + "block=" + block + ", timestamp=" + timestamp + ", hint=" + hint + '}';
    }
}
