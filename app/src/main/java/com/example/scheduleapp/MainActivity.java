package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout beforeLogin = findViewById(R.id.beforeLogin);
        LinearLayout afterLogin = findViewById(R.id.afterLogin);

        beforeLogin.setVisibility(View.VISIBLE);
        afterLogin.setVisibility(View.INVISIBLE);

        LinearLayout txtBefore = findViewById(R.id.txtBefore);
        LinearLayout txtAfter = findViewById(R.id.txtAfter);

        txtBefore.setVisibility(View.VISIBLE);
        txtAfter.setVisibility(View.INVISIBLE);

        View txtLayout = findViewById(R.id.txtBefore);
        onTxtClicked(txtLayout);
    }

    public void onTxtClicked(View view){
       TextView txtLogin = findViewById(R.id.txtLogin);
       TextView txtJoin = findViewById(R.id.txtJoin);
       TextView txtMypage = findViewById(R.id.txtMypage);
       TextView txtLogout = findViewById(R.id.txtLogout);

       txtLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });

        txtJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),JoinPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);
            }
        });

        txtMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
