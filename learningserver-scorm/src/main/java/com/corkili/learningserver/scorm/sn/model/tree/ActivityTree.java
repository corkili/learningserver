package com.corkili.learningserver.scorm.sn.model.tree;

import java.util.Objects;
import java.util.Stack;

import com.sun.istack.internal.NotNull;

import com.corkili.learningserver.scorm.sn.model.tracking.GlobalStateInformation;

public class ActivityTree {

    private String id;
    private boolean objectivesGlobalToSystem; // SN-3-39 3.10.2
    private Activity root;
    private final GlobalStateInformation globalStateInformation;

    public ActivityTree() {
        objectivesGlobalToSystem = true;
        globalStateInformation = new GlobalStateInformation();
    }

    public boolean isObjectivesGlobalToSystem() {
        return objectivesGlobalToSystem;
    }

    public void setObjectivesGlobalToSystem(boolean objectivesGlobalToSystem) {
        this.objectivesGlobalToSystem = objectivesGlobalToSystem;
    }

    public Activity getRoot() {
        return root;
    }

    public void setRoot(Activity root) {
        this.root = root;
    }

    public GlobalStateInformation getGlobalStateInformation() {
        return globalStateInformation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRoot(Activity activity) {
        return activity.getParentActivity() == null && Objects.equals(activity, root);
    }

    public boolean existActivity(@NotNull Activity activity) {
        if (root == null) {
            return false;
        }
        return existActivity(root, activity);
    }

    public boolean existActivity(@NotNull Activity activity, @NotNull Activity target) {
        if (activity.equals(target)) {
            return true;
        }
        for (Activity child : activity.getChildren()) {
            if (existActivity(child, target)) {
                return true;
            }
        }
        return false;
    }

    public Activity findCommonAncestorFor(Activity oneActivity, Activity twoActivity) {
        if (!existActivity(oneActivity) || !existActivity(twoActivity)) {
            return null;
        }
        if (isRoot(oneActivity) || isRoot(twoActivity)) {
            return root;
        }
        if (oneActivity.isSiblingActivity(twoActivity)) {
            return oneActivity.getParentActivity();
        }
        Stack<Activity> oneStack = new Stack<>();
        Stack<Activity> twoStack = new Stack<>();
        oneStack.push(oneActivity.getParentActivity());
        twoStack.push(twoActivity.getParentActivity());
        while (oneStack.peek().getParentActivity() != null) {
            oneStack.push(oneStack.peek().getParentActivity());
        }
        while (twoStack.peek().getParentActivity() != null) {
            twoStack.push(twoStack.peek().getParentActivity());
        }
        Activity commonAncestor = null;
        while (!oneStack.isEmpty() && !twoStack.isEmpty() && oneStack.peek().equals(twoStack.peek())) {
            commonAncestor = oneStack.pop();
            twoStack.pop();
        }
        return commonAncestor;

    }
}
