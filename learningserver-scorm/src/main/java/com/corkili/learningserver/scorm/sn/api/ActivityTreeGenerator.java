package com.corkili.learningserver.scorm.sn.api;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.scorm.cam.model.AdlseqMapInfo;
import com.corkili.learningserver.scorm.cam.model.AdlseqObjective;
import com.corkili.learningserver.scorm.cam.model.CompletionThreshold;
import com.corkili.learningserver.scorm.cam.model.ConditionRule;
import com.corkili.learningserver.scorm.cam.model.ConstrainedChoiceConsiderations;
import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.cam.model.ControlMode;
import com.corkili.learningserver.scorm.cam.model.HideLMSUI;
import com.corkili.learningserver.scorm.cam.model.Item;
import com.corkili.learningserver.scorm.cam.model.MapInfo;
import com.corkili.learningserver.scorm.cam.model.NavigationInterface;
import com.corkili.learningserver.scorm.cam.model.Objective;
import com.corkili.learningserver.scorm.cam.model.Objectives;
import com.corkili.learningserver.scorm.cam.model.Organization;
import com.corkili.learningserver.scorm.cam.model.Presentation;
import com.corkili.learningserver.scorm.cam.model.RandomizationControls;
import com.corkili.learningserver.scorm.cam.model.RollupConsiderations;
import com.corkili.learningserver.scorm.cam.model.RollupRule;
import com.corkili.learningserver.scorm.cam.model.RollupRules;
import com.corkili.learningserver.scorm.cam.model.Sequencing;
import com.corkili.learningserver.scorm.cam.model.SequencingCollection;
import com.corkili.learningserver.scorm.cam.model.SequencingRules;
import com.corkili.learningserver.scorm.cam.model.util.CPUtils;
import com.corkili.learningserver.scorm.common.LMSPersistDriverManager;
import com.corkili.learningserver.scorm.rte.api.LMSLearnerInfo;
import com.corkili.learningserver.scorm.sn.model.datatype.Vocabulary;
import com.corkili.learningserver.scorm.sn.model.definition.ConstrainChoiceControls;
import com.corkili.learningserver.scorm.sn.model.definition.LimitConditions;
import com.corkili.learningserver.scorm.sn.model.definition.ObjectiveDescription;
import com.corkili.learningserver.scorm.sn.model.definition.ObjectiveMap;
import com.corkili.learningserver.scorm.sn.model.definition.RollupCondition;
import com.corkili.learningserver.scorm.sn.model.definition.RollupConsiderationControls;
import com.corkili.learningserver.scorm.sn.model.definition.RollupRuleDescription;
import com.corkili.learningserver.scorm.sn.model.definition.RuleCondition;
import com.corkili.learningserver.scorm.sn.model.definition.SequencingControlMode;
import com.corkili.learningserver.scorm.sn.model.definition.SequencingDefinition;
import com.corkili.learningserver.scorm.sn.model.definition.SequencingRuleDescription;
import com.corkili.learningserver.scorm.sn.model.definition.SequencingRuleDescription.ConditionType;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;
import com.corkili.learningserver.scorm.sn.common.ID;

@Slf4j
public class ActivityTreeGenerator {

    private static LMSPersistDriverManager lmsPersistDriverManager = LMSPersistDriverManager.getInstance();

    public static Map<ID, ActivityTree> deriveActivityTreesFrom(
            ContentPackage contentPackage, String lmsContentPackageID, LMSLearnerInfo lmsLearnerInfo) {
        Map<ID, ActivityTree> map = new HashMap<>();
        SequencingCollection sequencingCollection = contentPackage.getManifest().getSequencingCollection();
        for (Organization organization : contentPackage.getManifest().getOrganizations().getOrganizationList()) {
            ID id = new ID(organization.getIdentifier().getValue(), lmsContentPackageID, lmsLearnerInfo.getLearnerID());
            ActivityTree activityTree = deriveActivityTreeFrom(organization, sequencingCollection, id);
            if (activityTree == null) {
                log.error("derive activity tree error - activity tree \"{}\"", id);
            } else {
                initAvailableChildren(activityTree);
                map.put(id, activityTree);
            }
        }
        return map;
    }

