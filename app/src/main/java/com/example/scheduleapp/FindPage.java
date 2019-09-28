package com.example.scheduleapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.scheduleapp.Retro.RetroController;
import com.example.scheduleapp.Retro.UserService;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.scheduleapp.Constant.NO_DATA;
import static com.example.scheduleapp.Constant.SUCCESS;
import static com.example.scheduleapp.Constant.ERR;

public class FindPage extends AppCompatActivity {
    EditText findIdName;
    EditText findIdEmail;

    EditText findPwName;
    EditText findPwId;
    EditText findPwEmail;

    TextView warnId;
    TextView warnPw;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_activity);

        findIdName = findViewById(R.id.iptFindId1);
        findIdEmail = findViewById(R.id.iptFindId2);

        findPwName = findViewById(R.id.iptFindPw1);
        findPwId = findViewById(R.id.iptFindPw2);
        findPwEmail = findViewById(R.id.iptFindPw3);

        warnId = findViewById(R.id.warnFindId);
        warnPw = findViewById(R.id.warnFindPw);

        alertDialog = makeAlert();
    }

    public void onBtnFindIdClicked(View view){
        String name = findIdName.getText().toString();
        String email = findIdEmail.getText().toString();

        if(name.length()==0 || email.length()==0){
            warnId.setTextSize(15);
            return ;
        }

        warnId.setTextSize(0);

        HashMap hashMap = new HashMap();
        hashMap.put("doing","findId");
        hashMap.put("name",name);
        hashMap.put("email",email);

        doCommunication(hashMap);
    }


    public void onBtnFindPwClicked(View view){
        String name = findPwName.getText().toString();
        String id = findPwId.getText().toString();
        String email = findPwEmail.getText().toString();

        if(name.length()==0 || id.length()==0 || email.length()==0){
            warnPw.setTextSize(15);
            return ;
        }

        warnPw.setTextSize(0);

        HashMap hashMap = new HashMap();
        hashMap.put("doing","findPw");
        hashMap.put("name",name);
        hashMap.put("id",id);
        hashMap.put("email",email);

        doCommunication(hashMap);
    }

    private void doCommunication(final HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);

        doService.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    showAlert(response.body().intValue());
                }
                else{
                    Log.d("FIND_ERR","Find Retrofit Err");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void showAlert(int code){
        String msg;

        switch(code){
            case SUCCESS: msg = "입력한 메일로 정보를 전송했습니다!"; break;
            case NO_DATA: msg = "일치하는 정보가 없습니다."; break;
            case ERR: msg = "Error가 발생했습니다. 다시 시도해주세요."; break;
            default: msg = "ERR!!"; break;
        }

        alertDialog.setMessage(msg);
        alertDialog.show();

        return ;
    }

    private AlertDialog makeAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(":)");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }
}