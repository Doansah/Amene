package doansah.github.amene.domain;

import doansah.github.amene.domain.enums.RelationType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RelationEdgeTest {

    @Test
    void copiesEvidenceAndDefaultsNotes() {
        List<String> evidence = List.of("source1");
        RelationEdge edge = new RelationEdge(
                "a",
                "b",
                RelationType.REQUIRES,
                5.0,
                7.5,
                null,
                null,
                evidence
        );

        assertEquals(List.of("source1"), edge.evidence());
        assertEquals("", edge.notes());
    }

    @Test
    void rejectsInvalidImportance() {
        assertThrows(IllegalArgumentException.class, () -> new RelationEdge(
                "a",
                "b",
                RelationType.REQUIRES,
                11.0,
                5.0,
                null,
                "",
                List.of()
        ));
    }
}
