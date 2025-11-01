package doansah.github.amene.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BuildArtifactsTest {

    @Test
    void enforcesConfidenceBounds() {
        BuildArtifacts.BuildRequest request = new BuildArtifacts.BuildRequest(
                Map.of("experience", "beginner"),
                "Learn Web Dev",
                List.of("frontend"),
                Map.of("tone", "encouraging")
        );
        BuildArtifacts.BuildQAPair qa = new BuildArtifacts.BuildQAPair(
                "What is HTTP?",
                "A protocol",
                Instant.parse("2023-01-01T00:00:00Z")
        );

        BuildArtifacts artifacts = new BuildArtifacts(request, List.of(qa), 0.8);
        assertEquals(1, artifacts.qaPairs().size());

        assertThrows(IllegalArgumentException.class, () -> new BuildArtifacts(request, List.of(), 1.5));
    }
}
