package com.corkili.learningserver.scorm.sn.model.tracking;

import com.corkili.learningserver.scorm.sn.model.datatype.DecimalWithRange;
import com.corkili.learningserver.scorm.sn.model.definition.ObjectiveDescription;
import com.corkili.learningserver.scorm.sn.model.definition.ObjectiveMap;
import com.corkili.learningserver.scorm.sn.model.global.GlobalObjectiveDescription;

/**
 * For each attempt on an activity, a learner gets one set of objective progress information for
 * each objective associated with the activity.
 */
public class ObjectiveProgressInformation {

    private final Object context;

    private boolean objectiveProgressStatus;
    private boolean objectiveSatisfiedStatus;

    private boolean objectiveMeasureStatus;
    private final DecimalWithRange objectiveNormalizedMeasure;

    private boolean objectiveCompletionProgressStatus;
    private boolean objectiveCompletionStatus;

    private boolean objectiveCompletionAmountStatus;
    private final DecimalWithRange objectiveCompletionAmount;

    public ObjectiveProgressInformation(Object context) {
        this.context = context;

        objectiveProgressStatus = false;
        objectiveSatisfiedStatus = false;

        objectiveMeasureStatus = false;
        objectiveNormalizedMeasure = new DecimalWithRange(0, -1, 1, 4);

        objectiveCompletionStatus = false;

        objectiveCompletionAmountStatus = false;
        objectiveCompletionAmount = new DecimalWithRange(0, 0, 1, 4);
    }

    public void reinit() {
        objectiveProgressStatus = false;
        objectiveSatisfiedStatus = false;

        objectiveMeasureStatus = false;
        objectiveNormalizedMeasure.setValue(0);

        objectiveCompletionStatus = false;

        objectiveCompletionAmountStatus = false;
        objectiveCompletionAmount.setValue(0);
    }

    public Object getContext() {
        return context;
    }

