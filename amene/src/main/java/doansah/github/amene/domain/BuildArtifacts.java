package doansah.github.amene.domain;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record BuildArtifacts(
        BuildRequest buildRequest,
        List<BuildQAPair> qaPairs,
        double confidence
) {
    public BuildArtifacts {
        Objects.requireNonNull(buildRequest, "buildRequest cannot be null");
        qaPairs = List.copyOf(qaPairs == null ? List.of() : qaPairs);
        if (Double.isNaN(confidence) || confidence < 0.0 || confidence > 1.0) {
            throw new IllegalArgumentException("confidence must be between 0 and 1");
        }
    }

    public record BuildRequest(
            Map<String, String> userProfile,
            String centralTopic,
            List<String> hints,
            Map<String, String> stylePrefs
    ) {
        public BuildRequest {
            userProfile = Map.copyOf(userProfile == null ? Map.of() : userProfile);
            Objects.requireNonNull(centralTopic, "centralTopic cannot be null");
            if (centralTopic.isBlank()) {
                throw new IllegalArgumentException("centralTopic cannot be blank");
            }
            hints = List.copyOf(hints == null ? List.of() : hints);
            stylePrefs = Map.copyOf(stylePrefs == null ? Map.of() : stylePrefs);
        }
    }

    public record BuildQAPair(
            String question,
            String answer,
            Instant at
    ) {
        public BuildQAPair {
            Objects.requireNonNull(question, "question cannot be null");
            if (question.isBlank()) {
                throw new IllegalArgumentException("question cannot be blank");
            }
            Objects.requireNonNull(answer, "answer cannot be null");
            Objects.requireNonNull(at, "at cannot be null");
        }
    }
}
