package com.corkili.learningserver.scorm.common;

public class LMSPersistDriverManager {

    private static LMSPersistDriverManager instance;

    private LMSPersistDriver driver;

    private LMSPersistDriverManager() {

    }

    public static LMSPersistDriverManager getInstance() {
        if (instance == null) {
            synchronized (LMSPersistDriverManager.class) {
                if (instance == null) {
                    instance = new LMSPersistDriverManager();
                }
            }
        }
        return instance;
    }

    public void registerDriver(LMSPersistDriver lmsPersistDriver) {
        this.driver = lmsPersistDriver;
    }

    public LMSPersistDriver getDriver() {
        return driver;
    }
}
