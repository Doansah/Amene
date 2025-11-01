package doansah.github.amene.analysis;

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

class GraphAnalysisServiceTest {

    private final GraphAnalysisService service = new GraphAnalysisService();

    @Test
    void computesGraphMetricsFromKnowledgeMap() {
        TopicNode central = topic("central", TopicKind.CENTRAL, 0.6, 0.4);
        TopicNode major = topic("major", TopicKind.MAJOR, 0.2, 0.8);
        TopicNode basicA = topic("basic-a", TopicKind.BASIC, 0.3, 0.7);
        TopicNode basicB = topic("basic-b", TopicKind.BASIC, 0.4, 0.6);
        TopicNode basicCommon = topic("basic-common", TopicKind.BASIC, 0.5, 0.5);

        List<RelationEdge> edges = List.of(
                edge("major", "central", 8, 7),
                edge("basic-a", "major", 6, 6),
                edge("basic-b", "major", 5, 5),
                edge("basic-common", "basic-a", 4, 4),
                edge("basic-common", "basic-b", 4, 4)
        );

        KnowledgeMap map = new KnowledgeMap(
                "central",
                List.of(central, major, basicA, basicB, basicCommon),
                edges,
                List.of(new PracticeItem(
                        "practice-1",
                        PracticeType.MCQ,
                        "central",
                        List.of("major"),
                        DifficultyBand.BASIC,
                        "Prompt",
                        null,
                        List.of(),
                        Map.of()
                )),
                Map.of("source", "demo"),
                Instant.parse("2024-01-01T00:00:00Z")
        );

        GraphMetrics metrics = service.analyze(map);

        assertEquals(3, metrics.depth());
        assertEquals(1.25, metrics.avgBranching(), 1e-6);
        assertEquals(0.4, metrics.overlapRatio(), 1e-6);
        assertEquals(149.0, metrics.edgeWeightSum(), 1e-6);
        assertEquals(0.25, metrics.density(), 1e-6);
        assertEquals(11.0, metrics.majorCoverageNeed().get("major"), 1e-6);
        assertTrue(metrics.centrality().containsKey("central"));
        assertTrue(metrics.breadthScore() >= 0.0 && metrics.breadthScore() <= 1.0);
        assertTrue(metrics.depthScore() >= 0.0 && metrics.depthScore() <= 1.0);
        assertTrue(metrics.depthScore() > metrics.breadthScore());
    }

    private TopicNode topic(String id, TopicKind kind, double breadthBias, double depthBias) {
        return new TopicNode(
                id,
                id,
                kind,
                "desc",
                "goal",
                List.of(),
                DifficultyBand.BASIC,
                ImportanceBand.CORE,
                breadthBias,
                depthBias,
                List.of(),
                Map.of()
        );
    }

    private RelationEdge edge(String source, String target, double importance, double coverage) {
        return new RelationEdge(
                source,
                target,
                RelationType.REQUIRES,
                importance,
                coverage,
                null,
                "",
                List.of()
        );
    }
}
