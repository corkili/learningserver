package com.corkili.learningserver.scorm.sn.model.global;

import com.corkili.learningserver.scorm.sn.model.tracking.ObjectiveProgressInformation;

public class GlobalObjectiveDescription {

//    private boolean objectiveSatisfiedStatus;
//    private boolean objectiveNormalizedMeasure;
//    private boolean rawScore;
//    private boolean minScore;
//    private boolean maxScore;
//    private boolean completionStatus;
//    private boolean progressMeasure;

    private final String objectiveID;
    private final ObjectiveProgressInformation objectiveProgressInformation;
//    private final AttemptProgressInformation attemptProgressInformation;

    public GlobalObjectiveDescription(String objectiveID) {
        this.objectiveID = objectiveID;
        this.objectiveProgressInformation = new ObjectiveProgressInformation(this);
//        this.attemptProgressInformation = new AttemptProgressInformation(this);
    }

    public String getObjectiveID() {
        return objectiveID;
    }

    public ObjectiveProgressInformation getObjectiveProgressInformation() {
        return objectiveProgressInformation;
    }

//    public AttemptProgressInformation getAttemptProgressInformation() {
//        return attemptProgressInformation;
//    }
}
