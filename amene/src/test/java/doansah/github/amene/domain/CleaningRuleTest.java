package doansah.github.amene.domain;

import doansah.github.amene.domain.enums.CleaningAction;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CleaningRuleTest {

    @Test
    void defaultsOptionalParams() {
        CleaningRule rule = new CleaningRule(
                "rule-1",
                "Drop weak part-of",
                "edge.relationType=='PART_OF' && edge.importance<4",
                CleaningAction.DROP_EDGE,
                null
        );

        assertEquals(Map.of(), rule.params());
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new CleaningRule(
                "rule-1",
                " ",
                "predicate",
                CleaningAction.DROP_EDGE,
                Map.of()
        ));
    }
}
