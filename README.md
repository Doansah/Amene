Before we get into the code I want to lay out the INTIUTION and the REASON I'm building Amene (an Educator Agent)  . 

1. Learning is Difficult

I struggled for a while learning programming, and here are a few reasons why:

1) Coding is built upon a 'tower of abstractions'
2) Beginners (Me) cannot dicsern what is and isn't important
3) It is extremely easy to go in to 'dependency rabbit holes'
4) Beginners (Me) do not know what they even want to learn
5) Learning is built on Testing and reforming knowledge.

I written about this in more depth at: 
https://medium.com/@dillondoa/what-made-programming-hard-for-me-unknown-unknowns-dc43b327855f

My goal is for Amene to help with such problems. 
More specifically, the goal is to have a system which takes any field of knowledge as input and does the following: 
1. Give a practical plan for learning 

Some intuitions of knowledge the architecture is built upon (there are more but these are the key ones): 
This is more philsophical than technical so bare with me: 

1. Knowledge can be represented as graph:

   Where nodes are ideas themselves, and edges are how they relate to each other.

   Goofy Example: Dogs (Node A) is related to Cats (Node B) in that they are BOTH animals.
   - A Relation of SAMENESS
     

Micellaneous Ideas I have that are cool, but cant prove (but I still will IMPLEMENT) : 

Given the structure of our Knowledge graph (remember the a set of nodes and edges) we can alter our learning style. 

Consider the difference between learning a historical event vs a biological system. 
Intuitively, we could say that History of Egypt events requires sequential lens, whereas learning the Human body demands a 'breadth' of information. 

Given that we've compressed our graph into a Data structure, we can analyse its structure to inform HOW we ought to learn the topic. 

'Depth Heavy' ie. ( High level count, etc) 
-  Build a road map, enforce sequential learning
'Breadth Heavy'
- Emphasise Context, and inter-relations between ideas.


And YES I am using AI for code assistance this a side project bro chill. 
