package doansah.github.amene.planning;

import doansah.github.amene.domain.GraphMetrics;
import doansah.github.amene.domain.KnowledgeMap;
import doansah.github.amene.domain.PracticeItem;
import doansah.github.amene.domain.RelationEdge;
import doansah.github.amene.domain.TopicNode;
import doansah.github.amene.domain.enums.DifficultyBand;
import doansah.github.amene.domain.enums.ImportanceBand;
import doansah.github.amene.domain.enums.PracticeType;
import doansah.github.amene.domain.enums.RelationType;
import doansah.github.amene.domain.enums.TopicKind;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlanningServiceTest {

    private final PlanningService service = new PlanningService();

    @Test
    void choosesBreadthFirstPrimerWhenBreadthDominates() {
        KnowledgeMap map = knowledgeMap();
        GraphMetrics metrics = new GraphMetrics(
                2,
                1.2,
                0.5,
                Map.of("central", 1.0),
                40.0,
                0.2,
                Map.of("major", 8.0),
                0.8,
                0.2
        );

        List<Recommendation> recommendations = service.plan(map, metrics);

        assertEquals(1, recommendations.size());
        Recommendation rec = recommendations.get(0);
        assertEquals(PlanStyle.BREADTH_FIRST_PRIMER, rec.planStyle());
        assertEquals("central", rec.steps().get(0).topicIds().get(0));
        assertTrue(rec.steps().stream().anyMatch(step -> step.topicIds().contains("major")));
    }

    @Test
    void choosesDepthSpineWhenDepthDominates() {
        KnowledgeMap map = knowledgeMap();
        GraphMetrics metrics = new GraphMetrics(
                3,
                0.8,
                0.2,
                Map.of("central", 1.0),
                30.0,
                0.15,
                Map.of("major", 8.0),
                0.2,
                0.8
        );

        Recommendation rec = service.plan(map, metrics).get(0);
        assertEquals(PlanStyle.DEPTH_SPINE, rec.planStyle());
        assertEquals(3, rec.steps().size());
        assertEquals("central", rec.steps().get(rec.steps().size() - 1).topicIds().get(0));
    }

    @Test
    void buildsHybridWhenScoresAreClose() {
        KnowledgeMap map = knowledgeMap();
        GraphMetrics metrics = new GraphMetrics(
                3,
                1.0,
                0.3,
                Map.of("central", 1.0),
                30.0,
                0.15,
                Map.of("major", 8.0),
                0.55,
                0.6
        );

        Recommendation rec = service.plan(map, metrics).get(0);
        assertEquals(PlanStyle.HYBRID_LAYERED, rec.planStyle());
        assertTrue(rec.steps().stream().anyMatch(step -> step.topicIds().contains("major")));
        assertTrue(rec.steps().stream().anyMatch(step -> step.title().contains("spine")));
    }

    private KnowledgeMap knowledgeMap() {
        TopicNode central = topic("central", TopicKind.CENTRAL, 0.5, 0.5);
        TopicNode major = topic("major", TopicKind.MAJOR, 0.4, 0.6);
        TopicNode basic = topic("basic", TopicKind.BASIC, 0.3, 0.7);

        List<RelationEdge> edges = List.of(
                new RelationEdge("major", "central", RelationType.REQUIRES, 6, 5, null, "", List.of()),
                new RelationEdge("basic", "major", RelationType.REQUIRES, 5, 5, null, "", List.of())
        );

        List<PracticeItem> practice = List.of(
                practice("central", "p1"),
                practice("major", "p2"),
                practice("basic", "p3")
        );

        return new KnowledgeMap(
                "central",
                List.of(central, major, basic),
                edges,
                practice,
                Map.of("src", "demo"),
                Instant.parse("2024-02-02T00:00:00Z")
        );
    }

    private TopicNode topic(String id, TopicKind kind, double breadth, double depth) {
        return new TopicNode(
                id,
                id,
                kind,
                "desc",
                "goal",
                List.of(),
                DifficultyBand.BASIC,
                ImportanceBand.CORE,
                breadth,
                depth,
                List.of(),
                Map.of()
        );
    }

    private PracticeItem practice(String topicId, String id) {
        return new PracticeItem(
                id,
                PracticeType.MCQ,
                topicId,
                List.of(),
                DifficultyBand.BASIC,
                "Prompt",
                null,
                List.of(),
                Map.of()
        );
    }
}
