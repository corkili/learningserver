package com.corkili.learningserver.scorm.sn.model.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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

    public boolean isAvailable(Activity activity) {
        if (isRoot(activity)) {
            return true;
        }
        return activity.getParentActivity().getActivityStateInformation().getAvailableChildren().contains(activity);
    }

    public List<Activity> preorder() {
        List<Activity> list = new ArrayList<>();
        preorder(list, root);
        return Collections.unmodifiableList(list);
    }

    private void preorder(List<Activity> resultList, Activity node) {
        if (node != null) {
            resultList.add(node);
            for (int i = 0; i < node.getChildren().size(); i++) {
                preorder(resultList, node.getChildren().get(i));
            }
        }
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

    public static List<Activity> getActivitySequenceList(Activity from, boolean includeFrom, Activity to, boolean includeTo) {
        if (from == null || to == null || !from.isSiblingActivity(to)
                || from.getParentActivity() == null || to.getParentActivity() == null) {
            return Collections.emptyList();
        }
        Activity parent = from.getParentActivity();
        int indexOfFrom = parent.getChildren().indexOf(from);
        int indexOfTo = parent.getChildren().indexOf(to);
        List<Activity> list = new LinkedList<>();
        if (indexOfFrom < indexOfTo) {
            if (!includeFrom) {
                indexOfFrom++;
            }
            if (!includeTo) {
                indexOfTo--;
            }
            if (indexOfFrom >= indexOfTo) {
                return Collections.emptyList();
            }
            for (int i = indexOfFrom; i <= indexOfTo; i++) {
                list.add(parent.getChildren().get(i));
            }
        } else {
            if (!includeFrom) {
                indexOfFrom--;
            }
            if (!includeTo) {
                indexOfTo++;
            }
            if (indexOfTo >=indexOfFrom) {
                return Collections.emptyList();
            }
            for (int i = indexOfFrom; i >= indexOfTo; i--) {
                list.add(parent.getChildren().get(i));
            }
        }
        return list;
    }
}
