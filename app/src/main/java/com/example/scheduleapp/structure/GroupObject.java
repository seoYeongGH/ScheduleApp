package com.example.scheduleapp.structure;

import org.json.JSONObject;

public class GroupObject extends JSONObject {
    int groupNum;
    String groupName;
    boolean isManager;

    public GroupObject(){ }

    public int getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
