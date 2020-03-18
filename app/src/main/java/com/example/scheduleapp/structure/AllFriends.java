package com.example.scheduleapp.structure;

import java.util.ArrayList;

public class AllFriends {
    private static AllFriends instance;
    private ArrayList<FriendObject> friends;

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
        int i;
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

    public void setFriends(ArrayList<FriendObject> friends) {
        this.friends = friends;
    }
    public ArrayList<FriendObject> getFriends() {
        return friends;
    }
}
