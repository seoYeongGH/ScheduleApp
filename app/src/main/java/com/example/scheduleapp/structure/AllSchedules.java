package com.example.scheduleapp.structure;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllSchedules {
   private static AllSchedules instance;
    private List<ScheduleObject> allSchedules;

    private int selectDayIdx = 1;
    private int selectObjIdx = -1;

    public static AllSchedules getInstance(){
        if(instance == null)
            instance = new AllSchedules();
        return instance;
    }
    private AllSchedules(){}

    public List<ScheduleObject> getAllSchedules() {
        return allSchedules;
    }

    public int getScheduleSize(int index){
        return allSchedules.get(index).getSchedules().size();
    }
    public void setAllSchedules(List<ScheduleObject> allSchedules) {
        this.allSchedules = allSchedules;
    }

    public int getSize(){
        return allSchedules.size();
    }

    public ScheduleObject getSchedule(int position){
        ScheduleObject obj = allSchedules.get(position);
        return obj;
    }

    public int getSelectDayIdx() {
        return selectDayIdx;
    }

    public void setSelectDayIdx(int selectDayIdx) {
        this.selectDayIdx = selectDayIdx;
    }

    public int getSelectObjIdx() {
        return selectObjIdx;
    }

    public void setSelectObjIdx(int selectObjIdx) {
        this.selectObjIdx = selectObjIdx;
    }

    public void addSchedule(boolean isFirst, int dateIdx, HashMap hashMap){
        String date = hashMap.get("date").toString();
        String startTime = hashMap.get("startTime").toString();
        String endTime = hashMap.get("endTime").toString();
        String schedule = hashMap.get("schedule").toString();

        if(isFirst){
            ScheduleObject scheduleObj = new ScheduleObject();
            scheduleObj.setDate(date);

            ArrayList<String> tempList = new ArrayList<>();
            tempList.add(startTime);
            scheduleObj.setStartTime(tempList);

            tempList = new ArrayList<>();
            tempList.add(endTime);
            scheduleObj.setEndTime(tempList);

            tempList = new ArrayList<>();
            tempList.add(schedule);
            scheduleObj.setSchedule(tempList);

            allSchedules.add(dateIdx,scheduleObj);
        }
        else{
            ArrayList<String> startTimes = allSchedules.get(dateIdx).getStartTimes();

            int i;
            int scheduleSize = startTimes.size();
            for(i=0; i<scheduleSize; i++){
                if(startTime.compareTo(startTimes.get(i))<0){
                    break;
                }
                else if(startTime.equals(startTimes.get(i))){
                    ArrayList<String> endTimes = allSchedules.get(dateIdx).getEndTimes();

                    do{
                        if(endTime.compareTo(endTimes.get(i))<=0)
                            break;
                        i++;
                    }while(startTime.equals(startTimes.get(i)));

                    break;
                }
            }

            allSchedules.get(dateIdx).getStartTimes().add(i,startTime);
            allSchedules.get(dateIdx).getEndTimes().add(i,endTime);
            allSchedules.get(dateIdx).getSchedules().add(i,schedule);
        }
    }
    public void deleteSchedule(int dateIdx,int scheduleIdx){
        if(allSchedules.get(dateIdx).getScheduleSize() == 1) {
            allSchedules.remove(dateIdx);
        }
        else {
            allSchedules.get(dateIdx).deleteSchedule(scheduleIdx);
        }
    }

    public void modifySchedule(int dateIdx,  int scheduleIdx, HashMap hashMap){
        allSchedules.get(dateIdx).getStartTimes().set(scheduleIdx,hashMap.get("aftStartTime").toString());
        allSchedules.get(dateIdx).getEndTimes().set(scheduleIdx,hashMap.get("aftEndTime").toString());
        allSchedules.get(dateIdx).getSchedules().set(scheduleIdx,hashMap.get("aftSchedule").toString());
    }
}
