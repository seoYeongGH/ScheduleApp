package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import static com.example.scheduleapp.structure.Constant.SUCCESS;

public class ChangePwPage extends AppCompatActivity {
    EditText iptNewPw;
    EditText iptChkNew;

    TextView txtWarnNew;
    TextView txtWarmNull;

    private int warnPwCode = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pw_activity);

        iptNewPw = findViewById(R.id.iptNewPw);
        iptChkNew = findViewById(R.id.iptChkNew);

        txtWarnNew = findViewById(R.id.txtNewPw);
        txtWarmNull = findViewById(R.id.txtWarnNull);

        TextWatcher newPwWatcher = getWatcher(iptChkNew);
        iptNewPw.addTextChangedListener(newPwWatcher);

        TextWatcher newChkWatcher = getWatcher(iptNewPw);
        iptChkNew.addTextChangedListener(newChkWatcher);
    }

    public void onBtnExitClicked(View view){
        finish();
    }

    public void onBtnOkClicked(View view){
        if(iptNewPw.getText().length()==0 || iptChkNew.getText().length()==0){
            txtWarmNull.setTextSize(15);
            return;
        }
        else{
            txtWarmNull.setTextSize(0);
        }

        if(warnPwCode == 0){
            String newPw = iptNewPw.getText().toString();

            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("doing","changePw");
            hashMap.put("newPw",newPw);

            doCommunication(hashMap);
        }
    }

    public void onBtnCancelClicked(View view){
        finish();
    }

    private void doCommunication(final HashMap<String,String> hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);

        doService.enqueue(new Callback<Integer>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && response.body()!=null)
                    processCode(response.body());
                else
                    Log.d("CHANGE_PW_ERR","Change Pw Retrofit Err");

            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void processCode(int code){
            if(code == SUCCESS){
                Toast.makeText(this, "비밀번호가 변경되었습니다.",Toast.LENGTH_LONG).show();
                finish();
            }
            else{
                Toast.makeText(this, "Error!!",Toast.LENGTH_LONG).show();

            }

    }

    private TextWatcher getWatcher(final EditText editText){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String comparePw = editText.getText().toString();

                if(!charSequence.toString().equals(comparePw)){
                    if(warnPwCode != 1){
                        txtWarnNew.setTextSize(15);
                        warnPwCode = 1;
                    }
                }
                else{
                    txtWarnNew.setTextSize(0);
                    warnPwCode = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }
}
