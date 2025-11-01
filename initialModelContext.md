🧩 Amene — Context for the Coding Agent

What the Project Is

You are helping build Amene, an Embabel-powered AI system that helps users understand topics—focusing on Eureka moments, not rote facts.
It models human learning through a knowledge graph of interconnected concepts (topics), analyzes that graph, and then generates learning recommendations (Breadth-first primers, Depth-spine lessons, contrasts, etc.).

⸻

Core Idea
	•	Understanding = connection of concepts.
	•	Every TopicNode represents a piece of knowledge (e.g., “Client–Server Model”).
	•	RelationEdges describe how concepts depend on, contrast with, or build upon each other.
	•	A KnowledgeMap (graph) is generated and cleaned from an LLM’s “graded landscape” of knowledge.
	•	Graph analysis (depth, branching, overlap, centrality) informs the learning plan.

⸻

Node Types
	•	Central Node: The user’s learning goal (e.g., “Learn Web Development”).
	•	Major Nodes: Key ideas supporting the central node, unlock richer teaching “features.”
	•	Basic Nodes: Foundational or contextual details.

⸻

Data Model (Simplified)
	•	TopicNode — id, name, nodeKind (CENTRAL|MAJOR|BASIC), description, goal, difficulty, importance, breadthBias, depthBias, featureFlags.
	•	RelationEdge — sourceId, targetId, relationType (REQUIRES|PART_OF|PRECEDES|CONTRASTS_WITH|ANALOGOUS_TO), importance (0–10), coverage (0–10).
	•	PracticeItem — small exercises/tests linked to topics.
	•	KnowledgeMap — the graph: topics[], edges[], practiceBank[].
	•	GraphMetrics — computed features: depth (D), branching (B), overlap, centrality, density, etc.
	•	Recommendation — structured output describing how to teach (BREADTH_FIRST_PRIMER, DEPTH_SPINE, etc.).

⸻

Analysis Logic

You use graph structure to guide teaching strategy:
	•	Compute Depth (D) = longest prerequisite chain.
	•	Compute Branching (B) = average out-degree near the central node.
	•	Compute Overlap = shared dependencies between sibling nodes.
	•	Compute Centrality = which topics unlock many others.
	•	Combine these to decide Breadth vs Depth:
	•	BreadthFirstPrimer → teach broad L1 overview before drilling down.
	•	DepthSpine → follow longest/most important chain deeply first.
	•	InterleaveSiblings / Contrast → when overlap or contrasts exist.

⸻

Implementation Stack
	•	Language: Java
	•	Graph Library: JGraphT￼ for analysis (prevents reinventing BFS/DFS logic).
	•	Vertices = TopicNode
	•	Edges = RelationEdge (weight = importance × coverage)
	•	Use JGraphT algorithms:
	•	TopologicalOrderIterator (depth)
	•	outDegreeOf (branching)
	•	BetweennessCentrality (leverage)
	•	AcyclicLongestPath (depth spine)

⸻

Agent’s Expected Responsibilities

When coding or extending the system, the agent should:
	1.	Respect the data model — use TopicNode, RelationEdge, etc., as building blocks.
	2.	Leverage JGraphT — for graph representation, traversal, and metrics.
	3.	Support analysis → recommendation flow:
	•	Input: KnowledgeMap
	•	Output: list of Recommendations (plan steps)
	4.	Avoid unnecessary complexity — prefer simplicity, minimal dependencies, clear code.
	5.	Preserve separation between:
	•	Domain model (data classes)
	•	Graph analysis (computations)
	•	Planning logic (recommendations)
	6.	Enable LLM-driven generation — LLM builds graph; your code analyzes and returns structured insights.

⸻

Example Flow
	1.	User asks: “I want to learn Web Development.”
	2.	LLM builds graph: Central node = “Web Development” → majors like “Client–Server Model”, “HTTP Basics”.
	3.	JGraphT analysis: finds D=4, B=3.5 → breadth-heavy.
	4.	Planner output: BREADTH_FIRST_PRIMER plan → teach HTTP, Networking, Coding Basics before deep dive.