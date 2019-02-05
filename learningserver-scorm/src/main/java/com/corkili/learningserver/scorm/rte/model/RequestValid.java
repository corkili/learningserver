package com.corkili.learningserver.scorm.rte.model;

import java.util.Arrays;
import java.util.HashSet;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.datatype.StateReadOnlyMap;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;

public class RequestValid implements GeneralDataType {

    @Meta("continue")
    private State continueValid;

    @Meta("previous")
    private State previous;

    @Meta("choice")
    private StateReadOnlyMap choice;

    @Meta("jump")
    private StateReadOnlyMap jump;

    public RequestValid() {
        this.continueValid = new State("unknown", new HashSet<>(Arrays.asList("true", "false", "unknown")));
        this.previous = new State("unknown", new HashSet<>(Arrays.asList("true", "false", "unknown")));
        this.choice = new StateReadOnlyMap("true", "false", "unknown");
        this.jump = new StateReadOnlyMap("true", "false", "unknown");
        registerHandler();
    }

    private void registerHandler() {
        continueValid.registerSetHandler(new ReadOnlyHandler());
        previous.registerSetHandler(new ReadOnlyHandler());
    }
}
