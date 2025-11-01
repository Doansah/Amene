package doansah.github.amene.planning;

import doansah.github.amene.domain.GraphMetrics;
import doansah.github.amene.domain.KnowledgeMap;
import doansah.github.amene.domain.PracticeItem;
import doansah.github.amene.domain.RelationEdge;
import doansah.github.amene.domain.TopicNode;
import doansah.github.amene.domain.enums.ImportanceBand;
import doansah.github.amene.domain.enums.RelationType;
import doansah.github.amene.domain.enums.TopicKind;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PlanningService {

    public List<Recommendation> plan(KnowledgeMap knowledgeMap, GraphMetrics metrics) {
        Objects.requireNonNull(knowledgeMap, "knowledgeMap cannot be null");
        Objects.requireNonNull(metrics, "metrics cannot be null");
        if (knowledgeMap.topics().isEmpty()) {
            return List.of();
        }

        double breadth = metrics.breadthScore();
        double depth = metrics.depthScore();

        if (breadth > depth + 0.1) {
            return List.of(buildBreadthPlan(knowledgeMap));
        }
        if (depth > breadth + 0.1) {
            return List.of(buildDepthPlan(knowledgeMap));
        }
        return List.of(buildHybridPlan(knowledgeMap, metrics));
    }

    private Recommendation buildBreadthPlan(KnowledgeMap knowledgeMap) {
        Map<String, TopicNode> topicsById = indexTopics(knowledgeMap.topics());
        Map<String, List<PracticeItem>> practiceByTopic = indexPractices(knowledgeMap.practiceBank());

        List<TopicNode> majors = knowledgeMap.topics().stream()
                .filter(topic -> topic.nodeKind() == TopicKind.MAJOR)
                .sorted(Comparator
                        .comparing((TopicNode topic) -> importanceRank(topic.importanceBand()))
                        .thenComparing(TopicNode::name))
                .toList();

        List<PlanStep> steps = new ArrayList<>();
        TopicNode central = topicsById.get(knowledgeMap.centralTopicId());
        steps.add(new PlanStep(
                "Anchor the central goal",
                "Give a high-level preview of " + central.name() + " and why it matters.",
                List.of(central.id()),
                practiceIds(practiceByTopic.get(central.id()))
        ));

        for (TopicNode major : majors) {
            steps.add(new PlanStep(
                    "Survey " + major.name(),
                    "Introduce supporting idea " + major.name() + " before drilling deeper.",
                    List.of(major.id()),
                    practiceIds(practiceByTopic.get(major.id()))
            ));
        }

        return new Recommendation(
                PlanStyle.BREADTH_FIRST_PRIMER,
                "Lead with a broad tour of major concepts before focusing on depth.",
                steps
        );
    }

    private Recommendation buildDepthPlan(KnowledgeMap knowledgeMap) {
        Map<String, TopicNode> topicsById = indexTopics(knowledgeMap.topics());
        Map<String, List<String>> prereqMap = buildPrereqMap(knowledgeMap.edges());
        List<String> spine = findLongestChain(knowledgeMap.centralTopicId(), prereqMap, new HashMap<>());
        Map<String, List<PracticeItem>> practiceByTopic = indexPractices(knowledgeMap.practiceBank());

        List<PlanStep> steps = new ArrayList<>();
        for (int i = 0; i < spine.size(); i++) {
            String topicId = spine.get(i);
            TopicNode topic = topicsById.get(topicId);
            String title = i == spine.size() - 1
                    ? "Synthesize " + topic.name()
                    : "Master " + topic.name();
            String description = i == spine.size() - 1
                    ? "Connect prerequisites to achieve the central goal."
                    : "Ensure deep understanding before advancing.";
            steps.add(new PlanStep(
                    title,
                    description,
                    List.of(topic.id()),
                    practiceIds(practiceByTopic.get(topic.id()))
            ));
        }

        return new Recommendation(
                PlanStyle.DEPTH_SPINE,
                "Follow the critical prerequisite spine leading directly to the goal.",
                steps
        );
    }

    private Recommendation buildHybridPlan(KnowledgeMap knowledgeMap, GraphMetrics metrics) {
        Map<String, TopicNode> topicsById = indexTopics(knowledgeMap.topics());
        Map<String, List<PracticeItem>> practiceByTopic = indexPractices(knowledgeMap.practiceBank());
        Map<String, List<String>> prereqMap = buildPrereqMap(knowledgeMap.edges());
        List<String> spine = findLongestChain(knowledgeMap.centralTopicId(), prereqMap, new HashMap<>());

        TopicNode central = topicsById.get(knowledgeMap.centralTopicId());
        List<PlanStep> steps = new ArrayList<>();
        steps.add(new PlanStep(
                "Frame the journey",
                "Blend context and focus: explain why " + central.name() + " matters and how to navigate it.",
                List.of(central.id()),
                practiceIds(practiceByTopic.get(central.id()))
        ));

        List<TopicNode> majors = knowledgeMap.topics().stream()
                .filter(topic -> topic.nodeKind() == TopicKind.MAJOR)
                .sorted(Comparator
                        .comparing((TopicNode topic) -> importanceRank(topic.importanceBand()))
                        .thenComparing(TopicNode::name))
                .toList();
        if (!majors.isEmpty()) {
            List<String> majorIds = majors.stream().map(TopicNode::id).toList();
            steps.add(new PlanStep(
                    "Surface the major pillars",
                    String.format("Outline %d major supports before the deep dive.", majors.size()),
                    majorIds,
                    majorIds.stream()
                            .flatMap(id -> practiceIds(practiceByTopic.get(id)).stream())
                            .distinct()
                            .toList()
            ));
        }

        if (spine.size() > 1) {
            List<String> prereqIds = spine.subList(0, spine.size() - 1);
            steps.add(new PlanStep(
                    "Deepen along the spine",
                    String.format("Work through %d-step prerequisite chain highlighted by graph depth %.2f.",
                            prereqIds.size(), metrics.depth()),
                    prereqIds,
                    prereqIds.stream()
                            .flatMap(id -> practiceIds(practiceByTopic.get(id)).stream())
                            .distinct()
                            .toList()
            ));
        }

        return new Recommendation(
                PlanStyle.HYBRID_LAYERED,
                "Mix a quick breadth scan with a focused depth spine to balance learning preferences.",
                steps
        );
    }

    private Map<String, TopicNode> indexTopics(List<TopicNode> topics) {
        return topics.stream().collect(Collectors.toMap(TopicNode::id, Function.identity()));
    }

    private Map<String, List<PracticeItem>> indexPractices(List<PracticeItem> practiceItems) {
        if (practiceItems == null || practiceItems.isEmpty()) {
            return Map.of();
        }
        return practiceItems.stream()
                .collect(Collectors.groupingBy(PracticeItem::primaryTopicId, LinkedHashMap::new, Collectors.toList()));
    }

    private Map<String, List<String>> buildPrereqMap(List<RelationEdge> edges) {
        return edges.stream()
                .filter(edge -> edge.relationType() == RelationType.REQUIRES)
                .collect(Collectors.groupingBy(RelationEdge::targetId,
                        Collectors.mapping(RelationEdge::sourceId, Collectors.toList())));
    }

    private List<String> findLongestChain(String target,
                                          Map<String, List<String>> prereqMap,
                                          Map<String, List<String>> memo) {
        if (target == null) {
            return List.of();
        }
        if (memo.containsKey(target)) {
            return memo.get(target);
        }
        List<String> prereqs = prereqMap.getOrDefault(target, List.of());
        if (prereqs.isEmpty()) {
            List<String> base = List.of(target);
            memo.put(target, base);
            return base;
        }
        List<String> bestPath = null;
        for (String prereq : prereqs) {
            List<String> candidate = new ArrayList<>(findLongestChain(prereq, prereqMap, memo));
            candidate.add(target);
            if (bestPath == null || candidate.size() > bestPath.size()) {
                bestPath = candidate;
            }
        }
        memo.put(target, bestPath);
        return bestPath;
    }

    private List<String> practiceIds(List<PracticeItem> items) {
        if (items == null) {
            return List.of();
        }
        return items.stream().map(PracticeItem::id).limit(2).toList();
    }

    private int importanceRank(ImportanceBand band) {
        return switch (band) {
            case CRITICAL -> 0;
            case CORE -> 1;
            case USEFUL -> 2;
            case NICE_TO_HAVE -> 3;
        };
    }
}
