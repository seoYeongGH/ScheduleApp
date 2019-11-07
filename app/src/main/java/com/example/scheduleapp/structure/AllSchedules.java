package com.example.scheduleapp.structure;

import java.util.ArrayList;

public class AllSchedules {
    ArrayList<ScheduleObject> allSchedules;

    public AllSchedules(ArrayList<ScheduleObject> allSchedules) {
        super();
        this.allSchedules = allSchedules;
    }

    public ArrayList<ScheduleObject> getAllSchedules() {
        return allSchedules;
    }

    public void setAllSchedules(ArrayList<ScheduleObject> allSchedules) {
        this.allSchedules = allSchedules;
    }


}