    private static ActivityTree deriveActivityTreeFrom(
            Organization organization, SequencingCollection sequencingCollection, ID id) {
        ActivityTree activityTree = new ActivityTree(id);
        activityTree.setObjectivesGlobalToSystem(organization.isObjectivesGlobalToSystem());
        // root
        Activity root = new Activity(
                new ID(organization.getIdentifier().getValue(), id.getLmsContentPackageID(), id.getLmsLearnerID()));
        root.setTitle(organization.getTitle());
        initSequencingDefinition(root, organization.getSequencing(), sequencingCollection);
        initCompletionThreshold(root, organization.getCompletionThreshold());
        activityTree.setRoot(root);
        for (Item item : organization.getItemList()) {
            Activity activity = deriveActivityFrom(item, sequencingCollection,
                    new ID(item.getIdentifierref(), id.getLmsContentPackageID(), id.getLmsLearnerID()), root);
            root.getChildren().add(activity);
        }
        initActivityProgressInformation(root);
        return activityTree;
    }

    private static Activity deriveActivityFrom(
            Item item, SequencingCollection sequencingCollection, ID id, Activity parentActivity) {
        Activity activity = new Activity(id);
        activity.setParentActivity(parentActivity);
        activity.setVisible(item.isIsvisible());
        activity.setTitle(item.getTitle());
        if (item.getParameters() != null) {
            activity.setParameters(item.getParameters());
        }
        if (item.getIdentifierref() != null) {
            activity.setReferenceResource(item.getIdentifierref());
        }
        initSequencingDefinition(activity, item.getSequencing(), sequencingCollection);
        initCompletionThreshold(activity, item.getCompletionThreshold());
        initPresentation(activity, item.getPresentation());
        for (Item childItem : item.getItemList()) {
            Activity childActivity = deriveActivityFrom(item, sequencingCollection,
                    new ID(childItem.getIdentifier().getValue(), id.getLmsContentPackageID(), id.getLmsLearnerID()), activity);
            activity.getChildren().add(childActivity);
        }
        initActivityProgressInformation(activity);
        return activity;
    }

    private static void initAvailableChildren(ActivityTree activityTree) {
        initAvailableChildren(activityTree.getRoot());
    }

    private static void initAvailableChildren(Activity activity) {
        if (activity == null) {
            return;
        }
        for (Activity child : activity.getChildren()) {
            activity.getActivityStateInformation().getAvailableChildren().add(child);
            initAvailableChildren(child);
        }
    }

    private static void initSequencingDefinition(Activity activity, Sequencing sequencing, SequencingCollection sequencingCollection) {
        if (sequencing != null) {
            if (sequencing.getIdRef() != null && StringUtils.isNotBlank(sequencing.getIdRef().getValue())
                    && sequencingCollection != null) {
                initSequencingDefinition(
                        activity, CPUtils.findSequencingByID(sequencingCollection, sequencing.getIdRef().getValue()));
            }
            initSequencingDefinition(activity, sequencing);
        }
    }

    private static void initActivityProgressInformation(Activity activity) {
        if (lmsPersistDriverManager.getDriver() == null) {
            log.error("not found lms persist driver");
            return;
        }
        ID id = activity.getId();
        int attemptCount = lmsPersistDriverManager.getDriver().queryActivityAttemptCountBy(
                id.getLmsContentPackageID(), id.getIdentifier(), id.getLmsLearnerID());
        if (attemptCount > 0) {
            activity.getActivityProgressInformation().setActivityProgressStatus(true)
                    .getActivityAttemptCount().setValue(attemptCount);
        }
    }

