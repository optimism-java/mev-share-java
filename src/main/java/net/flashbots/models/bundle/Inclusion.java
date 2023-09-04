package net.flashbots.models.bundle;

import java.math.BigInteger;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.flashbots.provider.json.BigIntToHexStringSerializer;

/**
 * The type Inclusion.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class Inclusion {

    @JsonSerialize(using = BigIntToHexStringSerializer.class)
    private BigInteger block;

    @JsonSerialize(using = BigIntToHexStringSerializer.class)
    private BigInteger maxBlock;

    /**
     * Instantiates a new Inclusion.
     */
    public Inclusion() {}

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
    public Inclusion setBlock(BigInteger block) {
        this.block = block;
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
    public Inclusion setMaxBlock(BigInteger maxBlock) {
        this.maxBlock = maxBlock;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inclusion inclusion)) return false;
        return Objects.equals(block, inclusion.block) && Objects.equals(maxBlock, inclusion.maxBlock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(block, maxBlock);
    }

    @Override
    public String toString() {
        return "Inclusion{" + "block=" + block + ", maxBlock=" + maxBlock + '}';
    }
}
