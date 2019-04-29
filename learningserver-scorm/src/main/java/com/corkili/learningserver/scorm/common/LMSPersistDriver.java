package com.corkili.learningserver.scorm.common;

public interface LMSPersistDriver {

    String querySCORMPackageZipFilePathBy(String lmsContentPackageID);

    int queryActivityAttemptCountBy(String lmsContentPackageID, String activityID, String learnerID);

    void saveActivityAttemptCount(String lmsContentPackageID, String activityID, String learnerID, int attemptCount);

    String queryRuntimeDataBy(String lmsContentPackageID, String activityID, String learnerID);

    void saveRuntimeData(String lmsContentPackageID, String activityID, String learnerID, String runtimeData);

}
