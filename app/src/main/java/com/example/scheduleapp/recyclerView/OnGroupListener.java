package com.example.scheduleapp.recyclerView;

import android.view.View;

public interface OnGroupListener {
    void onItemClicked(GroupAdapter.ViewHolder holder, int position);
    void onBtnDeleteClicked(GroupAdapter.ViewHolder holder, View view, int position);
    void onBtnMemberClicked(GroupAdapter.ViewHolder holder, View view, int position);
}
