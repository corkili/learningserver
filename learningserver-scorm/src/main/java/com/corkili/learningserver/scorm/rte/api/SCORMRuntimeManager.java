package com.corkili.learningserver.scorm.rte.api;

import com.corkili.learningserver.scorm.cam.load.SCORMPackageManager;
import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.cam.model.Item;
import com.corkili.learningserver.scorm.cam.model.util.CPUtils;
import com.corkili.learningserver.scorm.common.ID;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SCORMRuntimeManager {

    private static SCORMRuntimeManager instance;

    private SCORMPackageManager scormPackageManager;

    private Map<ID, LearnerAttempt> learnerAttemptMap;

    private SCORMRuntimeManager() {
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
        return launch(lmsContentPackageID, activityItemID, lmsLearnerInfo, false);
    }

    public boolean launch(String lmsContentPackageID, String activityItemID, LMSLearnerInfo lmsLearnerInfo, boolean reloadIfPresent) {
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
        ID id = new ID(activityItemID, lmsContentPackageID, lmsLearnerInfo.getLearnerID());
        if (learnerAttemptMap.containsKey(id) && !reloadIfPresent) {
            return true;
        }
        LearnerAttempt learnerAttempt = new LearnerAttempt(id);
        if (!learnerAttempt.initRuntimeData(contentPackage, item, lmsLearnerInfo)) {
            log.error("init run-time data error");
            return false;
        }
        learnerAttemptMap.put(learnerAttempt.getAttemptID(), learnerAttempt);
        return true;
    }

    public void unlaunch(LMSLearnerInfo lmsLearnerInfo) {
        List<ID> shouldDelete = new LinkedList<>();
        for (ID attemptID : learnerAttemptMap.keySet()) {
            if (attemptID.getLmsLearnerID().equals(lmsLearnerInfo.getLearnerID())) {
                shouldDelete.add(attemptID);
            }
        }
        unlaunch(shouldDelete);
    }

    public void unlaunch(String lmsContentPackageID) {
        List<ID> shouldDelete = new LinkedList<>();
        for (ID attemptID : learnerAttemptMap.keySet()) {
            if (attemptID.getLmsContentPackageID().equals(lmsContentPackageID)) {
                shouldDelete.add(attemptID);
            }
        }
        unlaunch(shouldDelete);
    }

    public void unlaunch(LMSLearnerInfo lmsLearnerInfo, String lmsContentPackageID) {
        List<ID> shouldDelete = new LinkedList<>();
        for (ID attemptID : learnerAttemptMap.keySet()) {
            if (attemptID.getLmsLearnerID().equals(lmsLearnerInfo.getLearnerID())
                    && attemptID.getLmsContentPackageID().equals(lmsContentPackageID)) {
                shouldDelete.add(attemptID);
            }
        }
        unlaunch(shouldDelete);
    }

    private void unlaunch(List<ID> shouldDelete) {
        for (ID attemptID : shouldDelete) {
            learnerAttemptMap.remove(attemptID);
        }
    }

    public LearnerAttempt getLearnerAttempt(ID id) {
        return learnerAttemptMap.get(id);
    }

    public String initialize(ID attemptID, String parameter) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            learnerAttempt.openLearnerSession();
            return learnerAttempt.initialize(parameter);
        } else {
            return "false";
        }
    }

    public String terminate(ID attemptID, String parameter) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            String returnValue = learnerAttempt.terminate(parameter);
            learnerAttempt.closeLearnerSession(false);
            return returnValue;
        } else {
            return "false";
        }
    }

    public String getValue(ID attemptID, String elementName) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            return learnerAttempt.getValue(elementName);
        } else {
            return "";
        }
    }

    public String setValue(ID attemptID, String elementName, String value) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            return learnerAttempt.setValue(elementName, value);
        } else {
            return "false";
        }
    }

    public String commit(ID attemptID, String parameter) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            return learnerAttempt.commit(parameter);
        } else {
            return "false";
        }
    }

    public String getLastErrorCode(ID attemptID) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            return learnerAttempt.getLastErrorCode();
        } else {
            return String.valueOf(ScormError.E_101.getCode());
        }
    }

    public String getLastErrorString(ID attemptID) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            return learnerAttempt.getLastErrorString();
        } else {
            return ScormError.E_101.getMsg();
        }
    }

    public String getLastDiagnostic(ID attemptID) {
        LearnerAttempt learnerAttempt = learnerAttemptMap.get(attemptID);
        if (learnerAttempt != null) {
            return learnerAttempt.getLastDiagnostic();
        } else {
            return "please try again";
        }
    }

    public String getErrorString(int errorCode) {
        for (ScormError value : ScormError.values()) {
            if (value.getCode() == errorCode) {
                return value.getMsg();
            }
        }
        return "Undefined error code - " + errorCode;
    }

    public String getDiagnostic(int errorCode) {
        for (ScormError value : ScormError.values()) {
            if (value.getCode() == errorCode) {
                return value.getMsg();
            }
        }
        return "Undefined error code - " + errorCode;
    }
}
