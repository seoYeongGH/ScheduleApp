package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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

import static com.example.scheduleapp.structure.Constant.SUCCESS;

public class CheckPwActivity extends AppCompatActivity {
    EditText iptPw;
    TextView txtWarn;

    String doing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_check);

        doing = getIntent().getStringExtra("doing");

        iptPw = findViewById(R.id.iptPw);
        txtWarn = findViewById(R.id.txtWarn);
        TextView txtMessage = findViewById(R.id.txtMessage);

        if("changePw".equals(doing)){
            txtMessage.setText("비밀번호를 변경하시려면\n현재 비밀번호를 입력해주세요.");
        }
        else if("changeEmail".equals(doing))
            txtMessage.setText("이메일을 변경하시려면\n현재 비밀번호를 입력해주세요.");
        else if("withdraw".equals(doing)){
            txtMessage.setTextColor(Color.parseColor("#DD0101"));
            txtMessage.setText("탈퇴하시려면 비밀번호를 입력해주세요.");
        }
    }

    public void onBtnExitClicked(View view){
        finish();
    }

    public void onBtnOkClicked(View view){
        String strIptPw = iptPw.getText().toString();

        if(strIptPw.length() != 0) {
            txtWarn.setTextSize(0);

            HashMap hashMap = new HashMap();
            hashMap.put("doing", "chkPw");
            hashMap.put("password",strIptPw );

            doCommunication(hashMap);
        }
        else{
            txtWarn.setText("비밀번호를 입력해주세요.");
            txtWarn.setTextSize(16);
        }
    }

    private void moveToPage(){
        Intent intent = null;
        if("changePw".equals(doing))
            intent = new Intent(getApplicationContext(),ChangePwPage.class);
        else if("changeEmail".equals(doing))
            intent = new Intent(getApplicationContext(),ChangeEmailActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        finish();
        startActivity(intent);
    }

    private void doWithdraw(){
        HashMap hashMap = new HashMap();
        hashMap.put("doing","withdraw");

        doCommunication(hashMap);
    }

    private void doCommunication(final HashMap hashMap) {
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);
        doService.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    doProcess(hashMap.get("doing").toString(),response.body());
                } else {
                    Log.d("CHK_PW_ERR", "CHECK PW Retrofit Err");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void doProcess(String doingCom, int code){
        if("chkPw".equals(doingCom)){
            if(code == SUCCESS){
                if("withdraw".equals(doing))
                    doWithdraw();
                else
                    moveToPage();
            }
            else{
                txtWarn.setText("비밀번호가 일치하지 않습니다.");
                txtWarn.setTextSize(16);
            }
        }
        else if("withdraw".equals(doingCom)){
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
