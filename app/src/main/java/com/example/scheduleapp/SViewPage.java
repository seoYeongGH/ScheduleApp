package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SViewPage extends AppCompatActivity {
    String date = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sview_activity);

        Intent getIntent = getIntent();
        date = getIntent.getStringExtra("date");

        TextView txtDate = findViewById(R.id.txtDate);
        txtDate.setText(date);
    }
}
