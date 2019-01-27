package com.corkili.learningserver.scorm.rte.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AttemptID {

    private LMSLearnerInfo lmsLearnerInfo;

    private String lmsContentPackageID;

    private String activityItemID;

    public AttemptID(LMSLearnerInfo lmsLearnerInfo, String lmsContentPackageID, String activityItemID) {
        this.lmsLearnerInfo = lmsLearnerInfo;
        this.lmsContentPackageID = lmsContentPackageID;
        this.activityItemID = activityItemID;
    }

    public LMSLearnerInfo getLmsLearnerInfo() {
        return lmsLearnerInfo;
    }

    public void setLmsLearnerInfo(LMSLearnerInfo lmsLearnerInfo) {
        this.lmsLearnerInfo = lmsLearnerInfo;
    }

    public String getLmsContentPackageID() {
        return lmsContentPackageID;
    }

    public void setLmsContentPackageID(String lmsContentPackageID) {
        this.lmsContentPackageID = lmsContentPackageID;
    }

    public String getActivityItemID() {
        return activityItemID;
    }

    public void setActivityItemID(String activityItemID) {
        this.activityItemID = activityItemID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AttemptID attemptID = (AttemptID) o;

        return new EqualsBuilder()
                .append(lmsLearnerInfo, attemptID.lmsLearnerInfo)
                .append(lmsContentPackageID, attemptID.lmsContentPackageID)
                .append(activityItemID, attemptID.activityItemID)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(lmsLearnerInfo)
                .append(lmsContentPackageID)
                .append(activityItemID)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("lmsLearnerInfo", lmsLearnerInfo)
                .append("lmsContentPackageID", lmsContentPackageID)
                .append("activityItemID", activityItemID)
                .toString();
    }
}
