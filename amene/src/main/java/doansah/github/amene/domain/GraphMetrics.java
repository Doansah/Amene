package doansah.github.amene.domain;

import java.util.Map;
import java.util.Objects;

public record GraphMetrics(
        int depth,
        double avgBranching,
        double overlapRatio,
        Map<String, Double> centrality,
        double edgeWeightSum,
        double density,
        Map<String, Double> majorCoverageNeed,
        double breadthScore,
        double depthScore
) {
    public GraphMetrics {
        if (depth < 0) {
            throw new IllegalArgumentException("depth cannot be negative");
        }
        validateNonNegative("avgBranching", avgBranching);
        validateRatio("overlapRatio", overlapRatio);
        Objects.requireNonNull(centrality, "centrality cannot be null");
        Objects.requireNonNull(majorCoverageNeed, "majorCoverageNeed cannot be null");
        centrality = Map.copyOf(centrality);
        majorCoverageNeed = Map.copyOf(majorCoverageNeed);
        validateNonNegative("edgeWeightSum", edgeWeightSum);
        validateNonNegative("density", density);
        validateRatio("breadthScore", breadthScore);
        validateRatio("depthScore", depthScore);
    }

    private static void validateNonNegative(String field, double value) {
        if (Double.isNaN(value) || value < 0.0) {
            throw new IllegalArgumentException(field + " cannot be negative or NaN");
        }
    }

    private static void validateRatio(String field, double value) {
        if (Double.isNaN(value) || value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException(field + " must be between 0 and 1");
        }
    }
}
