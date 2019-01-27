package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;

public class CMI implements GeneralDataType {

    @Meta(value = "_version", writable = false)
    private CharacterString version;

    @Meta("comments_from_learner")
    private CommentsFromLearner commentsFromLearner;

    @Meta("comments_from_lms")
    private CommentsFromLMS commentsFromLMS;

    @Meta("completion_status")
    private CompletionStatus completionStatus;

    @Meta(value = "completion_threshold", writable = false)
    private CompletionThreshold completionThreshold;

    @Meta(value = "credit", writable = false)
    private Credit credit;

    @Meta(value = "entry", writable = false)
    private Entry entry;

    @Meta(value = "exit", readable = false)
    private Exit exit;

    @Meta("interactions")
    private Interactions interactions;

    @Meta(value = "launch_data", writable = false)
    private LaunchData launchData;

    @Meta(value = "learner_id", writable = false)
    private LearnerId learnerId;

    @Meta(value = "learner_name", writable = false)
    private LearnerName learnerName;

    @Meta("learner_preference")
    private LearnerPreference learnerPreference;

    @Meta("location")
    private Location location;

    @Meta(value = "maximum_time_allowed", writable = false)
    private MaximumTimeAllowed maximumTimeAllowed;

    @Meta(value = "mode", writable = false)
    private Mode mode;

    @Meta("objectives")
    private Objectives objectives;

    @Meta("progress_measure")
    private ProgressMeasure progressMeasure;

    @Meta(value = "scaled_passing_score", writable = false)
    private ScaledPassingScore scaledPassingScore;

    @Meta("score")
    private Score score;

    @Meta(value = "session_time", readable = false)
    private SessionTime sessionTime;

    @Meta("success_status")
    private SuccessStatus successStatus;

    @Meta("suspend_data")
    private SuspendData suspendData;

    @Meta(value = "time_limit_action", writable = false)
    private TimeLimitAction timeLimitAction;

    @Meta(value = "total_time", writable = false)
    private TotalTime totalTime;

    public CMI() {
        this.version = new CharacterString("1.0");
        this.commentsFromLearner = new CommentsFromLearner();
        this.commentsFromLMS = new CommentsFromLMS();
        this.completionStatus = new CompletionStatus(this);
        this.completionThreshold = new CompletionThreshold();
        this.credit = new Credit();
        this.entry = new Entry();
        this.exit = new Exit();
        this.interactions = new Interactions();
        this.launchData = new LaunchData();
        this.learnerId = new LearnerId();
        this.learnerPreference = new LearnerPreference();
        this.location = new Location();
        this.maximumTimeAllowed = new MaximumTimeAllowed();
        this.mode = new Mode();
        this.objectives = new Objectives();
        this.progressMeasure = new ProgressMeasure();
        this.scaledPassingScore = new ScaledPassingScore();
        this.score = new Score();
        this.sessionTime = new SessionTime();
        this.successStatus = new SuccessStatus(this);
        this.suspendData = new SuspendData();
        this.timeLimitAction = new TimeLimitAction();
        this.totalTime = new TotalTime();
        registerHandler();
    }

    private void registerHandler() {
        version.registerSetHandler(new ReadOnlyHandler());
    }

    public CharacterString getVersion() {
        return version;
    }

    public void setVersion(CharacterString version) {
        this.version = version;
    }

    public CommentsFromLearner getCommentsFromLearner() {
        return commentsFromLearner;
    }

    public void setCommentsFromLearner(CommentsFromLearner commentsFromLearner) {
        this.commentsFromLearner = commentsFromLearner;
    }

    public CommentsFromLMS getCommentsFromLMS() {
        return commentsFromLMS;
    }

    public void setCommentsFromLMS(CommentsFromLMS commentsFromLMS) {
        this.commentsFromLMS = commentsFromLMS;
    }

    public CompletionStatus getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(CompletionStatus completionStatus) {
        this.completionStatus = completionStatus;
    }

    public CompletionThreshold getCompletionThreshold() {
        return completionThreshold;
    }

    public void setCompletionThreshold(CompletionThreshold completionThreshold) {
        this.completionThreshold = completionThreshold;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public Exit getExit() {
        return exit;
    }

    public void setExit(Exit exit) {
        this.exit = exit;
    }

    public Interactions getInteractions() {
        return interactions;
    }

    public void setInteractions(Interactions interactions) {
        this.interactions = interactions;
    }

    public LaunchData getLaunchData() {
        return launchData;
    }

    public void setLaunchData(LaunchData launchData) {
        this.launchData = launchData;
    }

    public LearnerId getLearnerId() {
        return learnerId;
    }

    public void setLearnerId(LearnerId learnerId) {
        this.learnerId = learnerId;
    }

    public LearnerName getLearnerName() {
        return learnerName;
    }

    public void setLearnerName(LearnerName learnerName) {
        this.learnerName = learnerName;
    }

    public LearnerPreference getLearnerPreference() {
        return learnerPreference;
    }

    public void setLearnerPreference(LearnerPreference learnerPreference) {
        this.learnerPreference = learnerPreference;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public MaximumTimeAllowed getMaximumTimeAllowed() {
        return maximumTimeAllowed;
    }

    public void setMaximumTimeAllowed(MaximumTimeAllowed maximumTimeAllowed) {
        this.maximumTimeAllowed = maximumTimeAllowed;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Objectives getObjectives() {
        return objectives;
    }

    public void setObjectives(Objectives objectives) {
        this.objectives = objectives;
    }

    public ProgressMeasure getProgressMeasure() {
        return progressMeasure;
    }

    public void setProgressMeasure(ProgressMeasure progressMeasure) {
        this.progressMeasure = progressMeasure;
    }

    public ScaledPassingScore getScaledPassingScore() {
        return scaledPassingScore;
    }

    public void setScaledPassingScore(ScaledPassingScore scaledPassingScore) {
        this.scaledPassingScore = scaledPassingScore;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public SessionTime getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(SessionTime sessionTime) {
        this.sessionTime = sessionTime;
    }

    public SuccessStatus getSuccessStatus() {
        return successStatus;
    }

    public void setSuccessStatus(SuccessStatus successStatus) {
        this.successStatus = successStatus;
    }

    public SuspendData getSuspendData() {
        return suspendData;
    }

    public void setSuspendData(SuspendData suspendData) {
        this.suspendData = suspendData;
    }

    public TimeLimitAction getTimeLimitAction() {
        return timeLimitAction;
    }

    public void setTimeLimitAction(TimeLimitAction timeLimitAction) {
        this.timeLimitAction = timeLimitAction;
    }

    public TotalTime getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(TotalTime totalTime) {
        this.totalTime = totalTime;
    }
}
