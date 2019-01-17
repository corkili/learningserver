package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

public class NavigationInterface {

    // elements
    private List<HideLMSUI> hideLMSUIList; // 0...n

    public NavigationInterface() {
        hideLMSUIList = new ArrayList<>();
    }

    public List<HideLMSUI> getHideLMSUIList() {
        return hideLMSUIList;
    }

    public void setHideLMSUIList(List<HideLMSUI> hideLMSUIList) {
        this.hideLMSUIList = hideLMSUIList;
    }
}
