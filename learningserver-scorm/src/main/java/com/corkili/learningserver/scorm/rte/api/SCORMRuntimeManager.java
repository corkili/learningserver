package com.corkili.learningserver.scorm.rte.api;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.scorm.cam.load.SCORMPackageManager;
import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.cam.model.Item;
import com.corkili.learningserver.scorm.cam.model.util.CPUtils;
import com.corkili.learningserver.scorm.common.LMSPersistDriverManager;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;

@Slf4j
public class SCORMRuntimeManager {

    private static SCORMRuntimeManager instance;

    private LMSPersistDriverManager lmsPersistDriverManager;

    private SCORMPackageManager scormPackageManager;

    private Map<AttemptID, LearnerAttempt> learnerAttemptMap;

    private SCORMRuntimeManager() {
        lmsPersistDriverManager = LMSPersistDriverManager.getInstance();
        scormPackageManager = SCORMPackageManager.getInstance();
        learnerAttemptMap = new ConcurrentHashMap<>();
    }

    public static SCORMRuntimeManager getInstance() {
        if (instance == null) {
            synchronized (SCORMRuntimeManager.class) {
                if (instance == null) {
                    instance = new SCORMRuntimeManager();
                }
            }
        }
        return instance;
    }

    public boolean launch(String lmsContentPackageID, String activityItemID, LMSLearnerInfo lmsLearnerInfo) {
        return launch(lmsContentPackageID, lmsLearnerInfo, activityItemID, false);
    }

    public boolean launch(String lmsContentPackageID, LMSLearnerInfo lmsLearnerInfo, String activityItemID, boolean reloadIfPresent) {
        ContentPackage contentPackage = scormPackageManager.launch(lmsContentPackageID, reloadIfPresent);
        if (contentPackage == null) {
            return false;
        }
        return launch(lmsContentPackageID, contentPackage, lmsLearnerInfo, activityItemID, reloadIfPresent);
    }

    private boolean launch(String lmsContentPackageID, ContentPackage contentPackage, LMSLearnerInfo lmsLearnerInfo,
                           String activityItemID, boolean reloadIfPresent) {
        Item item = CPUtils.findItemByIdentifier(contentPackage, activityItemID);
        if (item == null) {
            log.error("not found the activity's item {}", activityItemID);
            return false;
        }
        AttemptID attemptID = new AttemptID(lmsLearnerInfo, lmsContentPackageID, activityItemID);
        if (learnerAttemptMap.containsKey(attemptID) && !reloadIfPresent) {
            return true;
        }
        LearnerAttempt learnerAttempt = new LearnerAttempt(attemptID);
        if (!learnerAttempt.initRuntimeData(contentPackage, item, lmsLearnerInfo)) {
            log.error("init run-time data error");
            return false;
        }
        learnerAttemptMap.put(learnerAttempt.getAttemptID(), learnerAttempt);
        return true;
    }

    public void unlaunch(LMSLearnerInfo lmsLearnerInfo) {
        List<AttemptID> shouldDelete = new LinkedList<>();
        for (AttemptID attemptID : learnerAttemptMap.keySet()) {
            if (attemptID.getLmsLearnerInfo().equals(lmsLearnerInfo)) {
                shouldDelete.add(attemptID);
            }
        }
        unlaunch(shouldDelete);
    }

    public void unlaunch(String lmsContentPackageID) {
        List<AttemptID> shouldDelete = new LinkedList<>();
        for (AttemptID attemptID : learnerAttemptMap.keySet()) {
            if (attemptID.getLmsContentPackageID().equals(lmsContentPackageID)) {
                shouldDelete.add(attemptID);
            }
        }
        unlaunch(shouldDelete);
    }

    public void unlaunch(LMSLearnerInfo lmsLearnerInfo, String lmsContentPackageID) {
        List<AttemptID> shouldDelete = new LinkedList<>();
        for (AttemptID attemptID : learnerAttemptMap.keySet()) {
            if (attemptID.getLmsLearnerInfo().equals(lmsLearnerInfo)
                    && attemptID.getLmsContentPackageID().equals(lmsContentPackageID)) {
                shouldDelete.add(attemptID);
            }
        }
        unlaunch(shouldDelete);
    }

    private void unlaunch(List<AttemptID> shouldDelete) {
        for (AttemptID attemptID : shouldDelete) {
            learnerAttemptMap.remove(attemptID);
        }
    }

    public String initialize(AttemptID attemptID, String parameter) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            learnerAttempt.openLearnerSession();
            return learnerAttempt.initialize(parameter);
        } else {
            return "false";
        }
    }

    public String terminate(AttemptID attemptID, String parameter) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            String returnValue = learnerAttempt.terminate(parameter);
            learnerAttempt.closeLearnerSession(false);
            return returnValue;
        } else {
            return "false";
        }
    }

    public String getValue(AttemptID attemptID, String elementName) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            return learnerAttempt.getValue(elementName);
        } else {
            return "";
        }
    }

    public String setValue(AttemptID attemptID, String elementName, String value) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            return learnerAttempt.setValue(elementName, value);
        } else {
            return "false";
        }
    }

    public String commit(AttemptID attemptID, String parameter) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            return learnerAttempt.commit(parameter);
        } else {
            return "false";
        }
    }

    public String getLastErrorCode(AttemptID attemptID) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            return learnerAttempt.getLastErrorCode();
        } else {
            return String.valueOf(ScormError.E_101.getCode());
        }
    }

    public String getLastErrorString(AttemptID attemptID) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            return learnerAttempt.getLastErrorString();
        } else {
            return ScormError.E_101.getMsg();
        }
    }

    public String getLastDiagnostic(AttemptID attemptID) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            return learnerAttempt.getLastDiagnostic();
        } else {
            return "please try again";
        }
    }
}
