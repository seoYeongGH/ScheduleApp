package com.example.scheduleapp.structure;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleStruct {
    LocalDate date;
    LocalTime startTime;
    LocalTime endTime;
    String schedule;

    public ScheduleStruct(){}

    public ScheduleStruct(LocalDate date, LocalTime startTime, LocalTime endTime, String schedule) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.schedule = schedule;
    }

    public ScheduleStruct(String strDate, String strStartH, String strStartM, String strEndH, String strEndM, String strSchedule){
        String[] dates = strDate.split("-");
        date = LocalDate.of(Integer.parseInt(dates[0]),Integer.parseInt(dates[1]),Integer.parseInt(dates[2]));

        startTime = LocalTime.of(Integer.parseInt(strStartH),Integer.parseInt(strStartM));
        endTime = LocalTime.of(Integer.parseInt(strEndH),Integer.parseInt(strEndM));

        schedule = strSchedule;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDate(String strDate){
        String[] dates = strDate.split("-");
        date = LocalDate.of(Integer.parseInt(dates[0]),Integer.parseInt(dates[1]),Integer.parseInt(dates[2]));
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(String strHour, String strMinute){
        startTime = LocalTime.of(Integer.parseInt(strHour),Integer.parseInt(strMinute));
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(String strHour, String strMinute){
        endTime = LocalTime.of(Integer.parseInt(strHour),Integer.parseInt(strMinute));
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
