package doansah.github.amene.api;

import doansah.github.amene.agent.LearnerAgent;
import doansah.github.amene.agent.PlanOutcome;
import doansah.github.amene.domain.KnowledgeMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plan")
public class LearningController {

    private final LearnerAgent learnerAgent;

    public LearningController(LearnerAgent learnerAgent) {
        this.learnerAgent = learnerAgent;
    }

    @PostMapping
    public ResponseEntity<PlanResponse> buildPlan(@RequestBody KnowledgeMap knowledgeMap) {
        PlanOutcome outcome = learnerAgent.buildPlan(knowledgeMap);
        return ResponseEntity.ok(new PlanResponse(outcome.metrics(), outcome.recommendations()));
    }
}