    private static void initSequencingDefinition(Activity activity, Sequencing sequencing) {
        if (sequencing == null) {
            return;
        }
        SequencingDefinition sequencingDefinition = activity.getSequencingDefinition();
        // SequencingControlMode
        if (sequencing.getControlMode() != null) {
            ControlMode controlMode = sequencing.getControlMode();
            SequencingControlMode sequencingControlMode = sequencingDefinition.getSequencingControlMode();
            sequencingControlMode.setSequencingControlChoice(controlMode.isChoice());
            sequencingControlMode.setSequencingControlChoiceExit(controlMode.isChoiceExit());
            sequencingControlMode.setSequencingControlFlow(controlMode.isFlow());
            sequencingControlMode.setSequencingControlForwardOnly(controlMode.isForwardOnly());
            sequencingControlMode.setUseCurrentAttemptObjectiveInformation(controlMode.isUseCurrentAttemptObjectiveInfo());
            sequencingControlMode.setUseCurrentAttemptProgressInformation(controlMode.isUseCurrentAttemptProgressInfo());
        }
        // ConstrainChoiceControls
        if (sequencing.getConstrainedChoiceConsiderations() != null) {
            ConstrainedChoiceConsiderations constrainedChoiceConsiderations = sequencing.getConstrainedChoiceConsiderations();
            ConstrainChoiceControls constrainChoiceControls = sequencingDefinition.getConstrainChoiceControls();
            constrainChoiceControls.setConstrainChoice(constrainedChoiceConsiderations.isConstrainChoice());
            constrainChoiceControls.setPreventActivation(constrainedChoiceConsiderations.isPreventActivation());
        }
        // SequencingRuleDescriptions
        if (sequencing.getSequencingRules() != null) {
            SequencingRules sequencingRules = sequencing.getSequencingRules();
            List<SequencingRuleDescription> sequencingRuleDescriptions = sequencingDefinition.getSequencingRuleDescriptions();
            for (ConditionRule conditionRule : sequencingRules.getPreConditionRuleList()) {
                sequencingRuleDescriptions.add(deriveSequencingRuleDescriptionFrom(conditionRule, ConditionType.PRECONDITION));
            }
            for (ConditionRule conditionRule : sequencingRules.getPostConditionRuleList()) {
                sequencingRuleDescriptions.add(deriveSequencingRuleDescriptionFrom(conditionRule, ConditionType.POSTCONDITION));
            }
            for (ConditionRule conditionRule : sequencingRules.getExitConditionRuleList()) {
                sequencingRuleDescriptions.add(deriveSequencingRuleDescriptionFrom(conditionRule, ConditionType.EXITCONDITION));
            }
        }
        // LimitConditions
        if (sequencing.getLimitConditions() != null) {
            com.corkili.learningserver.scorm.cam.model.LimitConditions limit = sequencing.getLimitConditions();
            LimitConditions limitConditions = sequencingDefinition.getLimitConditions();
            // attemptControl & attemptLimit
            if (limit.getAttemptLimit() != null) {
                limitConditions.setAttemptControl(true);
                limitConditions.getAttemptLimit().setValue(limit.getAttemptLimit().getIntValue());
            } else {
                limitConditions.setAttemptControl(false);
            }
            // attemptAbsoluteDurationControl & attemptAbsoluteDurationLimit
            if (StringUtils.isNotBlank(limit.getAttemptAbsoluteDurationLimit())) {
                limitConditions.setAttemptAbsoluteDurationControl(true);
                limitConditions.setAttemptAbsoluteDurationLimit(Duration.parse(limit.getAttemptAbsoluteDurationLimit()));
            } else {
                limitConditions.setAttemptAbsoluteDurationControl(false);
            }
        }
        // RollupRuleDescriptions & RollupControls
        if (sequencing.getRollupRules() != null) {
            RollupRules rollupRules = sequencing.getRollupRules();
            for (RollupRule rollupRule : rollupRules.getRollupRuleList()) {
                RollupRuleDescription description = new RollupRuleDescription();
                // conditionCombination
                switch (rollupRule.getRollupConditions().getConditionCombination().getValue()) {
                    case "all":
                        description.getConditionCombination().setValue("All");
                        break;
                    case "any":
                        description.getConditionCombination().setValue("Any");
                        break;
                }
                // childActivitySet
                switch (rollupRule.getChildActivitySet().getValue()) {
                    case "all":
                        description.getChildActivitySet().setValue("All");
                        break;
                    case "any":
                        description.getChildActivitySet().setValue("Any");
                        break;
                    case "none":
                        description.getChildActivitySet().setValue("None");
                        break;
                    case "atLeastCount":
                        description.getChildActivitySet().setValue("At Least Count");
                        break;
                    case "atLeastPercent":
                        description.getChildActivitySet().setValue("At Least Percent");
                        break;
                }
                // rollupMinimumCount
                description.getRollupMinimumCount().setValue(rollupRule.getMinimumCount().getIntValue());
                // rollupMinimumPercent
                description.getRollupMinimumPercent().setValue(rollupRule.getMinimumPercent().getDecimalValue().doubleValue());
                // rollupAction
                switch (rollupRule.getRollupAction().getValue()) {
                    case "satisfied":
                        description.getRollupAction().setValue("Satisfied");
                        break;
                    case "notSatisfied":
                        description.getRollupAction().setValue("Not Satisfied");
                        break;
                    case "completed":
                        description.getRollupAction().setValue("Completed");
                        break;
                    case "incomplete":
                        description.getRollupAction().setValue("Incomplete");
                        break;
                }
                // rollupConditions
                for (com.corkili.learningserver.scorm.cam.model.RollupCondition condition :
                        rollupRule.getRollupConditions().getRollupConditionList()) {
                    RollupCondition rollupCondition = new RollupCondition();
                    switch (condition.getCondition().getValue()) {
                        case "satisfied":
                            rollupCondition.getRollupCondition().setValue("Satisfied");
                            break;
                        case "objectiveStatusKnown":
                            rollupCondition.getRollupCondition().setValue("Objective Status Known");
                            break;
                        case "objectiveMeasureKnown":
                            rollupCondition.getRollupCondition().setValue("Objective Measure Known");
                            break;
                        case "completed":
                            rollupCondition.getRollupCondition().setValue("Completed");
                            break;
                        case "activityProgressKnown":
                            rollupCondition.getRollupCondition().setValue("Activity Progress Known");
                            break;
                        case "attempted":
                            rollupCondition.getRollupCondition().setValue("Attempted");
                            break;
                        case "attemptLimitExceeded":
                            rollupCondition.getRollupCondition().setValue("Attempt Limit Exceeded");
                            break;
                        case "timeLimitExceeded":
                            rollupCondition.getRollupCondition().setValue("Never");
                            break;
                        case "outsideAvailableTimeRange":
                            rollupCondition.getRollupCondition().setValue("Never");
                            break;
                    }
                    switch (condition.getOperator().getValue()) {
                        case "not":
                            rollupCondition.getOperator().setValue("Not");
                            break;
                        case "noOp":
                            rollupCondition.getOperator().setValue("NO-OP");
                            break;
                    }
                    description.getRollupConditions().add(rollupCondition);
                }
                sequencingDefinition.getRollupRuleDescriptions().add(description);
            }
            // rollupObjectiveSatisfied
            sequencingDefinition.getRollupControls().setRollupObjectiveSatisfied(rollupRules.isRollupObjectiveSatisfied());
            // rollupObjectiveMeasureWeight
            sequencingDefinition.getRollupControls().getRollupObjectiveMeasureWeight().setValue(
                    rollupRules.getObjectiveMeasureWeight().getDecimalValue().doubleValue());
            // rollupProgressCompletion
            sequencingDefinition.getRollupControls().setRollupProgressCompletion(rollupRules.isRollupProgressCompletion());
        }
        // RollupConsiderationControls
        if (sequencing.getRollupConsiderations() != null) {
            RollupConsiderationControls rollupConsiderationControls = sequencingDefinition.getRollupConsiderationControls();
            RollupConsiderations rollupConsiderations = sequencing.getRollupConsiderations();
            rollupConsiderationControls.getRequiredForSatisfied()
                    .setValue(rollupConsiderations.getRequiredForSatisfied().getValue());
            rollupConsiderationControls.getRequiredForNotSatisfied()
                    .setValue(rollupConsiderations.getRequiredForNotSatisfied().getValue());
            rollupConsiderationControls.getRequiredForCompleted()
                    .setValue(rollupConsiderations.getRequiredForCompleted().getValue());
            rollupConsiderationControls.getRequiredForIncomplete()
                    .setValue(rollupConsiderations.getRequiredForIncomplete().getValue());
            rollupConsiderationControls.setMeasureSatisfactionIfActive(rollupConsiderations.isMeasureSatisfactionIfActive());
        }
        // objectiveDescriptions
        if (sequencing.getObjectives() != null) {
            Objectives objectives = sequencing.getObjectives();
            sequencingDefinition.getObjectiveDescriptions().add(deriveObjectiveDescriptionFrom(
                    objectives.getPrimaryObjective(), 
                    CPUtils.findAdlseqObjectiveByID(sequencing.getAdlseqObjectives(), 
                            objectives.getPrimaryObjective().getObjectiveID()), true));
            for (Objective objective : objectives.getObjectiveList()) {
                sequencingDefinition.getObjectiveDescriptions().add(deriveObjectiveDescriptionFrom(objective,
                        CPUtils.findAdlseqObjectiveByID(sequencing.getAdlseqObjectives(), objective.getObjectiveID()), 
                        false));
            }
        }
        // SelectionControls & RandomizationControls
        if (sequencing.getRandomizationControls() != null) {
            RandomizationControls randomizationControls = sequencing.getRandomizationControls();
            setTiming(randomizationControls.getSelectionTiming().getValue(),
                    sequencingDefinition.getSelectionControls().getSelectionTiming());
            if (randomizationControls.getSelectCount() != null) {
                sequencingDefinition.getSelectionControls().setSelectionCountStatus(true);
                sequencingDefinition.getSelectionControls().getSelectionCount().setValue(
                        randomizationControls.getSelectCount().getIntValue());
            } else {
                sequencingDefinition.getSelectionControls().setSelectionCountStatus(false);
            }
            setTiming(randomizationControls.getRandomizationTiming().getValue(),
                    sequencingDefinition.getRandomizationControls().getRandomizationTiming());
            sequencingDefinition.getRandomizationControls().setRandomizeChildren(randomizationControls.isReorderChildren());
        }
        // DeliveryControls
        if (sequencing.getDeliveryControls() != null) {
            sequencingDefinition.getDeliveryControls().setTracked(sequencing.getDeliveryControls().isTracked());
            sequencingDefinition.getDeliveryControls().setCompletionSetByContent(sequencing.getDeliveryControls().isCompletionSetByContent());
            sequencingDefinition.getDeliveryControls().setObjectiveSetByContent(sequencing.getDeliveryControls().isObjectiveSetByContent());
        }
    }

