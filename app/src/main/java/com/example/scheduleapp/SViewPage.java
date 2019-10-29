package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static com.example.scheduleapp.Constant.ADD_SCHEDULE;

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

    public void onBtnAddClicked(View view){
        Intent intent = new Intent(getApplicationContext(),SInputPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("flag",ADD_SCHEDULE);

        startActivity(intent);
    }
}
