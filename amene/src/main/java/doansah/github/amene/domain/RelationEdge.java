package doansah.github.amene.domain;

import doansah.github.amene.domain.enums.RelationType;

import java.util.List;
import java.util.Objects;

public record RelationEdge(
        String sourceId,
        String targetId,
        RelationType relationType,
        double importance,
        double coverage,
        String andGroup,
        String notes,
        List<String> evidence
) {
    public RelationEdge {
        Objects.requireNonNull(sourceId, "sourceId cannot be null");
        Objects.requireNonNull(targetId, "targetId cannot be null");
        Objects.requireNonNull(relationType, "relationType cannot be null");
        if (sourceId.isBlank()) {
            throw new IllegalArgumentException("sourceId cannot be blank");
        }
        if (targetId.isBlank()) {
            throw new IllegalArgumentException("targetId cannot be blank");
        }
        validateScore("importance", importance);
        validateScore("coverage", coverage);
        evidence = List.copyOf(evidence == null ? List.of() : evidence);
        notes = notes == null ? "" : notes;
    }

    private static void validateScore(String field, double value) {
        if (Double.isNaN(value) || value < 0.0 || value > 10.0) {
            throw new IllegalArgumentException(field + " must be between 0 and 10");
        }
    }
}