    private static void setTiming(String rawValue, Vocabulary filed) {
        switch (rawValue) {
            case "never":
                filed.setValue("Never");
                break;
            case "once":
                filed.setValue("Once");
                break;
            case "onEachNewAttempt":
                filed.setValue("On Each New Attempt");
                break;
        }
    }
    
    private static ObjectiveDescription deriveObjectiveDescriptionFrom(
            Objective objective, AdlseqObjective adlseqObjective, boolean isPrimary) {
        String objectiveID = objective.getObjectiveID() != null 
                && StringUtils.isNotBlank(objective.getObjectiveID().getValue()) ? objective.getObjectiveID().getValue() : null;
        ObjectiveDescription description = new ObjectiveDescription(isPrimary);
        if (objectiveID != null) {
            description.setObjectiveID(objectiveID);
        }
        description.setObjectiveSatisfiedByMeasure(objective.isSatisfiedByMeasure());
        description.getObjectiveMinimumSatisfiedNormalizedMeasure().setValue(
                objective.getMinNormalizedMeasure().getDecimalValue().doubleValue());
        Set<String> processedID = new HashSet<>();
        for (MapInfo mapInfo : objective.getMapInfoList()) {
            String targetObjectiveID = mapInfo.getTargetObjectiveID() != null
                    && StringUtils.isNotBlank(mapInfo.getTargetObjectiveID().getValue()) ?
                    mapInfo.getTargetObjectiveID().getValue() : null;
            AdlseqMapInfo adlseqMapInfo = CPUtils.findAdlseqMapInfoByID(adlseqObjective, mapInfo.getTargetObjectiveID());
            ObjectiveMap objectiveMap = deriveObjectiveMapFrom(mapInfo, adlseqMapInfo, objectiveID, targetObjectiveID);
            if (objectiveMap != null) {
                processedID.add(targetObjectiveID);
                description.getObjectiveMaps().add(objectiveMap);
            }
        }
        if (adlseqObjective != null) {
            for (AdlseqMapInfo adlseqMapInfo : adlseqObjective.getMapInfoList()) {
                String targetObjectiveID = adlseqMapInfo.getTargetObjectiveID() != null
                        && StringUtils.isNotBlank(adlseqMapInfo.getTargetObjectiveID().getValue()) ?
                        adlseqMapInfo.getTargetObjectiveID().getValue() : null;
                if (targetObjectiveID == null || processedID.contains(targetObjectiveID)) {
                    continue;
                }
                MapInfo mapInfo = CPUtils.findMapInfoByID(objective, adlseqMapInfo.getTargetObjectiveID());
                ObjectiveMap objectiveMap = deriveObjectiveMapFrom(mapInfo, adlseqMapInfo, objectiveID, targetObjectiveID);
                if (objectiveMap != null) {
                    description.getObjectiveMaps().add(objectiveMap);
                }
            }
        }
        return description;
    }
    
