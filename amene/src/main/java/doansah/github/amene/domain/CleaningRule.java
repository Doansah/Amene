package doansah.github.amene.domain;

import doansah.github.amene.domain.enums.CleaningAction;

import java.util.Map;
import java.util.Objects;

public record CleaningRule(
        String id,
        String name,
        String predicate,
        CleaningAction action,
        Map<String, String> params
) {
    public CleaningRule {
        Objects.requireNonNull(id, "id cannot be null");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id cannot be blank");
        }
        Objects.requireNonNull(name, "name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        Objects.requireNonNull(predicate, "predicate cannot be null");
        Objects.requireNonNull(action, "action cannot be null");
        params = Map.copyOf(params == null ? Map.of() : params);
    }
}
