package com.example.scheduleapp.structure;

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
        return allSchedules.get(index).getSchedule().size();
    }
    public void setAllSchedules(List<ScheduleObject> allSchedules) {
        this.allSchedules = allSchedules;
    }

    public int getSize(){
        return allSchedules.size();
    }

    public ScheduleObject getSchedule(int position){
        return allSchedules.get(position);
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
}
