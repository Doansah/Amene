package doansah.github.amene.api;

import doansah.github.amene.domain.GraphMetrics;
import doansah.github.amene.planning.Recommendation;

import java.util.List;
import java.util.Objects;

public record PlanResponse(
        GraphMetrics metrics,
        List<Recommendation> recommendations
) {
    public PlanResponse {
        Objects.requireNonNull(metrics, "metrics cannot be null");
        Objects.requireNonNull(recommendations, "recommendations cannot be null");
        recommendations = List.copyOf(recommendations);
    }
}
