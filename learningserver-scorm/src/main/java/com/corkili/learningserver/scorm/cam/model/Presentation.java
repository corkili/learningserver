package com.corkili.learningserver.scorm.cam.model;

public class Presentation {

    private NavigationInterface navigationInterface; // 0...1

    public Presentation() {
    }

    public NavigationInterface getNavigationInterface() {
        return navigationInterface;
    }

    public void setNavigationInterface(NavigationInterface navigationInterface) {
        this.navigationInterface = navigationInterface;
    }
}
