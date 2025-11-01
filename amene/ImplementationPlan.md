# Implementation Plan

## 1. Define the domain data structures
- Create immutable domain classes or records under `src/main/java/doansah/github/amene/domain/` for `TopicNode`, `RelationEdge`, `PracticeItem`, `KnowledgeMap`, `GraphMetrics`, `CleaningRule`, and `BuildArtifacts`, mirroring the fields specified in the domain documentation.
- Introduce supporting enums (`TopicKind`, `DifficultyBand`, `ImportanceBand`, `RelationType`, `PracticeType`, `CleaningAction`) under `domain/enums/`.
- Ensure constructors validate invariants (non-null IDs, bounded scores) and collections are defensively copied.
- Provide builders or factory methods where helpful and add unit tests under `src/test/java/doansah/github/amene/domain/`.

## 2. Implement graph metrics computation with JGraphT
- Add the JGraphT dependency to `pom.xml` if it is not already present.
- Implement `GraphAnalysisService` under `src/main/java/doansah/github/amene/analysis/` to convert a `KnowledgeMap` into a directed weighted graph with weights derived from importance and coverage.
- Compute depth, branching, betweenness centrality, overlap ratios, density, and longest weighted path, then populate a `GraphMetrics` instance.
- Write tests under `src/test/java/doansah/github/amene/analysis/` using fixture graphs to confirm metric calculations.

## 3. Design the learning-plan recommender
- Define a `Recommendation` model and plan-type enums based on the documented plan styles.
- Create `PlanningService` in `src/main/java/doansah/github/amene/planning/` that consumes `KnowledgeMap` and `GraphMetrics`, applies heuristics, and produces structured plans (e.g., Breadth-First Primer, Depth Spine).
- Supply helper builders that reference specific `TopicNode` IDs for generated sequences.
- Extend `LearnerAgent` to orchestrate domain loading, analysis, and planning; add tests under `src/test/java/doansah/github/amene/planning/`.

## 4. Provide serialization and integration scaffolding
- Configure Jackson in Spring Boot to serialize/deserialize the new domain classes.
- Add API layer components (DTOs, controller) under `src/main/java/doansah/github/amene/api/` to accept a `KnowledgeMap` and return recommendations.
- Wire services with Spring annotations and expose REST endpoints; seed example JSON in `src/main/resources/`.
- Implement integration tests (e.g., `@WebMvcTest`) to verify end-to-end behavior.