    private static ObjectiveMap deriveObjectiveMapFrom(
            MapInfo mapInfo, AdlseqMapInfo adlseqMapInfo, String objectiveID, String targetObjectiveID) {
        if (mapInfo == null && adlseqMapInfo == null) {
            return null;
        }
        if (objectiveID == null || targetObjectiveID == null) {
            return null;
        }
        if (mapInfo != null && !targetObjectiveID.equals(mapInfo.getTargetObjectiveID().getValue())) {
            return null;
        }
        if (adlseqMapInfo != null && !targetObjectiveID.equals(adlseqMapInfo.getTargetObjectiveID().getValue())) {
            return null;
        }
        ObjectiveMap objectiveMap = new ObjectiveMap(objectiveID, targetObjectiveID);
        if (mapInfo != null) {
            objectiveMap.setReadObjectiveSatisfiedStatus(mapInfo.isReadSatisfiedStatus());
            objectiveMap.setReadObjectiveNormalizedMeasure(mapInfo.isReadNormalizedMeasure());
            objectiveMap.setWriteObjectiveSatisfiedStatus(mapInfo.isWriteSatisfiedStatus());
            objectiveMap.setWriteObjectiveNormalizedMeasure(mapInfo.isWriteNormalizedMeasure());
        }
        if (adlseqMapInfo != null) {
            objectiveMap.setReadRawScore(adlseqMapInfo.isReadRawScore());
            objectiveMap.setReadMinScore(adlseqMapInfo.isReadMinScore());
            objectiveMap.setReadMaxScore(adlseqMapInfo.isReadMaxScore());
            objectiveMap.setReadCompletionStatus(adlseqMapInfo.isReadCompletionStatus());
            objectiveMap.setReadProgressMeasure(adlseqMapInfo.isReadProgressMeasure());
            objectiveMap.setWriteRawScore(adlseqMapInfo.isWriteRawScore());
            objectiveMap.setWriteMinScore(adlseqMapInfo.isWriteMinScore());
            objectiveMap.setWriteMaxScore(adlseqMapInfo.isWriteMaxScore());
            objectiveMap.setWriteCompletionStatus(adlseqMapInfo.isWriteCompletionStatus());
            objectiveMap.setWriteProgressMeasure(adlseqMapInfo.isWriteProgressMeasure());
        }
        return objectiveMap;
    }

