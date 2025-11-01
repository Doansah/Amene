package doansah.github.amene.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import doansah.github.amene.agent.LearnerAgent;
import doansah.github.amene.agent.PlanOutcome;
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
import doansah.github.amene.planning.PlanStep;
import doansah.github.amene.planning.PlanStyle;
import doansah.github.amene.planning.Recommendation;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LearningController.class)
class LearningControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LearnerAgent learnerAgent;

    @Test
    void returnsPlanResponse() throws Exception {
        KnowledgeMap request = knowledgeMap();
        GraphMetrics metrics = new GraphMetrics(
                2,
                1.1,
                0.4,
                Map.of("central", 1.0),
                40.0,
                0.2,
                Map.of("major", 8.0),
                0.7,
                0.3
        );
        Recommendation recommendation = new Recommendation(
                PlanStyle.BREADTH_FIRST_PRIMER,
                "summary",
                List.of(new PlanStep("Step", "Do it", List.of("central"), List.of("practice")))
        );
        when(learnerAgent.buildPlan(ArgumentMatchers.any())).thenReturn(new PlanOutcome(metrics, List.of(recommendation)));

        mockMvc.perform(post("/api/plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.metrics.depth").value(2))
                .andExpect(jsonPath("$.recommendations[0].planStyle").value("BREADTH_FIRST_PRIMER"));
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
                practice("central", "p1")
        );
        return new KnowledgeMap(
                "central",
                List.of(central, major, basic),
                edges,
                practice,
                Map.of("src", "demo"),
                Instant.parse("2024-04-04T00:00:00Z")
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
