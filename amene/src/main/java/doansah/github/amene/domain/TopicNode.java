package doansah.github.amene.domain;

import doansah.github.amene.domain.enums.DifficultyBand;
import doansah.github.amene.domain.enums.ImportanceBand;
import doansah.github.amene.domain.enums.TopicKind;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public record TopicNode(
        String id,
        String name,
        TopicKind nodeKind,
        String description,
        String goal,
        List<String> tags,
        DifficultyBand difficultyBand,
        ImportanceBand importanceBand,
        double breadthBias,
        double depthBias,
        List<String> featureFlags,
        Map<String, String> metadata
) {
    public TopicNode {
        Objects.requireNonNull(id, "id cannot be null");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id cannot be blank");
        }
        Objects.requireNonNull(name, "name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        Objects.requireNonNull(nodeKind, "nodeKind cannot be null");
        Objects.requireNonNull(description, "description cannot be null");
        Objects.requireNonNull(goal, "goal cannot be null");
        Objects.requireNonNull(difficultyBand, "difficultyBand cannot be null");
        Objects.requireNonNull(importanceBand, "importanceBand cannot be null");
        validateBias("breadthBias", breadthBias);
        validateBias("depthBias", depthBias);
        tags = List.copyOf(tags == null ? List.of() : tags);
        featureFlags = List.copyOf(featureFlags == null ? List.of() : featureFlags);
        metadata = Map.copyOf(metadata == null ? Map.of() : metadata);
    }

    private static void validateBias(String field, double value) {
        if (Double.isNaN(value) || value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException(field + " must be between 0 and 1");
        }
    }
}
