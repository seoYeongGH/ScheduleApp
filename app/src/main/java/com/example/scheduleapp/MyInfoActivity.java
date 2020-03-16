package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

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
import retrofit2.internal.EverythingIsNonNull;

public class MyInfoActivity extends AppCompatActivity {
    TextView txtName;
    TextView txtId;
    TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        txtName = findViewById(R.id.txtName);
        txtId = findViewById(R.id.txtId);
        txtEmail = findViewById(R.id.txtEmail);


        txtId.setText(USession.getInstance().getId());
        getCommunication();
    }

    public void onBtnExitClicked(View view){
        finish();
    }

    private void getCommunication (){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        HashMap hashMap = new HashMap();
        hashMap.put("doing","getInfo");

        Call<HashMap<String,String>> getService = userService.getService(hashMap);

        getService.enqueue(new Callback<HashMap<String,String>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<HashMap<String,String>> call, Response<HashMap<String,String>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    txtName.setText(response.body().get("name"));
                    txtEmail.setText(response.body().get("email"));
                }
                else{
                    Log.d("INFO_ERR","Info Retrofit Err");
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<HashMap<String,String>> call, Throwable t) {
                Log.d("ERRRRR",t.getMessage());
            }
        });
    }
}
