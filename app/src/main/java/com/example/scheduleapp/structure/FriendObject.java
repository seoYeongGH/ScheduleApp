package com.example.scheduleapp.structure;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class FriendObject extends JSONObject {
    @SerializedName("name")
    String name;
    @SerializedName("id")
    String id;

    public FriendObject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
