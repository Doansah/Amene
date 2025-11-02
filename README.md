Before we get into the code, I want to lay out the intuition and the reason I’m building Amene (an Educator Agent).

⸻

1. Learning Is Difficult

I struggled for a while learning programming, and here are a few reasons why:
	1.	Coding is built upon a tower of abstractions
	2.	Beginners (like me) cannot discern what is and isn’t important
	3.	It’s extremely easy to fall into dependency rabbit holes
	4.	Beginners (like me) often don’t even know what they want to learn
	5.	Learning is built on testing and reforming knowledge

I’ve written about this in more depth here:
What Made Programming Hard for Me — Unknown Unknowns￼

⸻

2. The Goal of Amene

My goal is for Amene to help with such problems.
More specifically, the goal is to have a system that takes any field of knowledge as input and does the following:
	1.	Provides a practical plan for learning
	2.	Creates a Knowledge Map of the given topic

Here’s what I mean:

<img width="397" height="298" alt="image" src="https://github.com/user-attachments/assets/98697fcf-3169-41a0-a8ab-f93f06a76518" />  


I’m a great artist, I know!

In this case, the user wants to learn the Client-Server Model. This is the central node, and the other topics—Networking, Coding Basics, etc.—are related to it. To understand the Client-Server Model, an understanding of adjacent topics is relevant.

⸻

3. Foundational Intuitions Behind the Architecture

These are more philosophical than technical, so bear with me:

a) Knowledge Can Be Represented as a Graph
Nodes represent ideas themselves, and edges represent how they relate to one another.

Goofy Example:
“Dogs” (Node A) is related to “Cats” (Node B) because they are both animals — a relation of sameness.

⸻

4. Miscellaneous Ideas (That Are Cool but Hard to Prove — Still Implementing Anyway)

Given the structure of our knowledge graph (a set of nodes and edges), we can alter our learning style.

Consider the difference between learning a historical event versus a biological system:
	•	Learning about the History of Egypt requires a sequential lens.
	•	Learning about the Human Body demands a breadth of information.

Because the graph is represented as a data structure, we can analyze its shape to inform how we ought to learn the topic.

Depth-Heavy Graphs (high level count, etc.)
	•	Build a roadmap and enforce sequential learning

Breadth-Heavy Graphs
	•	Emphasize context and interrelations between ideas

⸻
