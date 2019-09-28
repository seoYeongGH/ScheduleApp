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
    USession session;

    ConstraintLayout beforeLogin;
    LinearLayout afterLogin;
    LinearLayout txtBefore;
    LinearLayout txtAfter;
    TextView txtViewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = USession.getInstance();

        beforeLogin = findViewById(R.id.beforeLogin);
        afterLogin = findViewById(R.id.afterLogin);

        txtBefore = findViewById(R.id.txtBefore);
        txtAfter = findViewById(R.id.txtAfter);

        txtViewId = findViewById(R.id.txtViewId);

        View txtLayout = findViewById(R.id.txtBefore);
        onTxtClicked(txtLayout);

        setDisplay();
    }

    private void setDisplay(){
        if(!session.getIsLogin()){
            beforeLogin.setVisibility(View.VISIBLE);
            afterLogin.setVisibility(View.INVISIBLE);

            txtBefore.setVisibility(View.VISIBLE);
            txtAfter.setVisibility(View.INVISIBLE);

            txtViewId.setText("로그인 후 이용하세요.");
        }
        else{
            beforeLogin.setVisibility(View.INVISIBLE);
            afterLogin.setVisibility(View.VISIBLE);

            txtBefore.setVisibility(View.INVISIBLE);
            txtAfter.setVisibility(View.VISIBLE);

            txtViewId.setText(USession.getInstance().getId()+"님의 일정입니다.");
        }
    }

    public void onTxtClicked(View view){
       TextView txtLogin = findViewById(R.id.txtLogin);
       TextView txtJoin = findViewById(R.id.txtJoin);
       TextView txtMypage = findViewById(R.id.txtMypage);
       TextView txtLogout = findViewById(R.id.txtLogout);

       txtLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(),LoginPage.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

               startActivity(intent);
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
