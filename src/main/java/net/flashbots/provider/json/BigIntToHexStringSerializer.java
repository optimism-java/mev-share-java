package net.flashbots.provider.json;

import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.web3j.utils.Numeric;

/**
 * The type BigIntToHexStringSerializer.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class BigIntToHexStringSerializer extends JsonSerializer<BigInteger> {

    /**
     * Instantiates a new BigIntToHexStringSerializer.
     */
    public BigIntToHexStringSerializer() {
        super();
    }

    @Override
    public void serialize(BigInteger value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(Numeric.toHexStringWithPrefix(value));
    }
}
