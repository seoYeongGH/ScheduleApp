package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.scheduleapp.recyclerView.InviteAdapter;
import com.example.scheduleapp.structure.AllInvites;

public class InvitePage extends AppCompatActivity {
    RecyclerView recInvite;
    InviteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_activity);

        recInvite = findViewById(R.id.recInvite);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recInvite.setLayoutManager(layoutManager);

        adapter = new InviteAdapter();
        adapter.setItems(AllInvites.getInstance().getInvites());

        recInvite.setAdapter(adapter);
        recInvite.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
    }

}