    private static SequencingRuleDescription deriveSequencingRuleDescriptionFrom(
            ConditionRule conditionRule, ConditionType conditionType) {
        SequencingRuleDescription description = new SequencingRuleDescription(conditionType);
        // conditionCombination
        switch (conditionRule.getRuleConditions().getConditionCombination().getValue()) {
            case "all":
                description.getConditionCombination().setValue("All");
                break;
            case "any":
                description.getConditionCombination().setValue("Any");
                break;
        }
        // ruleConditions
        for (com.corkili.learningserver.scorm.cam.model.RuleCondition condition : conditionRule.getRuleConditions().getRuleConditionList()) {
            RuleCondition ruleCondition = new RuleCondition();
            switch (condition.getCondition().getValue()) {
                case "satisfied":
                    ruleCondition.getRuleCondition().setValue("Satisfied");
                    break;
                case "objectiveStatusKnown":
                    ruleCondition.getRuleCondition().setValue("Objective Status Known");
                    break;
                case "objectiveMeasureKnown":
                    ruleCondition.getRuleCondition().setValue("Objective Measure Known");
                    break;
                case "objectiveMeasureGreaterThan":
                    ruleCondition.getRuleCondition().setValue("Objective Measure Greater Than");
                    break;
                case "objectiveMeasureLessThan":
                    ruleCondition.getRuleCondition().setValue("Objective Measure Less Than");
                    break;
                case "completed":
                    ruleCondition.getRuleCondition().setValue("Completed");
                    break;
                case "activityProgressKnown":
                    ruleCondition.getRuleCondition().setValue("Activity Progress Known");
                    break;
                case "attempted":
                    ruleCondition.getRuleCondition().setValue("Attempted");
                    break;
                case "attemptLimitExceeded":
                    ruleCondition.getRuleCondition().setValue("Attempt Limit Exceeded");
                    break;
                case "timeLimitExceeded":
                    ruleCondition.getRuleCondition().setValue("Always");
                    break;
                case "outsideAvailableTimeRange":
                    ruleCondition.getRuleCondition().setValue("Always");
                    break;
                case "always":
                    ruleCondition.getRuleCondition().setValue("Always");
                    break;
            }
            if (StringUtils.isNotBlank(condition.getReferencedObjective())) {
                ruleCondition.setReferencedObjective(condition.getReferencedObjective());
            }
            if (condition.getMeasureThreshold() != null) {
                ruleCondition.getMeasureThreshold().setValue(condition.getMeasureThreshold().getDecimalValue().doubleValue());
            }
            switch (condition.getOperator().getValue()) {
                case "not":
                    ruleCondition.getOperator().setValue("Not");
                    break;
                case "noOp":
                    ruleCondition.getOperator().setValue("NO-OP");
                    break;
            }
            description.getRuleConditions().add(ruleCondition);
        }
        // ruleAction
        if (conditionType == ConditionType.PRECONDITION) {
            switch (conditionRule.getRuleAction().getAction().getValue()) {
                case "skip":
                    description.getRuleAction().setValue("Skip");
                    break;
                case "disabled":
                    description.getRuleAction().setValue("Disabled");
                    break;
                case "hiddenFromChoice":
                    description.getRuleAction().setValue("Hidden from Choice");
                    break;
                case "stopForwardTraversal":
                    description.getRuleAction().setValue("Stop Forward Traversal");
                    break;
            }
        } else if (conditionType == ConditionType.POSTCONDITION) {
            switch (conditionRule.getRuleAction().getAction().getValue()) {
                case "exitParent":
                    description.getRuleAction().setValue("Exit Parent");
                    break;
                case "exitAll":
                    description.getRuleAction().setValue("Exit All");
                    break;
                case "retry":
                    description.getRuleAction().setValue("Retry");
                    break;
                case "retryAll":
                    description.getRuleAction().setValue("Retry All");
                    break;
                case "continue":
                    description.getRuleAction().setValue("Continue");
                    break;
                case "previous":
                    description.getRuleAction().setValue("Previous");
                    break;
            }

        } else if (conditionType == ConditionType.EXITCONDITION) {
            if ("exit".equals(conditionRule.getRuleAction().getAction().getValue())) {
                description.getRuleAction().setValue("Exit");
            }
        }
        return description;
    }

