package net.flashbots.provider.json;

import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.web3j.utils.Numeric;

/**
 * The type HexStringToBigIntDeserializer.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class HexStringToBigIntDeserializer extends JsonDeserializer<BigInteger> {
    /**
     * Instantiates a new HexStringToBigIntDeserializer.
     */
    public HexStringToBigIntDeserializer() {}

    @Override
    public BigInteger deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return Numeric.toBigInt(p.getValueAsString());
    }
}
