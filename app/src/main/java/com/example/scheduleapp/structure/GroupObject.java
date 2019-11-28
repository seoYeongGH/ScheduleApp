package com.example.scheduleapp.structure;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class GroupObject extends JSONObject {
    @SerializedName("groupNum")
    int groupNum;

    @SerializedName("groupName")
    String groupName;

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
}
