package doansah.github.amene.analysis;

import doansah.github.amene.domain.GraphMetrics;
import doansah.github.amene.domain.KnowledgeMap;
import doansah.github.amene.domain.RelationEdge;
import doansah.github.amene.domain.TopicNode;
import doansah.github.amene.domain.enums.RelationType;
import doansah.github.amene.domain.enums.TopicKind;
import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.BetweennessCentrality;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GraphAnalysisService {

    public GraphMetrics analyze(KnowledgeMap knowledgeMap) {
        Objects.requireNonNull(knowledgeMap, "knowledgeMap cannot be null");

        Map<String, TopicNode> topicsById = knowledgeMap.topics().stream()
                .collect(Collectors.toMap(TopicNode::id, Function.identity()));

        DirectedWeightedMultigraph<String, DefaultWeightedEdge> graph =
                new DirectedWeightedMultigraph<>(DefaultWeightedEdge.class);

        knowledgeMap.topics().forEach(topic -> graph.addVertex(topic.id()));

        double edgeWeightSum = 0.0;
        for (RelationEdge edge : knowledgeMap.edges()) {
            if (!graph.containsVertex(edge.sourceId()) || !graph.containsVertex(edge.targetId())) {
                continue;
            }
            DefaultWeightedEdge graphEdge = graph.addEdge(edge.sourceId(), edge.targetId());
            if (graphEdge != null) {
                double weight = edge.importance() * edge.coverage();
                graph.setEdgeWeight(graphEdge, weight);
                edgeWeightSum += weight;
            }
        }

        Map<String, List<String>> prereqMap = knowledgeMap.edges().stream()
                .filter(edge -> edge.relationType() == RelationType.REQUIRES)
                .collect(Collectors.groupingBy(RelationEdge::targetId,
                        Collectors.mapping(RelationEdge::sourceId, Collectors.toList())));

        int depth = computeDepth(knowledgeMap.centralTopicId(), prereqMap, new HashMap<>(), new HashSet<>());
        double avgBranching = computeAverageBranching(knowledgeMap.edges());
        double overlapRatio = computeOverlapRatio(prereqMap);
        Map<String, Double> centrality = computeBetweenness(graph);
        double density = computeDensity(graph);
        Map<String, Double> majorCoverageNeed = computeMajorCoverage(knowledgeMap, topicsById);
        double breadthScore = computeBreadthScore(avgBranching, overlapRatio, knowledgeMap.topics());
        double depthScore = computeDepthScore(depth, overlapRatio, knowledgeMap.topics());

        return new GraphMetrics(
                depth,
                avgBranching,
                overlapRatio,
                centrality,
                edgeWeightSum,
                density,
                majorCoverageNeed,
                breadthScore,
                depthScore
        );
    }

    private int computeDepth(String nodeId,
                             Map<String, List<String>> prereqMap,
                             Map<String, Integer> memo,
                             Set<String> visiting) {
        if (nodeId == null) {
            return 0;
        }
        if (memo.containsKey(nodeId)) {
            return memo.get(nodeId);
        }
        if (!visiting.add(nodeId)) {
            return 0; // cycle guard
        }
        List<String> prereqs = prereqMap.getOrDefault(nodeId, List.of());
        int depth = prereqs.stream()
                .map(prereq -> 1 + computeDepth(prereq, prereqMap, memo, visiting))
                .max(Integer::compareTo)
                .orElse(0);
        visiting.remove(nodeId);
        memo.put(nodeId, depth);
        return depth;
    }

    private double computeAverageBranching(List<RelationEdge> edges) {
        Map<String, Long> counts = edges.stream()
                .filter(edge -> edge.relationType() == RelationType.REQUIRES)
                .collect(Collectors.groupingBy(RelationEdge::sourceId, Collectors.counting()));
        return counts.values().stream()
                .mapToDouble(Long::doubleValue)
                .average()
                .orElse(0.0);
    }

    private double computeOverlapRatio(Map<String, List<String>> prereqMap) {
        if (prereqMap.isEmpty()) {
            return 0.0;
        }
        Map<String, Integer> frequency = new HashMap<>();
        int total = 0;
        for (List<String> prereqs : prereqMap.values()) {
            for (String prereq : prereqs) {
                frequency.merge(prereq, 1, Integer::sum);
                total++;
            }
        }
        if (total == 0) {
            return 0.0;
        }
        int shared = frequency.values().stream()
                .filter(count -> count > 1)
                .mapToInt(Integer::intValue)
                .sum();
        return clamp((double) shared / total);
    }

    private Map<String, Double> computeBetweenness(Graph<String, DefaultWeightedEdge> graph) {
        if (graph.vertexSet().isEmpty()) {
            return Map.of();
        }
        BetweennessCentrality<String, DefaultWeightedEdge> centrality =
                new BetweennessCentrality<>(graph, true, false);
        return centrality.getScores();
    }

    private double computeDensity(Graph<String, DefaultWeightedEdge> graph) {
        int vertices = graph.vertexSet().size();
        if (vertices <= 1) {
            return 0.0;
        }
        int edges = graph.edgeSet().size();
        return (double) edges / (vertices * (vertices - 1));
    }

    private Map<String, Double> computeMajorCoverage(KnowledgeMap map, Map<String, TopicNode> topicsById) {
        Map<String, Double> coverage = new HashMap<>();
        for (RelationEdge edge : map.edges()) {
            TopicNode target = topicsById.get(edge.targetId());
            if (target != null && target.nodeKind() == TopicKind.MAJOR && edge.relationType() == RelationType.REQUIRES) {
                coverage.merge(target.id(), edge.coverage(), Double::sum);
            }
        }
        return Map.copyOf(coverage);
    }

    private double computeBreadthScore(double avgBranching, double overlapRatio, List<TopicNode> topics) {
        double bias = topics.isEmpty()
                ? 0.0
                : topics.stream().mapToDouble(TopicNode::breadthBias).average().orElse(0.0);
        double branchingFactor = Math.min(1.0, avgBranching / 5.0);
        return clamp((branchingFactor + overlapRatio + bias) / 3.0);
    }

    private double computeDepthScore(int depth, double overlapRatio, List<TopicNode> topics) {
        double bias = topics.isEmpty()
                ? 0.0
                : topics.stream().mapToDouble(TopicNode::depthBias).average().orElse(0.0);
        double depthFactor = Math.min(1.0, depth / 6.0);
        double focusFactor = 1.0 - overlapRatio;
        return clamp((depthFactor + focusFactor + bias) / 3.0);
    }

    private double clamp(double value) {
        if (Double.isNaN(value)) {
            return 0.0;
        }
        return Math.max(0.0, Math.min(1.0, value));
    }
}
