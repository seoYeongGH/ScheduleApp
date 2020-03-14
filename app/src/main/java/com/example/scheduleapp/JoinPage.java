package com.example.scheduleapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
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

import static com.example.scheduleapp.structure.Constant.ERR_CHK_ID;
import static com.example.scheduleapp.structure.Constant.ERR_NULL;
import static com.example.scheduleapp.structure.Constant.SUCCESS;
import static com.example.scheduleapp.structure.Constant.DUP_ID;
import static com.example.scheduleapp.structure.Constant.DUP_USER;

public class JoinPage extends AppCompatActivity {
    private EditText iptName;
    private EditText iptId;
    private EditText iptPw;
    private EditText iptChkPw;
    private EditText iptEmail;
    private TextView warningPw;

    private boolean chkId = false;
    private String strChkId;
    private int warnPwCode = -1;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_activity);

        iptName = findViewById(R.id.iptName);
        iptId = findViewById(R.id.iptId);
        iptPw = findViewById(R.id.iptPw);
        iptChkPw = findViewById(R.id.iptChkPw);
        iptEmail = findViewById(R.id.iptEmail);
        warningPw  =findViewById(R.id.txtWarnPw);

        TextWatcher pwWatcher = getWatcher(iptChkPw);
        iptPw.addTextChangedListener(pwWatcher);

        TextWatcher chkWatcher = getWatcher(iptPw);
        iptChkPw.addTextChangedListener(chkWatcher);

        alertDialog = makeAlert();
    }

    public void onBtnChkIdClicked(View view){
        TextView warnId = findViewById(R.id.txtWarnId);

        String strWarning;
        String id = iptId.getText().toString();
        int len = id.length();

        if(len == 0){
            strChkId = "";

            strWarning = "ID를 입력하세요.";
            warnId.setText(strWarning);
            warnId.setTextSize(15);

            return;
        }
        if(chkRange(id,len)){
            chkId = true;
            strChkId = id;

            warnId.setTextSize(0);
        }
        else{
            chkId = false;
            strChkId = "";

            strWarning = "ID는 영문자와 숫자만 사용가능합니다.";
            warnId.setText(strWarning);
            warnId.setTextSize(15);

            return ;
        }

        if(chkId){
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("doing","chkId");
            hashMap.put("id",id);

            doChkId(hashMap);
        }
    }

    public void onBtnJoinClicked(View view){
        String name = iptName.getText().toString();
        String id = iptId.getText().toString();
        String pw = iptPw.getText().toString();
        String chkPw = iptChkPw.getText().toString();
        String email = iptEmail.getText().toString();

        if(email.length()==0 || name.length()==0|| pw.length()==0 || chkPw.length()==0) {
            showAlert(ERR_NULL);
            return;
        }

        if( !chkId || !id.equals(strChkId)){
            showAlert(ERR_CHK_ID);
            return;
        }

        if(warnPwCode == 2){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("doing","join");
            hashMap.put("id",id);
            hashMap.put("password",pw);
            hashMap.put("name",name);
            hashMap.put("email",email);

            doCommunication(hashMap);
        }
    }

    private void showAlert(int code){
        String msg;

        switch(code){
            case SUCCESS: msg = "사용가능한 ID입니다."; break;
            case ERR_NULL: msg = "모든 항목을 입력해야합니다."; break;
            case DUP_ID: msg = "이미 존재하는 아이디 입니다."; break;
            case DUP_USER: msg = "이미 가입되어있는 회원입니다."; break;
            case ERR_CHK_ID: msg = "ID 중복확인을 하세요."; break;
            default: msg = "ERR!!"; break;
        }

        alertDialog.setMessage(msg);
        alertDialog.show();
    }

    private AlertDialog makeAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notice");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return builder.create();
    }

    private void doChkId(HashMap<String, Object> hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.get_doService(hashMap);
        doService.enqueue(new Callback<Integer>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && (response.body()!=null)){
                        showAlert(response.body());
                }
                else{
                    Log.d("JOIN_ERR","Join Retrofit Err");
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("ERRRR","JOIN_ERR");
            }
        });
    }

    private void doCommunication(HashMap<String, String> hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);
        doService.enqueue(new Callback<Integer>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && (response.body()!=null)){
                        canJoin(response.body());

                }
                else{
                    Log.d("JOIN_ERR","Join Retrofit Err");
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("ERRRR","JOIN_ERR");
            }
        });
    }

    private void canJoin(int code){
        switch(code){
            case SUCCESS: Toast.makeText(this, "JOIN SUCCESS!!", Toast.LENGTH_LONG).show();
                finish(); break;
            case DUP_ID: code = ERR_CHK_ID;
            case DUP_USER:
            default: showAlert(code); break;
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
                    if(warnPwCode != 1) {
                        warningPw.setTextColor(Color.parseColor("#B90000"));
                        warningPw.setText("비밀번호가 일치하지 않습니다.");
                        warnPwCode = 1;
                    }
                }
                else{
                    if(warnPwCode != 2){
                        warningPw.setTextColor(Color.parseColor("#0C7900"));
                        warningPw.setText("사용할 수 있는 비밀번호입니다.");

                        warnPwCode = 2;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String pw = editable.toString();

                if(!chkRange(pw,pw.length())){
                    warningPw.setTextColor(Color.parseColor("#B90000"));
                    warningPw.setText("비밀번호는 영문자와 숫자만 사용가능합니다.");
                }
            }
        };

    }

    private boolean chkRange(String str, int len){
        char ch;
        for(int i=0; i<len; i++) {
            ch = str.charAt(i);

            if (!(ch >= 'A' && ch <= 'z') && !(ch >= '0' && ch <= '9'))
                return false;
        }

        return true;
    }

    public void onBtnCancelClicked(View view){
        finish();
    }

}
