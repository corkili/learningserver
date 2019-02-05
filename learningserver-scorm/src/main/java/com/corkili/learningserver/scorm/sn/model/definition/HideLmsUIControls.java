package com.corkili.learningserver.scorm.sn.model.definition;

public class HideLmsUIControls {

    private boolean hidePrevious;
    private boolean hideContinue;
    private boolean hideExit;
    private boolean hideExitAll;
    private boolean hideAbandon;
    private boolean hideAbandonAll;
    private boolean hideSuspendAll;

    public HideLmsUIControls() {
        hidePrevious = false;
        hideContinue = false;
        hideExit = false;
        hideExitAll = false;
        hideAbandon = false;
        hideAbandonAll = false;
        hideSuspendAll = false;
    }

    public boolean isHidePrevious() {
        return hidePrevious;
    }

    public HideLmsUIControls setHidePrevious(boolean hidePrevious) {
        this.hidePrevious = hidePrevious;
        return this;
    }

    public boolean isHideContinue() {
        return hideContinue;
    }

    public HideLmsUIControls setHideContinue(boolean hideContinue) {
        this.hideContinue = hideContinue;
        return this;
    }

    public boolean isHideExit() {
        return hideExit;
    }

    public HideLmsUIControls setHideExit(boolean hideExit) {
        this.hideExit = hideExit;
        return this;
    }

    public boolean isHideExitAll() {
        return hideExitAll;
    }

    public HideLmsUIControls setHideExitAll(boolean hideExitAll) {
        this.hideExitAll = hideExitAll;
        return this;
    }

    public boolean isHideAbandon() {
        return hideAbandon;
    }

    public HideLmsUIControls setHideAbandon(boolean hideAbandon) {
        this.hideAbandon = hideAbandon;
        return this;
    }

    public boolean isHideAbandonAll() {
        return hideAbandonAll;
    }

    public HideLmsUIControls setHideAbandonAll(boolean hideAbandonAll) {
        this.hideAbandonAll = hideAbandonAll;
        return this;
    }

    public boolean isHideSuspendAll() {
        return hideSuspendAll;
    }

    public HideLmsUIControls setHideSuspendAll(boolean hideSuspendAll) {
        this.hideSuspendAll = hideSuspendAll;
        return this;
    }
}
