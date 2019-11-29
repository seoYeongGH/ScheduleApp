package com.example.scheduleapp.structure;

import java.util.ArrayList;

public class AllGroups {
    public static AllGroups instance;

    public ArrayList<GroupObject> isManagers;
    public ArrayList<GroupObject> notManagers;
    public boolean isInit = false;

    private AllGroups(){
        isManagers = new ArrayList<GroupObject>();
        notManagers = new ArrayList<GroupObject>();
    }

    public static AllGroups getInstance(){
        if(instance == null)
            instance = new AllGroups();
        return instance;
    }

    public void addManagerGroup(GroupObject obj){
        String groupName = obj.getGroupName();
        int size = isManagers.size();
        int i=0;

            for(i=0; i<size; i++){
                GroupObject tempObj = isManagers.get(i);
                if(tempObj.groupName.compareTo(groupName)>0)
                    break;
            }
        isManagers.add(i,obj);
    }


    public void addMemberGroup(GroupObject obj){
        String groupName = obj.getGroupName();
        int size = notManagers.size();
        int i=0;

        for(i=0; i<size; i++){
            GroupObject tempObj = notManagers.get(i);
            if(tempObj.groupName.compareTo(groupName)>0)
                break;
        }
        notManagers.add(i,obj);
    }

    public boolean isManagerGroup(int groupNum){
        int size = isManagers.size();
        for(int i=0; i<size; i++){
            if(groupNum == isManagers.get(i).getGroupNum())
                return true;
        }

        return false;
    }

    public ArrayList<GroupObject> getIsManagers() {
        return isManagers;
    }

    public void setIsManagers(ArrayList<GroupObject> isManagers) {
        this.isManagers = isManagers;
    }

    public ArrayList<GroupObject> getNotManagers() {
        return notManagers;
    }

    public void setNotManagers(ArrayList<GroupObject> notManagers) {
        this.notManagers = notManagers;
    }

    public void setInit(boolean init){
        isInit = init;
    }
}
