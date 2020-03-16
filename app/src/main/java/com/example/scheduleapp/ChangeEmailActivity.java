package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

public class ChangeEmailActivity extends AppCompatActivity {
    EditText iptEmail;
    TextView txtWarn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        iptEmail = findViewById(R.id.iptEmail);
        txtWarn = findViewById(R.id.txtWarn);
    }

    public void onBtnExitClicked(View view){
        finish();
    }

    public void onBtnOkClicked(View view){
        String strEmail = iptEmail.getText().toString();

        if(strEmail.length() != 0) {
            txtWarn.setTextSize(0);

            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("doing","changeEmail");
            hashMap.put("email",strEmail);

            doCommunication(hashMap);
        }
        else{
            txtWarn.setTextSize(16);
        }
    }


    private void doCommunication(final HashMap<String,String> hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);

        doService.enqueue(new Callback<Integer>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    Toast.makeText(ChangeEmailActivity.this, "이메일 주소가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Log.d("CHANGE_EMAIL_ERR","Change Email Retrofit Err");
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }
}
