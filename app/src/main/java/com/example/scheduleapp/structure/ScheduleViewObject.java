package com.example.scheduleapp.structure;

public class ScheduleViewObject {
    public String schedule;
    public String time;

    public ScheduleViewObject(String schedule, String time) {
        this.schedule = schedule;
        this.time = time;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
