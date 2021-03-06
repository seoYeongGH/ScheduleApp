package com.example.scheduleapp.structure;

import android.util.Log;

import com.example.scheduleapp.fragment.AfterLoginFragment;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScheduleObject extends JSONObject{
    @SerializedName("scheduledate")
    String scheduledate;

    @SerializedName("startTimes")
    ArrayList<String> startTimes;

    @SerializedName("endTimes")
    ArrayList<String> endTimes;

    @SerializedName("schedules")
    ArrayList<String> schedules;

    public ScheduleObject(){}

    public int getScheduleSize(){
        return schedules.size();
    }

    public String getDate() {
        return scheduledate;
    }

    public  ArrayList<String> getStartTimes() {
        return startTimes;
    }

    public  ArrayList<String> getEndTimes() {
        return endTimes;
    }

    public  ArrayList<String> getSchedules() {
        return schedules;
    }

    public void setDate(String date) {
        this.scheduledate = date;
    }

    public void setStartTime( ArrayList<String> startTime) {
        this.startTimes = startTime;
    }

    public void setEndTime( ArrayList<String> endTime) {
        this.endTimes = endTime;
    }

    public void setSchedule( ArrayList<String> schedule) {
        this.schedules = schedule;
    }

    public void deleteSchedule(int scheduleIdx){
            startTimes.remove(scheduleIdx);
            endTimes.remove(scheduleIdx);
            schedules.remove(scheduleIdx);
    }
}
