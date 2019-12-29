package com.example.scheduleapp.structure;

import android.util.Log;

import java.util.ArrayList;

public class AllFriends {
    public static AllFriends instance;

    public ArrayList<FriendObject> friends;
    public boolean isInit = false;

    private AllFriends(){}

    public static AllFriends getInstance(){
        if(instance==null)
            instance = new AllFriends();
        return instance;
    }

    public int getSize(){
        return friends.size();
    }

    public boolean getExist(String id){
        int size = friends.size();
        for(int i=0; i<size; i++){
            if(friends.get(i).getId().equals(id))
                return true;
        }

        return false;
    }
    public void addFriend(FriendObject obj){
        int i=0;
        int size = friends.size();

        for(i=0; i<size; i++){
            if(friends.get(i).getName().compareTo(obj.getName())>0)
                break;
        }

        friends.add(i,obj);
    }

    public void removeFriend(int position){
        friends.remove(position);
    }

    public ArrayList<FriendObject> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<FriendObject> friends) {
        this.friends = friends;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }
}
