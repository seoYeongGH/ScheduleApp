package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.scheduleapp.Retro.RetroController;
import com.example.scheduleapp.Retro.UserService;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyPage extends AppCompatActivity {
    TextView txtInfo;
    private boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity);

        txtInfo = findViewById(R.id.txtInfo);

        final String[] arrMenu = {"▶ 쪽지함","▶ 내가 작성한 글", "▶ 내 정보보기", "▶ 비밀번호 변경"};
        isShow = false;

        HashMap hashMap = new HashMap();
        hashMap.put("doing","getInfo");

        getCommunication(hashMap);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrMenu);

        ListView lstMenu = findViewById(R.id.lstMyMenu);
        lstMenu.setAdapter(adapter);

        lstMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==2){
                    if(isShow)
                        txtInfo.setTextSize(0);
                    else
                        txtInfo.setTextSize(18);

                    isShow = !isShow;
                }
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
                    Log.d("GETINFO","ID: "+hashInfo.get("id")+"\n이름; "+hashInfo.get("name")+"\nE-mail: "+hashInfo.get("email"));
                    txtInfo.setText("ID: "+hashInfo.get("id")+"\n이름; "+hashInfo.get("name")+"\nE-mail: "+hashInfo.get("email"));
                }
                else{
                    Log.d("INFO_ERR","Info Retrofit Err");
                }
            }

            @Override
            public void onFailure(Call<HashMap> call, Throwable t) {

            }
        });
    }
}
