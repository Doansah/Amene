package doansah.github.amene.planning;

import java.util.List;
import java.util.Objects;

public record Recommendation(
        PlanStyle planStyle,
        String summary,
        List<PlanStep> steps
) {
    public Recommendation {
        Objects.requireNonNull(planStyle, "planStyle cannot be null");
        Objects.requireNonNull(summary, "summary cannot be null");
        if (summary.isBlank()) {
            throw new IllegalArgumentException("summary cannot be blank");
        }
        Objects.requireNonNull(steps, "steps cannot be null");
        steps = List.copyOf(steps);
    }
}