    private static void initPresentation(Activity activity, Presentation presentation) {
        if (presentation != null) {
            NavigationInterface navigationInterface = presentation.getNavigationInterface();
            if (navigationInterface != null) {
                for (HideLMSUI hideLMSUI : navigationInterface.getHideLMSUIList()) {
                    switch (hideLMSUI.getValue()) {
                        case "previous":
                            activity.getHideLmsUIControls().setHidePrevious(true);
                            break;
                        case "continue":
                            activity.getHideLmsUIControls().setHideContinue(true);
                            break;
                        case "exit":
                            activity.getHideLmsUIControls().setHideExit(true);
                            break;
                        case "exitAll":
                            activity.getHideLmsUIControls().setHideExitAll(true);
                            break;
                        case "abandon":
                            activity.getHideLmsUIControls().setHideAbandon(true);
                            break;
                        case "abandonAll":
                            activity.getHideLmsUIControls().setHideAbandonAll(true);
                            break;
                        case "suspendAll":
                            activity.getHideLmsUIControls().setHideSuspendAll(true);
                            break;
                    }
                }
            }
        }
    }

    private static void initCompletionThreshold(Activity activity, CompletionThreshold completionThreshold) {
        if (completionThreshold != null) {
            activity.getSequencingDefinition().getCompletionThreshold()
                    .setCompletedByMeasure(completionThreshold.isCompletedByMeasure());
            if (completionThreshold.getMinProgressMeasure() != null) {
                activity.getSequencingDefinition().getCompletionThreshold().getMinimumProgressMeasure()
                        .setValue(completionThreshold.getMinProgressMeasure().getDecimalValue().doubleValue());
            }
            if (completionThreshold.getProgressWeight() != null) {
                activity.getSequencingDefinition().getCompletionThreshold().getProgressWeight()
                        .setValue(completionThreshold.getProgressWeight().getDecimalValue().doubleValue());
            }
        }
    }

}
