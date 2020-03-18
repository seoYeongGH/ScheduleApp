package com.example.scheduleapp.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllSchedules {
    private static AllSchedules instance;
    private List<ScheduleObject> allSchedules;

    private AllSchedules(){}

    public static AllSchedules getInstance(){
        if(instance == null)
            instance = new AllSchedules();
        return instance;
    }

    public void setAllSchedules(List<ScheduleObject> allSchedules) {
        this.allSchedules = allSchedules;
    }

    public List<ScheduleObject> getAllSchedules() {
        return allSchedules;
    }

    public ScheduleObject getSchedule(int position){
        return allSchedules.get(position);
    }

    public int getSize(){
        return allSchedules.size();
    }

    public void addSchedule(boolean isFirst, int dateIdx, HashMap hashMap){
        String date = String.valueOf(hashMap.get("date"));
        String startTime = String.valueOf(hashMap.get("startTime"));
        String endTime = String.valueOf(hashMap.get("endTime"));
        String schedule = String.valueOf(hashMap.get("schedule"));

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
        allSchedules.get(dateIdx).getStartTimes().set(scheduleIdx, String.valueOf(hashMap.get("aftStartTime")));
        allSchedules.get(dateIdx).getEndTimes().set(scheduleIdx, String.valueOf(hashMap.get("aftEndTime")));
        allSchedules.get(dateIdx).getSchedules().set(scheduleIdx, String.valueOf(hashMap.get("aftSchedule")));
    }
}
