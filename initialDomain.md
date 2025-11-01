TopicNode

What is it?
A node in the knowledge graph representing a learnable concept. Central = user goal. Major = key idea that may unlock extra features. Basic = everything else.

Fields
	•	id – unique id
	•	name – short title (e.g., “Client–Server Model”)
	•	nodeKind – CENTRAL | MAJOR | BASIC
	•	description – brief summary
	•	goal – why this node matters (CENTRAL = user’s learning goal; others = “supports CENTRAL”)
	•	tags – string[]
	•	difficultyBand – INTRO | BASIC | INTERMEDIATE | ADVANCED
	•	importanceBand – NICE_TO_HAVE | USEFUL | CORE | CRITICAL
	•	breadthBias – 0–1 (benefits from broad context first)
	•	depthBias – 0–1 (benefits from drilling a chain)
	•	featureFlags – string[] (features unlocked when MAJOR is covered; e.g., use_analogies, long_context)
	•	metadata – kv (links, sources)

⸻

RelationEdge

What is it?
A directed edge between two TopicNodes describing how they relate and how much is needed.

Fields
	•	sourceId – upstream TopicNode id (u)
	•	targetId – downstream TopicNode id (v)
	•	relationType – REQUIRES | PART_OF | PRECEDES | ANALOGOUS_TO | CONTRASTS_WITH
	•	importance – 0–10 (how essential u is for learning v)
	•	coverage – 0–10 (how much of u is needed for v)
	•	andGroup – string | null (same id = AND; different ids across same parent = OR)
	•	notes – short rationale
	•	evidence – string[] (citations/snippets justifying the edge)

⸻

PracticeItem

What is it?
An atomic drill/check aligned to topics or edges.

Fields
	•	id – unique id
	•	type – MCQ | SHORT_ANSWER | DIAGRAM | PARSONS | INTERACTIVE
	•	primaryTopicId – main node tested
	•	secondaryTopicIds – related nodes (optional)
	•	difficultyBand – matches TopicNode band
	•	prompt – the task/question
	•	canonicalSolution – optional model answer
	•	targets – ids of misconceptions or edge ids probed
	•	metadata – kv (source, author, etc.)

⸻

KnowledgeMap

What is it?
A snapshot of the generated/cleaned graph used to plan learning.

Fields
	•	centralTopicId – the CENTRAL node
	•	topics – TopicNode[]
	•	edges – RelationEdge[]
	•	practiceBank – PracticeItem[] (optional)
	•	sources – kv of provenance (id → url/doc-id)
	•	createdISO – timestamp

⸻

GraphMetrics

What is it?
Quantitative structure signals to steer breadth vs depth and pruning.

Fields
	•	depth – longest REQUIRES chain to CENTRAL
	•	avgBranching – mean out-degree on REQUIRES
	•	overlapRatio – shared prereqs among siblings (0–1)
	•	centrality – map(topicId → betweenness score)
	•	edgeWeightSum – Σ(importance × coverage) over goal subgraph
	•	density – |E| / (|V|·(|V|−1))
	•	majorCoverageNeed – map(majorId → Σ coverage to unlock)
	•	breadthScore – heuristic from structure + node biases
	•	depthScore – heuristic from structure + node biases

⸻

CleaningRule

What is it?
A declarative rule for collapsing/merging nodes and pruning edges to simplify the graph.

Fields
	•	id – rule id
	•	name – label
	•	predicate – boolean expression over node/edge fields (e.g., edge.relationType=='PART_OF' && edge.importance<4)
	•	action – DROP_NODE | DROP_EDGE | COLLAPSE_INTO_PARENT | MERGE_SIBLINGS
	•	params – kv (e.g., thresholds)

⸻

BuildArtifacts

What is it?
Trace of how the graph was LLM-built (“grade the landscape”) to keep it auditable.

Fields
	•	buildRequest – inputs used to generate the map
	•	qaPairs – list of (question, answer, time) the LLM produced during grading
	•	confidence – 0–1 overall build confidence

BuildRequest (embedded)
	•	userProfile – lightweight profile/preferences
	•	centralTopic – user’s stated goal
	•	hints – scope hints from user
	•	stylePrefs – e.g., prefers analogies, concise steps

BuildQAPair (embedded)
	•	question – generated prompt
	•	answer – model’s response
	•	atISO – timestamp