package com.example.scheduleapp.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleapp.R;
import com.example.scheduleapp.SInputPage;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.ScheduleService;
import com.example.scheduleapp.structure.AllSchedules;
import com.example.scheduleapp.structure.ScheduleViewObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.scheduleapp.structure.Constant.DELETE_SUCCESS;
import static com.example.scheduleapp.structure.Constant.FLAG_MODIFY;
import static com.example.scheduleapp.structure.Constant.ID_DELETE;
import static com.example.scheduleapp.structure.Constant.ID_MODIFY;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{
    private static OnScheduleItemListener listener;
    static Context context;
    static int dateIdx;

    ArrayList<ScheduleViewObject> schedules  = new ArrayList<ScheduleViewObject>();

    public ScheduleAdapter(Context context, int dateIdx){
        this.context = context;
        this.dateIdx = dateIdx;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView txtSchedule;
        TextView txtTime;

        public ViewHolder(View itemView){
            super(itemView);
            txtSchedule = itemView.findViewById(R.id.txtSchedule);
            txtTime = itemView.findViewById(R.id.txtTime);

            itemView.setOnCreateContextMenuListener(this);
        }

        public void setItem(ScheduleViewObject schedule){
            txtSchedule.setText(schedule.getSchedule());
            txtTime.setText(schedule.getTime());
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem edit = contextMenu.add(Menu.NONE,ID_MODIFY,0,"수정");
            MenuItem delete = contextMenu.add(Menu.NONE,ID_DELETE,1,"삭제");
            edit.setOnMenuItemClickListener(OnMenuItemClickListener);
            delete.setOnMenuItemClickListener(OnMenuItemClickListener);
        }

        private final MenuItem.OnMenuItemClickListener OnMenuItemClickListener = new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case ID_MODIFY:
                        listener.doModify(getAdapterPosition());
                        return true;

                    case ID_DELETE:
                        listener.doDelete(getAdapterPosition());
                        return true;
                }
                return false;
            }
        };

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View scheduleView = inflater.inflate(R.layout.two_textview,parent,false);

        return new ViewHolder(scheduleView);
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
