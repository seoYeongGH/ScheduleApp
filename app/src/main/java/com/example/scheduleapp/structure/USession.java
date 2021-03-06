package com.example.scheduleapp.structure;

import java.util.ArrayList;

public class USession {
    private static USession instance;

    private String id;
    private String name;

    private boolean isLogin;
    private ArrayList<Integer> connectGroups;

    private USession(){
        isLogin = false;
        id = null;
        connectGroups = new ArrayList<>();
        }

    public static USession getInstance(){
        if(instance == null)
            instance = new USession();

        return instance;
    }

    public void removeGroup(int groupNum){
        int groupSize = connectGroups.size();
        for(int i=0; i<groupSize; i++){
            if(connectGroups.get(i) == groupNum) {
                connectGroups.remove(i);
                break;
            }
        }
    }

    public void setIsLogin(boolean login) {
        isLogin = login;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setConnectGroups(ArrayList<Integer> connectGroups) {
        this.connectGroups = connectGroups;
    }

    public boolean getIsLogin() {
        return isLogin;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public ArrayList<Integer> getConnectGroups() {
        return connectGroups;
    }

    public void addGroup(int groupNum){
        connectGroups.add(groupNum);
    }
}
