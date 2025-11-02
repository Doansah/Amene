package doansah.github.amene.agent;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;

import doansah.github.amene.domain.KnowledgeGraph;
import doansah.github.amene.domain.Topic;
import doansah.github.amene.domain.TopicNode;
import doansah.github.amene.domain.User;

/*
General Idea: 
Knowledge Graph Builder Agent: 
1) Receives Topic (and User Information)
2) Searches the web 
3) Builds Knowledge Graph

*/


/*
KnowledgeGraph Building should be a lot more granular. LLM should not 'one shot'

Currently:
source topic -> related topics -> knowledgeGraph 


*/

@Agent(name = "Knowledge-Graph-Builder", description = "Given a Source Topic build a Knowledge Graph")
public class KnowledgeGraphBuilderAgent {


    // this is gna be like the FINAL FUCTION, that simply lists topics that are related naive
    @Action(toolGroups = CoreToolGroups.WEB)
    public List<TopicNode> findAllRelatedTopicsToSource(User  learner, int topicCount {
        PromptRunner runner = new PromptRunner().usingLlm().createObject(
            "Given the topic " + learner.getDesiredTopic() + "List " + topicCount + " topics which are related"
            , TopicNode.ListOf.class);

    }

    @Action
    @AcheivesGoal(description = "Knowledge Graph built")
    public KnowledgeGraph generateKnoledgeGraph(List<TopicNode> topics) {
        PromptRunner runner = new PromptRunner().usingLlm().createObject("Given this list of topics" + topics.toString() + " Generate a Knowledge graph")
        
    }
}

