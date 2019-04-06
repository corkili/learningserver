package com.corkili.learningserver.scorm.sn.api;

import com.corkili.learningserver.scorm.cam.load.SCORMPackageManager;
import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.common.CommonUtils;
import com.corkili.learningserver.scorm.common.ID;
import com.corkili.learningserver.scorm.rte.api.LMSLearnerInfo;
import com.corkili.learningserver.scorm.sn.api.event.NavigationEvent;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SCORMSeqNavManager {

    private static SCORMSeqNavManager instance;

    private SCORMPackageManager scormPackageManager;

    private Map<ID, AttemptManager> attemptManagerMap;

    private SCORMSeqNavManager() {
        this.scormPackageManager = SCORMPackageManager.getInstance();
        this.attemptManagerMap = new ConcurrentHashMap<>();
    }

    public static SCORMSeqNavManager getInstance() {
        if (instance == null) {
            synchronized (SCORMSeqNavManager.class) {
                if (instance == null) {
                    instance = new SCORMSeqNavManager();
                }
            }
        }
        return instance;
    }

    public boolean launch(String lmsContentPackageID, LMSLearnerInfo lmsLearnerInfo, String organizationId) {
        return launch(lmsContentPackageID, lmsLearnerInfo, organizationId, false);
    }

    public boolean launch(String lmsContentPackageID, LMSLearnerInfo lmsLearnerInfo, String organizationId, boolean reloadIfPresent) {
        ContentPackage contentPackage = scormPackageManager.launch(lmsContentPackageID, reloadIfPresent);
        ActivityTree activityTree = ActivityTreeGenerator
                .deriveActivityTreesFrom(contentPackage, lmsContentPackageID, lmsLearnerInfo, organizationId);
        if (activityTree == null) {
            return false;
        }
        if (attemptManagerMap.containsKey(activityTree.getId()) && !reloadIfPresent) {
            return true;
        }
        AttemptManager attemptManager = new AttemptManager(activityTree);
        attemptManagerMap.put(attemptManager.getManagerID(), attemptManager);
        return true;
    }

    public void unlaunch(LMSLearnerInfo lmsLearnerInfo) {
        List<ID> shouldDelete = new LinkedList<>();
        for (ID id : attemptManagerMap.keySet()) {
            if (id.getLmsLearnerID().equals(lmsLearnerInfo.getLearnerID())) {
                shouldDelete.add(id);
            }
        }
        unlaunch(shouldDelete);
    }

    public void unlaunch(String lmsContentPackageID) {
        List<ID> shouldDelete = new LinkedList<>();
        for (ID id : attemptManagerMap.keySet()) {
            if (id.getLmsContentPackageID().equals(lmsContentPackageID)) {
                shouldDelete.add(id);
            }
        }
        unlaunch(shouldDelete);
    }

    public void unlaunch(String learnerID, String lmsContentPackageID) {
        List<ID> shouldDelete = new LinkedList<>();
        for (ID id : attemptManagerMap.keySet()) {
            if (id.getLmsLearnerID().equals(learnerID)
                    && id.getLmsContentPackageID().equals(lmsContentPackageID)) {
                shouldDelete.add(id);
            }
        }
        unlaunch(shouldDelete);
    }

    private void unlaunch(List<ID> shouldDelete) {
        for (ID id : shouldDelete) {
            attemptManagerMap.remove(id);
        }
    }

    public ProcessResult process(ID managerID, NavigationEvent event) {
        if (attemptManagerMap.containsKey(managerID)) {
            return attemptManagerMap.get(managerID).process(event);
        }
        return new ProcessResult(CommonUtils.format("manager id \"{}\" not exist."));
    }

    public AttemptManager findAttemptManagerBy(ID id) {
        return attemptManagerMap.get(id);
    }

}
