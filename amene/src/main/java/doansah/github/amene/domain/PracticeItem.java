package doansah.github.amene.domain;

import doansah.github.amene.domain.enums.DifficultyBand;
import doansah.github.amene.domain.enums.PracticeType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public record PracticeItem(
        String id,
        PracticeType type,
        String primaryTopicId,
        List<String> secondaryTopicIds,
        DifficultyBand difficultyBand,
        String prompt,
        String canonicalSolution,
        List<String> targets,
        Map<String, String> metadata
) {
    public PracticeItem {
        Objects.requireNonNull(id, "id cannot be null");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id cannot be blank");
        }
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(primaryTopicId, "primaryTopicId cannot be null");
        if (primaryTopicId.isBlank()) {
            throw new IllegalArgumentException("primaryTopicId cannot be blank");
        }
        Objects.requireNonNull(difficultyBand, "difficultyBand cannot be null");
        Objects.requireNonNull(prompt, "prompt cannot be null");
        secondaryTopicIds = List.copyOf(secondaryTopicIds == null ? List.of() : secondaryTopicIds);
        targets = List.copyOf(targets == null ? List.of() : targets);
        metadata = Map.copyOf(metadata == null ? Map.of() : metadata);
        canonicalSolution = canonicalSolution == null || canonicalSolution.isBlank() ? null : canonicalSolution;
    }
}
