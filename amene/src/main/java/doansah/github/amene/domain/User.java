package doansah.github.amene.domain;

//import javax.persistence.Entity;

import lombok.Data;


//@Entity
@Data
public class User {
    private String name; 
    private String desiredTopic; // what this person actually wants to know
    private String desiredGoal; // why do they want this information?  
    private LearningPrefrence learningPrefrence;

    // learning preference gets pulled for PERSONA (a prompt contributor)

}