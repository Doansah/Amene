package doansah.github.amene.domain;

import doansah.github.amene.domain.enums.DifficultyBand;
import doansah.github.amene.domain.enums.ImportanceBand;
import doansah.github.amene.domain.enums.TopicKind;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TopicNodeTest {

    @Test
    void createsDefensiveCopiesAndNormalizes() {
        List<String> tags = new ArrayList<>(List.of("networking"));
        List<String> flags = new ArrayList<>(List.of("long_context"));
        Map<String, String> metadata = Map.of("source", "llm");

        TopicNode node = new TopicNode(
                "topic-1",
                "Client-Server",
                TopicKind.MAJOR,
                "Explains client/server",
                "Supports web dev",
                tags,
                DifficultyBand.BASIC,
                ImportanceBand.CORE,
                0.3,
                0.7,
                flags,
                metadata
        );

        tags.add("extra");
        flags.clear();

        assertEquals(List.of("networking"), node.tags());
        assertEquals(List.of("long_context"), node.featureFlags());
        assertEquals("llm", node.metadata().get("source"));
    }

    @Test
    void rejectsBiasOutsideRange() {
        assertThrows(IllegalArgumentException.class, () -> new TopicNode(
                "topic-1",
                "Client-Server",
                TopicKind.MAJOR,
                "Explains client/server",
                "Supports web dev",
                List.of(),
                DifficultyBand.BASIC,
                ImportanceBand.CORE,
                -0.1,
                0.5,
                List.of(),
                Map.of()
        ));
    }
}