    public boolean isObjectiveProgressStatus() {
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isReadObjectiveSatisfiedStatus()) {
                    if (globalObjectiveDescription.getObjectiveProgressInformation().isObjectiveProgressStatus()) {
                        return globalObjectiveDescription.getObjectiveProgressInformation().isObjectiveProgressStatus();
                    } else {
                        return objectiveProgressStatus;
                    }
                }
            }
        }
        return objectiveProgressStatus;
    }

    public void setObjectiveProgressStatus(boolean objectiveProgressStatus) {
        this.objectiveProgressStatus = objectiveProgressStatus;
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isWriteObjectiveSatisfiedStatus()) {
                    globalObjectiveDescription.getObjectiveProgressInformation().setObjectiveProgressStatus(objectiveProgressStatus);
                }
            }
        }
    }

    public boolean isObjectiveSatisfiedStatus() {
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isReadObjectiveSatisfiedStatus()) {
                    if (globalObjectiveDescription.getObjectiveProgressInformation().isObjectiveProgressStatus()) {
                        return globalObjectiveDescription.getObjectiveProgressInformation().isObjectiveSatisfiedStatus();
                    } else {
                        return objectiveSatisfiedStatus;
                    }
                }
            }
        }
        return objectiveSatisfiedStatus;
    }

    public void setObjectiveSatisfiedStatus(boolean objectiveSatisfiedStatus) {
        this.objectiveSatisfiedStatus = objectiveSatisfiedStatus;
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isWriteObjectiveSatisfiedStatus()) {
                    globalObjectiveDescription.getObjectiveProgressInformation().setObjectiveSatisfiedStatus(objectiveSatisfiedStatus);
                }
            }
        }
    }

    public boolean isObjectiveMeasureStatus() {
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isReadObjectiveNormalizedMeasure()) {
                    if (globalObjectiveDescription.getObjectiveProgressInformation().isObjectiveMeasureStatus()) {
                        return globalObjectiveDescription.getObjectiveProgressInformation().isObjectiveMeasureStatus();
                    } else {
                        return objectiveMeasureStatus;
                    }
                }
            }
        }
        return objectiveMeasureStatus;
    }

    public void setObjectiveMeasureStatus(boolean objectiveMeasureStatus) {
        this.objectiveMeasureStatus = objectiveMeasureStatus;
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isWriteObjectiveNormalizedMeasure()) {
                    globalObjectiveDescription.getObjectiveProgressInformation().setObjectiveMeasureStatus(objectiveMeasureStatus);
                }
            }
        }
    }

    public DecimalWithRange getObjectiveNormalizedMeasure() {
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isReadObjectiveNormalizedMeasure()) {
                    if (globalObjectiveDescription.getObjectiveProgressInformation().isObjectiveMeasureStatus()) {
                        return globalObjectiveDescription.getObjectiveProgressInformation().getObjectiveNormalizedMeasure();
                    } else {
                        return objectiveNormalizedMeasure;
                    }
                }
            }
        }
        return objectiveNormalizedMeasure;
    }

    public void setObjectiveNormalizedMeasure(double objectiveNormalizedMeasure) {
        this.objectiveNormalizedMeasure.setValue(objectiveNormalizedMeasure);
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isWriteObjectiveNormalizedMeasure()) {
                    globalObjectiveDescription.getObjectiveProgressInformation().setObjectiveNormalizedMeasure(objectiveNormalizedMeasure);
                }
            }
        }
    }

    public boolean isObjectiveCompletionProgressStatus() {
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isReadCompletionStatus()) {
                    if (globalObjectiveDescription.getObjectiveProgressInformation().isObjectiveCompletionProgressStatus()) {
                        return globalObjectiveDescription.getObjectiveProgressInformation().isObjectiveCompletionProgressStatus();
                    } else {
                        return objectiveCompletionProgressStatus;
                    }
                }
            }
        }
        return objectiveCompletionProgressStatus;
    }

    public void setObjectiveCompletionProgressStatus(boolean objectiveCompletionProgressStatus) {
        this.objectiveCompletionProgressStatus = objectiveCompletionProgressStatus;
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isWriteCompletionStatus()) {
                    globalObjectiveDescription.getObjectiveProgressInformation().setObjectiveCompletionProgressStatus(objectiveCompletionProgressStatus);
                }
            }
        }
    }

    public boolean isObjectiveCompletionStatus() {
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isReadCompletionStatus()) {
                    if (globalObjectiveDescription.getObjectiveProgressInformation().isObjectiveCompletionProgressStatus()) {
                        return globalObjectiveDescription.getObjectiveProgressInformation().isObjectiveCompletionStatus();
                    } else {
                        return objectiveSatisfiedStatus;
                    }
                }
            }
        }
        return objectiveCompletionStatus;
    }

    public void setObjectiveCompletionStatus(boolean objectiveCompletionStatus) {
        this.objectiveCompletionStatus = objectiveCompletionStatus;
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isWriteCompletionStatus()) {
                    globalObjectiveDescription.getObjectiveProgressInformation().setObjectiveCompletionStatus(objectiveCompletionStatus);
                }
            }
        }
    }

    public boolean isObjectiveCompletionAmountStatus() {
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isReadProgressMeasure()) {
                    if (globalObjectiveDescription.getObjectiveProgressInformation().isObjectiveCompletionAmountStatus()) {
                        return globalObjectiveDescription.getObjectiveProgressInformation().isObjectiveCompletionAmountStatus();
                    } else {
                        return objectiveCompletionAmountStatus;
                    }
                }
            }
        }
        return objectiveCompletionAmountStatus;
    }

    public void setObjectiveCompletionAmountStatus(boolean objectiveCompletionAmountStatus) {
        this.objectiveCompletionAmountStatus = objectiveCompletionAmountStatus;
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isWriteProgressMeasure()) {
                    globalObjectiveDescription.getObjectiveProgressInformation().setObjectiveCompletionAmountStatus(objectiveCompletionAmountStatus);
                }
            }
        }
    }

    public DecimalWithRange getObjectiveCompletionAmount() {
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isReadProgressMeasure()) {
                    if (globalObjectiveDescription.getObjectiveProgressInformation().isObjectiveCompletionAmountStatus()) {
                        return globalObjectiveDescription.getObjectiveProgressInformation().getObjectiveCompletionAmount();
                    } else {
                        return objectiveCompletionAmount;
                    }
                }
            }
        }
        return objectiveCompletionAmount;
    }

    public void setObjectiveCompletionAmount(double objectiveCompletionAmount) {
        this.objectiveCompletionAmount.setValue(objectiveCompletionAmount);
        if (context instanceof ObjectiveDescription) {
            ObjectiveDescription objectiveDescription = (ObjectiveDescription) context;
            for (ObjectiveMap objectiveMap : objectiveDescription.getObjectiveMaps()) {
                GlobalObjectiveDescription globalObjectiveDescription = objectiveDescription.getContext().getContext()
                        .findGlobalObjectiveDescription(objectiveMap.getTargetObjectiveID());
                if (objectiveMap.isWriteProgressMeasure()) {
                    globalObjectiveDescription.getObjectiveProgressInformation().setObjectiveCompletionAmount(objectiveCompletionAmount);
                }
            }
        }
    }
}
