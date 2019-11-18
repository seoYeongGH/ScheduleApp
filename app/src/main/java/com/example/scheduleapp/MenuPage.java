package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.USession;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MenuPage extends AppCompatActivity{
    TextView txtSocial;
    TextView txtInfo;
    TextView txtChangePw;
    TextView txtInfos;
    TextView txtWithdraw;

    private String id;
    private boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        id = USession.getInstance().getId();
        txtSocial = findViewById(R.id.txtSocial);
        txtChangePw = findViewById(R.id.txtChangePw);
        txtInfo = findViewById(R.id.txtInfo);
        txtInfos = findViewById(R.id.txtInfos);
        txtWithdraw = findViewById(R.id.txtWithdraw);

        HashMap hashMap = new HashMap();
        hashMap.put("doing","getInfo");
        hashMap.put("id",id);

        getCommunication(hashMap);

        isShow = false;
        setTextEvent();
    }

    private void setTextEvent(){
        txtSocial.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SocialPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        txtInfo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(isShow)
                    txtInfos.setTextSize(0);
                else
                    txtInfos.setTextSize(20);

                isShow = !isShow;
            }
        });
        txtChangePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ChangePwPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        txtWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),WithdrawPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

    }

    private void getCommunication (HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<HashMap> getService = userService.getService(hashMap);

        getService.enqueue(new Callback<HashMap>() {
            @Override
            public void onResponse(Call<HashMap> call, Response<HashMap> response) {
                if(response.isSuccessful()){
                    Log.d("DODO","START");
                    HashMap hashInfo =  response.body();
                    txtInfos.setText("My Info\n\nID: "+id+"\n이름: "+hashInfo.get("name")+"\nE-mail: "+hashInfo.get("email"));
                }
                else{
                    Log.d("INFO_ERR","Info Retrofit Err");
                }
            }

            @Override
            public void onFailure(Call<HashMap> call, Throwable t) {
                Log.d("ERRRRR",t.getMessage());
            }
        });
    }
}
