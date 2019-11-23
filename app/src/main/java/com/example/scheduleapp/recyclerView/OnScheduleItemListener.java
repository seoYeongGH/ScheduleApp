package com.example.scheduleapp.recyclerView;

import android.view.View;

public interface OnScheduleItemListener {
    public void onItemClick(ScheduleAdapter.ViewHolder holder, View view, int position);
}
