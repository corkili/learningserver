package com.corkili.learningserver.scorm.sn.api.behavior.result;

public enum SequencingException {
    NB211("NB.2.1-1", "Current Activity is already defined / Sequencing session has already begun"),
    NB212("NB.2.1-2", "Current Activity is not defined / Sequencing session has not begun"),
    NB213("NB.2.1-3", "Suspended Activity is not defined"),
    NB214("NB.2.1-4", "Flow Sequencing Control Mode violation"),
    NB215("NB.2.1-5", "Flow or Forward Only Sequencing Control Mode violation"),
    NB216("NB.2.1-6", "No activity is \"previous\" to the root"),
    NB217("NB.2.1-7", "Unsupported navigation request"),
    NB218("NB.2.1-8", "Choice Exit Sequencing Control Mode violation"),
    NB219("NB.2.1-9", "No activities to consider"),
    NB2110("NB.2.1-10", "Choice Sequencing Control Mode violation"),
    NB2111("NB.2.1-11", "Target activity does not exist"),
    NB2112("NB.2.1-12", "Current Activity already terminated"),
    NB2113("NB.2.1-13", "Undefined navigation request"),
    TB231("TB.2.3-1", "Current Activity is not defined / Sequencing session has not begun"),
    TB232("TB.2.3-2", "Current Activity already terminated"),
    TB233("TB.2.3-3", "Cannot suspend an inactive root"),
    TB234("TB.2.3-4", "Activity tree root has no parent"),
    TB235("TB.2.3-5", "Nothing to suspend; No active activities"),
    TB236("TB.2.3-6", "Nothing to abandon; No active activities"),
    TB237("TB.2.3-7", "Undefined termination request"),
    SB211("SB.2.1-1", "Last activity in the tree"),
    SB212("SB.2.1-2", "Cluster has no available children"),
    SB213("SB.2.1-3", "No activity is \"previous\" to the root"),
    SB214("SB.2.1-4", "Forward Only Sequencing Control Mode violation"),
    SB221("SB.2.2-1", "Flow Sequencing Control Mode violation"),
    SB222("SB.2.2-2", "Activity unavailable"),
    SB241("SB.2.4-1", "Forward Traversal Blocked"),
    SB242("SB.2.4-2", "Forward Only Sequencing Control Mode violation"),
    SB243("SB.2.4-3", "No activity is \"previous\" to the root"),
    SB251("SB.2.5-1", "Current Activity is defined / Sequencing session already begun"),
    SB261("SB.2.6-1", "Current Activity is defined / Sequencing session already begun"),
    SB262("SB.2.6-2", "No Suspended Activity defined"),
    SB271("SB.2.7-1", "Current Activity is not defined / Sequencing session has not begun"),
    SB272("SB.2.7-2", "Flow Sequencing Control Mode violation"),
    SB281("SB.2.8-1", "Current Activity is not defined / Sequencing session has not begun"),
    SB282("SB.2.8-2", "Flow Sequencing Control Mode violation"),
    SB291("SB.2.9-1", "No target for Choice"),
    SB292("SB.2.9-2", "Target activity does not exist or is unavailable"),
    SB293("SB.2.9-3", "Target activity hidden from choice"),
    SB294("SB.2.9-4", "Choice Sequencing Control Mode violation"),
    SB295("SB.2.9-5", "No activities to consider"),
    SB296("SB.2.9-6", "Unable to activate target; target is not a child of the Current Activity"),
    SB297("SB.2.9-7", "Choice Exit Sequencing Control Mode violation"),
    SB298("SB.2.9-8", "Unable to choose target activity – constrained choice"),
    SB299("SB.2.9-9", "Choice request prevented by Flow-only activity"),
    SB2101("SB.2.10-1", "Current Activity is not defined / Sequencing session has not begun"),
    SB2102("SB.2.10-2", "Current Activity is active or suspended"),
    SB2103("SB.2.10-3", "Flow Sequencing Control Mode violation"),
    SB2111("SB.2.11-1", "Current Activity is not defined / Sequencing session has not begun"),
    SB2112("SB.2.11-2", "Current Activity has not been terminated"),
    SB2121("SB.2.12-1", "Undefined sequencing request"),
    SB2131("SB.2.13-1", "Current Activity is not defined / Sequencing session has not begun"),
    DB111("DB.1.1-1", "Cannot deliver a non-leaf activity"),
    DB112("DB.1.1-2", "Nothing to deliver"),
    DB113("DB.1.1-3", "Activity unavailable"),
    DB21("DB.2-1", "Identified activity is already active");

    private final String code;
    private final String description;

    SequencingException(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
