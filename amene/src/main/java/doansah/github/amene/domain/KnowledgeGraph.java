package doansah.github.amene.domain;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import lombok.Data;


@Data
@Entity
public class KnowledgeGraph{ 
        String centralTopicId;
        List<TopicNode> topics;
        List<RelationEdge> edges;
        List<PracticeItem> practiceBank;
        Map<String, String> sources;
        Instant createdAt;

        @Tool(description = "Analyze the Structure of the Knowledge Map")
        public String analyzeKnowledgeMapStructure() {
            // JGRAPH T implementation for structure about map structure. 
 
        }
}
