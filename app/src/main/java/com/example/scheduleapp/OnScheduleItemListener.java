package com.example.scheduleapp;

import android.content.Context;
import android.view.View;

public interface OnScheduleItemListener {
    public void onItemClick(ScheduleAdapter.ViewHolder holder, View view, int position);
}
