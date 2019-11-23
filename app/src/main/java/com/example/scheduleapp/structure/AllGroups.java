package com.example.scheduleapp.structure;

import java.util.ArrayList;

public class AllGroups {
    public static AllGroups instance;

    public ArrayList<GroupObject> groups;
    public boolean isInit = false;

    private AllGroups(){groups = new ArrayList<GroupObject>();}

    public static AllGroups getInstance(){
        if(instance == null)
            instance = new AllGroups();
        return instance;
    }

    public void addGroup(GroupObject obj){
        GroupObject gObj = obj;
        String groupName = gObj.getGroupName();
        int size = groups.size();
        int i=0;

        if(gObj.isManager){
            for(i=0; i<size; i++){
                GroupObject tempObj = groups.get(i);
                if(tempObj.groupName.compareTo(groupName)>0 || !tempObj.isManager())
                    break;
            }
        }
        else{
            for(i=0; i<size; i++){
                GroupObject tempObj = groups.get(i);
                if(!tempObj.isManager() && tempObj.getGroupName().compareTo(groupName)>0)
                    break;
            }
        }

        groups.add(i,gObj);
    }

    public void setGroups(ArrayList<GroupObject> groups) {
        this.groups = groups;
    }

    public ArrayList<GroupObject> getGroups(){
        return groups;
    }

    public void setInit(boolean init){
        isInit = init;
    }
}
