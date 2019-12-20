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

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.scheduleapp.structure.Constant.ERR;
import static com.example.scheduleapp.structure.Constant.NO_DATA;
import static com.example.scheduleapp.structure.Constant.SUCCESS;

public class FindPage extends AppCompatActivity {
    EditText iptName;
    EditText iptEmail;

    TextView txtWarn;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_activity);

        iptName = findViewById(R.id.iptName);
        iptEmail = findViewById(R.id.iptEmail);

        txtWarn = findViewById(R.id.txtWarn);
    }

    public void onBtnExitClicked(View view){
        finish();
    }

    public void onBtnFindClicked(View view){
        String name = iptName.getText().toString();
        String email = iptEmail.getText().toString();

        if(name.length()==0 || email.length()==0){
            txtWarn.setTextSize(15);
            return ;
        }

        txtWarn.setTextSize(0);

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("doing","findInfo");
        hashMap.put("name",name);
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
            case SUCCESS: msg = "입력한 메일로 정보를 전송했습니다.\n메일을 확인해 주세요 :)"; break;
            case NO_DATA: msg = "일치하는 정보가 없습니다."; break;
            case ERR: msg = "Error가 발생했습니다. 다시 시도해주세요."; break;
            default: msg = "ERR!!"; break;
        }

        alertDialog = makeAlert(code);

        alertDialog.setMessage(msg);
        alertDialog.show();

        return ;
    }

    private AlertDialog makeAlert(final int code){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notice");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(code == SUCCESS) {
                    finish();

                    Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    startActivity(intent);
                }
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }
}