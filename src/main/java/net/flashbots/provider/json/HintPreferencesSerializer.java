package net.flashbots.provider.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.flashbots.models.bundle.HintPreferences;

/**
 * The type HintPreferencesSerializer.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class HintPreferencesSerializer extends JsonSerializer<HintPreferences> {

    /**
     * Instantiates a new HintPreferencesSerializer.
     */
    public HintPreferencesSerializer() {
        super();
    }

    @Override
    public void serialize(HintPreferences value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        if (value.isCalldata()) {
            gen.writeString("calldata");
        }
        if (value.isContractAddress()) {
            gen.writeString("contract_address");
        }
        if (value.isFunctionSelector()) {
            gen.writeString("function_selector");
        }
        if (value.isLogs()) {
            gen.writeString("logs");
        }
        if (value.isTxHash()) {
            gen.writeString("tx_hash");
        }
        gen.writeString("hash");
        gen.writeEndArray();
    }
}
