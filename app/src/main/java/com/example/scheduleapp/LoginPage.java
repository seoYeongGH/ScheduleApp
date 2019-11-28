package com.example.scheduleapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.USession;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.scheduleapp.structure.Constant.ERR_LOG_ID;
import static com.example.scheduleapp.structure.Constant.ERR_LOG_PW;
import static com.example.scheduleapp.structure.Constant.SUCCESS;

public class LoginPage extends AppCompatActivity {
    private EditText iptId;
    private EditText iptPw;
    protected TextView warnLogin;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        iptId = findViewById(R.id.loginId);
        iptPw = findViewById(R.id.loginPw);

        warnLogin = findViewById(R.id.txtWarnLogin);

        alertDialog = makeAlert();

        TextView txtJoin = findViewById(R.id.txtJoin);
        txtJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),JoinPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        TextView txtFind = findViewById(R.id.txtFind);
        txtFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FindPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    public void onBtnLoginClicked(View view){
        String id = iptId.getText().toString();
        String pw = iptPw.getText().toString();

        if(id.length()==0 || pw.length()==0)
            warnLogin.setTextSize(15);
        else {
            warnLogin.setTextSize(0);

            HashMap hashMap = new HashMap();
            hashMap.put("doing","login");
            hashMap.put("id",id);
            hashMap.put("password",pw);

            doCommunication(hashMap);
        }
    }

    private void doCommunication(final HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);

        doService.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    doLogin(hashMap.get("id").toString(),response.body().intValue());
                }
                else{
                    Log.d("Login_ERR","Login Retrofit Err");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void getGroupNums(){
        HashMap hashMap = new HashMap();
        hashMap.put("doing","getGroupNums");

        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<ArrayList<Integer>> getService = userService.getGroupNums(hashMap);
        getService.enqueue(new Callback<ArrayList<Integer>>() {
            @Override
            public void onResponse(Call<ArrayList<Integer>> call, Response<ArrayList<Integer>> response) {
                if(response.isSuccessful())
                    USession.getInstance().setConnectGroups(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Integer>> call, Throwable t) {

            }
        });
    }
    private void doLogin(String id, int code){
        switch(code){
            case SUCCESS: USession.getInstance().setId(id);
                          USession.getInstance().setIsLogin(true);
                          getGroupNums();

                          Intent intent = new Intent(this, MainActivity.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                          startActivity(intent);
                          break;
            case ERR_LOG_ID:
            case ERR_LOG_PW: showAlert(code); break;
            default: showAlert(0); break;
        }
    }
    private void showAlert(int code){
        String msg;

        switch(code){
            case ERR_LOG_ID: msg = "존재하지 않는 ID입니다."; break;
            case ERR_LOG_PW: msg = "잘못된 비밀번호입니다."; break;
            default: msg = "ERR!!"; break;
        }

        alertDialog.setMessage(msg);
        alertDialog.show();

        return ;
    }

    private AlertDialog makeAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sorry :(");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
