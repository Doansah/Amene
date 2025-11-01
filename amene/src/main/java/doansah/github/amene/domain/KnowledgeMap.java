package doansah.github.amene.domain;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record KnowledgeMap(
        String centralTopicId,
        List<TopicNode> topics,
        List<RelationEdge> edges,
        List<PracticeItem> practiceBank,
        Map<String, String> sources,
        Instant createdAt
) {
    public KnowledgeMap {
        Objects.requireNonNull(centralTopicId, "centralTopicId cannot be null");
        if (centralTopicId.isBlank()) {
            throw new IllegalArgumentException("centralTopicId cannot be blank");
        }
        Objects.requireNonNull(topics, "topics cannot be null");
        Objects.requireNonNull(edges, "edges cannot be null");
        Objects.requireNonNull(sources, "sources cannot be null");
        Objects.requireNonNull(createdAt, "createdAt cannot be null");
        topics = List.copyOf(topics);
        edges = List.copyOf(edges);
        practiceBank = List.copyOf(practiceBank == null ? List.of() : practiceBank);
        sources = Map.copyOf(sources);
    }
}
