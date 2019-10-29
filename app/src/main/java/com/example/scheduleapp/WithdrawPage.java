package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.USession;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.scheduleapp.structure.Constant.ERR_LOG_PW;
import static com.example.scheduleapp.structure.Constant.SUCCESS;

public class WithdrawPage extends AppCompatActivity {
    EditText iptPw;
    TextView txtWarn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdraw_activity);

        iptPw = findViewById(R.id.iptPw);
        txtWarn = findViewById(R.id.txtWarn);
    }

    public void onBtnOkClicked(View view) {
        String strPw = iptPw.getText().toString();

        if (strPw.length() == 0) {
            txtWarn.setTextSize(15);
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put("doing", "withdraw");
            hashMap.put("password", strPw);

            doCommunication(hashMap);
        }
    }

    public void onBtnCancelClicked(View view) {
        finish();
    }

    private void doCommunication(final HashMap hashMap) {
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);

        doService.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    processCode(response.body().intValue());
                } else {
                    Log.d("Login_ERR", "Login Retrofit Err");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void processCode(int code) {
        if (code == ERR_LOG_PW) {
            txtWarn.setTextSize(15);
            return;
        } else {
            txtWarn.setTextSize(0);

            if (code == SUCCESS) {
                Toast.makeText(this, "탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show();

                USession.getInstance().setIsLogin(false);
                USession.getInstance().setId(null);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error!!", Toast.LENGTH_LONG).show();

            }
        }
    }
}