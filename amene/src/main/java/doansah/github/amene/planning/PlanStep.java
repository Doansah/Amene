package doansah.github.amene.planning;

import java.util.List;
import java.util.Objects;

public record PlanStep(
        String title,
        String description,
        List<String> topicIds,
        List<String> practiceItemIds
) {
    public PlanStep {
        Objects.requireNonNull(title, "title cannot be null");
        if (title.isBlank()) {
            throw new IllegalArgumentException("title cannot be blank");
        }
        Objects.requireNonNull(description, "description cannot be null");
        topicIds = List.copyOf(topicIds == null ? List.of() : topicIds);
        practiceItemIds = List.copyOf(practiceItemIds == null ? List.of() : practiceItemIds);
    }
}
