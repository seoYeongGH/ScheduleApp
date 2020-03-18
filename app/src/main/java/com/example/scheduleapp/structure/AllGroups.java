package com.example.scheduleapp.structure;

import java.util.ArrayList;

public class AllGroups {
    private static AllGroups instance;

    private ArrayList<GroupObject> isManagers;
    private ArrayList<GroupObject> notManagers;

    private AllGroups(){
        isManagers = new ArrayList<>();
        notManagers = new ArrayList<>();
    }

    public static AllGroups getInstance(){
        if(instance == null)
            instance = new AllGroups();

        return instance;
    }

    public void addManagerGroup(GroupObject obj){
        String groupName = obj.getGroupName();
        int size;

        if(isManagers != null) {
            size = isManagers.size();
        }
        else {
            size = 0;
            isManagers = new ArrayList<>();
        }

        int i;
        for(i=0; i<size; i++){
            GroupObject tempObj = isManagers.get(i);
            if(tempObj.groupName.compareTo(groupName)>0)
                break;
        }

        isManagers.add(i,obj);
    }


    public void addMemberGroup(GroupObject obj){
        String groupName = obj.getGroupName();
        int size;
        if(notManagers != null) {
            size = notManagers.size();
        }
        else {
            size = 0;
            notManagers = new ArrayList<>();
        }

        int i;
        for(i=0; i<size; i++){
            GroupObject tempObj = notManagers.get(i);
            if(tempObj.groupName.compareTo(groupName)>0)
                break;
        }
        notManagers.add(i,obj);
    }

    public void removeGroup(int position){
        if(position<isManagers.size())
            isManagers.remove(position);
        else
            notManagers.remove(position-isManagers.size());
    }

    public boolean isManagerGroup(int groupNum){
        int size = isManagers.size();
        for(int i=0; i<size; i++){
            if(groupNum == isManagers.get(i).getGroupNum())
                return true;
        }

        return false;
    }

    public void setIsManagers(ArrayList<GroupObject> isManagers) {
        this.isManagers = isManagers;
    }
    public void setNotManagers(ArrayList<GroupObject> notManagers) {
        this.notManagers = notManagers;
    }
    public ArrayList<GroupObject> getIsManagers() {
        return isManagers;
    }
    public ArrayList<GroupObject> getNotManagers() {
        return notManagers;
    }
}
