package com.example.scheduleapp.structure;

import java.util.ArrayList;

public class AllInvites {
    private static AllInvites instance;

    private ArrayList<InviteObject> invites;
    public static AllInvites getInstance(){
        if(instance == null)
            instance = new AllInvites();

        return instance;
    }

    private AllInvites(){
        invites = new ArrayList<>();
    }

    public ArrayList<InviteObject> getInvites() {
        return invites;
    }

    public void setInvites(ArrayList<InviteObject> invites) {
        this.invites = invites;
    }

    public boolean isEmpty(){
        return invites.isEmpty();
    }
}
