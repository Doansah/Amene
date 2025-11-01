package doansah.github.amene.domain;

import doansah.github.amene.domain.enums.DifficultyBand;
import doansah.github.amene.domain.enums.PracticeType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PracticeItemTest {

    @Test
    void allowsOptionalSolutionAndDefensiveCopies() {
        List<String> secondary = new ArrayList<>(List.of("topic-2"));
        PracticeItem item = new PracticeItem(
                "practice-1",
                PracticeType.MCQ,
                "topic-1",
                secondary,
                DifficultyBand.BASIC,
                "What is HTTP?",
                "Hypertext Transfer Protocol",
                List.of("misconception-1"),
                Map.of()
        );

        secondary.add("topic-3");
        assertEquals(List.of("topic-2"), item.secondaryTopicIds());
        assertEquals("Hypertext Transfer Protocol", item.canonicalSolution());
    }

    @Test
    void rejectsBlankId() {
        assertThrows(IllegalArgumentException.class, () -> new PracticeItem(
                " ",
                PracticeType.MCQ,
                "topic-1",
                List.of(),
                DifficultyBand.BASIC,
                "Prompt",
                null,
                List.of(),
                Map.of()
        ));
    }
}
