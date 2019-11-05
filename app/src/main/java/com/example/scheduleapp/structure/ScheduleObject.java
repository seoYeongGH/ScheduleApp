package com.example.scheduleapp.structure;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ScheduleObject {
    @SerializedName("date")
    LocalDate date;

    @SerializedName("startTimes")
    ArrayList<String> startTimes;

    @SerializedName("endTimes")
    ArrayList<String> endTimes;

    @SerializedName("schedules")
    ArrayList<String> schedules;

    public ScheduleObject(){}

    public ScheduleObject(LocalDate date,  ArrayList<String> startTimes,  ArrayList<String> endTimes,  ArrayList<String> schedules) {
        this.date = date;
        this.startTimes = startTimes;
        this.endTimes = endTimes;
        this.schedules = schedules;
    }

    public LocalDate getDate() {
        return date;
    }

    public  ArrayList<String> getStartTime() {
        return startTimes;
    }

    public  ArrayList<String> getEndTime() {
        return endTimes;
    }

    public  ArrayList<String> getSchedule() {
        return schedules;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

}
