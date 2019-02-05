package com.corkili.learningserver.scorm.sn.api.event;

import com.sun.istack.internal.NotNull;

import com.corkili.learningserver.scorm.sn.api.request.NavigationRequest;
import com.corkili.learningserver.scorm.sn.api.request.NavigationRequest.Type;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class EventTranslator {

    public static NavigationRequest translateEventToRequestType(@NotNull NavigationEvent event,
                                                                ActivityTree activityTree, Activity activity) {
        return new NavigationRequest(getRequestType(event), activityTree, activity);
    }

    private static NavigationRequest.Type getRequestType(NavigationEvent event) {
        if (event == null) {
            throw new IllegalArgumentException();
        }
        switch (event.getType()) {
            case Start:
                return Type.Start;
            case ResumeAll:
                return Type.ResumeAll;
            case Continue:
                return Type.Continue;
            case Previous:
                return Type.Previous;
            case Choose:
                return Type.Choice;
            case Jump:
                return Type.Jump;
            case Abandon:
                return Type.Abandon;
            case AbandonAll:
                return Type.AbandonAll;
            case SuspendAll:
                return Type.SuspendAll;
            case UnqualifiedExit:
                return Type.Exit;
            case ExitAll:
                return Type.ExitAll;
        }
        throw new IllegalArgumentException();
    }

}
