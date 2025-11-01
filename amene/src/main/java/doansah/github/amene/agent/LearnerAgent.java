package doansah.github.amene.agent;

import doansah.github.amene.analysis.GraphAnalysisService;
import doansah.github.amene.domain.GraphMetrics;
import doansah.github.amene.domain.KnowledgeMap;
import doansah.github.amene.planning.PlanningService;
import doansah.github.amene.planning.Recommendation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LearnerAgent {

    private final GraphAnalysisService graphAnalysisService;
    private final PlanningService planningService;

    public LearnerAgent(GraphAnalysisService graphAnalysisService, PlanningService planningService) {
        this.graphAnalysisService = Objects.requireNonNull(graphAnalysisService);
        this.planningService = Objects.requireNonNull(planningService);
    }

    public PlanOutcome buildPlan(KnowledgeMap knowledgeMap) {
        Objects.requireNonNull(knowledgeMap, "knowledgeMap cannot be null");
        GraphMetrics metrics = graphAnalysisService.analyze(knowledgeMap);
        List<Recommendation> recommendations = planningService.plan(knowledgeMap, metrics);
        return new PlanOutcome(metrics, recommendations);
    }
}
