package doansah.github.amene.agent;

import doansah.github.amene.analysis.GraphAnalysisService;
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
import doansah.github.amene.planning.PlanningService;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LearnerAgentTest {

    private final GraphAnalysisService analysisService = new GraphAnalysisService();
    private final PlanningService planningService = new PlanningService();
    private final LearnerAgent agent = new LearnerAgent(analysisService, planningService);

    @Test
    void orchestratesAnalysisAndPlanning() {
        KnowledgeMap map = buildKnowledgeMap();

        PlanOutcome outcome = agent.buildPlan(map);

        GraphMetrics metrics = outcome.metrics();
        assertTrue(metrics.depth() > 0);
        assertFalse(outcome.recommendations().isEmpty());
        var steps = outcome.recommendations().get(0).steps();
        assertEquals(map.centralTopicId(), steps.get(steps.size() - 1).topicIds().get(steps.get(steps.size() - 1).topicIds().size() - 1));
    }

    private KnowledgeMap buildKnowledgeMap() {
        TopicNode central = topic("central", TopicKind.CENTRAL, 0.5, 0.5);
        TopicNode major = topic("major", TopicKind.MAJOR, 0.4, 0.6);
        TopicNode basic = topic("basic", TopicKind.BASIC, 0.3, 0.7);

        List<RelationEdge> edges = List.of(
                new RelationEdge("major", "central", RelationType.REQUIRES, 6, 5, null, "", List.of()),
                new RelationEdge("basic", "major", RelationType.REQUIRES, 5, 5, null, "", List.of())
        );

        List<PracticeItem> practice = List.of(
                practice("central", "p1"),
                practice("major", "p2")
        );

        return new KnowledgeMap(
                "central",
                List.of(central, major, basic),
                edges,
                practice,
                Map.of("src", "demo"),
                Instant.parse("2024-03-03T00:00:00Z")
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
