package net.flashbots.models.bundle;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.flashbots.provider.json.HintPreferencesSerializer;

/**
 * The type Bundle privacy.
 *
 * @author kaichen
 * @since 0.1.0
 */
public class BundlePrivacy {

    private List<String> builders;

    @JsonSerialize(using = HintPreferencesSerializer.class)
    private HintPreferences hints;

    /**
     * Instantiates a new Bundle privacy.
     */
    public BundlePrivacy() {}

    /**
     * Gets builders.
     *
     * @return the builders
     */
    public List<String> getBuilders() {
        return builders;
    }

    /**
     * Sets builders.
     *
     * @param builders the builders
     * @return the builders
     */
    public BundlePrivacy setBuilders(List<String> builders) {
        this.builders = builders;
        return this;
    }

    /**
     * Gets hints.
     *
     * @return the hints
     */
    public HintPreferences getHints() {
        return hints;
    }

    /**
     * Sets hints.
     *
     * @param hints the hints
     * @return the hints
     */
    public BundlePrivacy setHints(HintPreferences hints) {
        this.hints = hints;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BundlePrivacy that)) return false;
        return Objects.equals(builders, that.builders) && Objects.equals(hints, that.hints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(builders, hints);
    }

    @Override
    public String toString() {
        return "BundlePrivacy{" + "builders=" + builders + ", hints=" + hints + '}';
    }
}
