package doansah.github.amene.domain;

import doansah.github.amene.domain.enums.DifficultyBand;
import doansah.github.amene.domain.enums.ImportanceBand;
import doansah.github.amene.domain.enums.TopicKind;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KnowledgeMapTest {

    @Test
    void createsUnmodifiableCollections() {
        TopicNode node = new TopicNode(
                "central",
                "Central",
                TopicKind.CENTRAL,
                "desc",
                "goal",
                List.of(),
                DifficultyBand.INTRO,
                ImportanceBand.CRITICAL,
                0.5,
                0.5,
                List.of(),
                Map.of()
        );
        RelationEdge edge = new RelationEdge(
                "central",
                "child",
                doansah.github.amene.domain.enums.RelationType.REQUIRES,
                5,
                5,
                null,
                "",
                List.of()
        );
        List<TopicNode> topics = new ArrayList<>(List.of(node));

        KnowledgeMap map = new KnowledgeMap(
                "central",
                topics,
                List.of(edge),
                null,
                Map.of("src", "demo"),
                Instant.parse("2023-01-01T00:00:00Z")
        );

        topics.add(node);
        assertEquals(1, map.topics().size());
        assertEquals("demo", map.sources().get("src"));
        assertTrue(map.practiceBank().isEmpty());
    }

    @Test
    void rejectsBlankCentralId() {
        assertThrows(IllegalArgumentException.class, () -> new KnowledgeMap(
                " ",
                List.of(),
                List.of(),
                List.of(),
                Map.of(),
                Instant.now()
        ));
    }
}
