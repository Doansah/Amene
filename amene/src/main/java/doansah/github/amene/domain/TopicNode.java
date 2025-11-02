package doansah.github.amene.domain;

import doansah.github.amene.domain.enums.DifficultyBand;
import doansah.github.amene.domain.enums.ImportanceBand;
import doansah.github.amene.domain.enums.TopicKind;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.Data;

@Data
@Entity
public class TopicNode {
        String id;
        String name;
        TopicKind nodeKind;
        String description;
        String goal;
        List<String> tags;
        DifficultyBand difficultyBand;
        ImportanceBand importanceBand;
        double breadthBias;
        double depthBias;
        List<String> featureFlags;
        Map<String, String> metadata;



}




