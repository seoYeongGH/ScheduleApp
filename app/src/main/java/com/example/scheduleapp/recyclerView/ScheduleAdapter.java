package com.example.scheduleapp.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleapp.R;
import com.example.scheduleapp.structure.ScheduleViewObject;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> implements OnScheduleItemListener{
    private OnScheduleItemListener listener;
    Context context;

    ArrayList<ScheduleViewObject> schedules  = new ArrayList<ScheduleViewObject>();

    public ScheduleAdapter(Context context){
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtSchedule;
        TextView txtTime;

        public ViewHolder(View itemView, final OnScheduleItemListener listener){
            super(itemView);
            txtSchedule = itemView.findViewById(R.id.txtSchedule);
            txtTime = itemView.findViewById(R.id.txtTime);

            itemView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    int position = getAdapterPosition();
                    if(listener != null)
                        listener.onItemClick(ViewHolder.this, v, position);
                }
            });
        }

        public void setItem(ScheduleViewObject schedule){
            txtSchedule.setText(schedule.getSchedule());
            txtTime.setText(schedule.getTime());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View scheduleView = inflater.inflate(R.layout.two_textview,parent,false);

        return new ViewHolder(scheduleView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScheduleViewObject schedule = schedules.get(position);
        holder.setItem(schedule);
    }

    @Override
    public int getItemCount() {

        return schedules.size();
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null)
            listener.onItemClick(holder, view, position);
    }

    public void setListener(OnScheduleItemListener listener){
        this.listener = listener;
    }

    public void addItem(ScheduleViewObject schedule){
        schedules.add(schedule);
    }

    public void setItems(ArrayList<ScheduleViewObject> schedules){
        this.schedules = schedules;
    }

    public ScheduleViewObject getItem(int position){
        return schedules.get(position);
    }

    public void setItem(int position,ScheduleViewObject schedule){
        schedules.set(position, schedule);
    }

    public void clearList(){
        if(schedules != null)
            schedules.clear();
    }
}
