package doansah.github.amene.domain;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GraphMetricsTest {

    @Test
    void copiesMaps() {
        GraphMetrics metrics = new GraphMetrics(
                3,
                2.5,
                0.4,
                Map.of("central", 1.0),
                42.0,
                0.3,
                Map.of("major", 5.0),
                0.6,
                0.4
        );

        assertEquals(1.0, metrics.centrality().get("central"));
        assertEquals(5.0, metrics.majorCoverageNeed().get("major"));
    }

    @Test
    void rejectsNegativeDepth() {
        assertThrows(IllegalArgumentException.class, () -> new GraphMetrics(
                -1,
                0.0,
                0.0,
                Map.of(),
                0.0,
                0.0,
                Map.of(),
                0.0,
                0.0
        ));
    }
}
