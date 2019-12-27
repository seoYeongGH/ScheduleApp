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
import com.example.scheduleapp.structure.AllFriends;
import com.example.scheduleapp.structure.AllGroups;
import com.example.scheduleapp.structure.FriendObject;
import com.example.scheduleapp.structure.GroupObject;
import com.example.scheduleapp.structure.USession;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

import static com.example.scheduleapp.structure.Constant.ERR_LOG_ID;
import static com.example.scheduleapp.structure.Constant.ERR_LOG_PW;
import static com.example.scheduleapp.structure.Constant.SUCCESS;

public class LoginPage extends AppCompatActivity {
    private EditText iptId;
    private EditText iptPw;
    private TextView warnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        iptId = findViewById(R.id.loginId);
        iptPw = findViewById(R.id.loginPw);
        warnLogin = findViewById(R.id.txtWarnLogin);

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

            loginCommunication(hashMap);
        }
    }

    private void loginCommunication(final HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);
        doService.enqueue(new Callback<Integer>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    doLogin(hashMap.get("id").toString(),response.body().intValue());
                }
                else{
                    Log.d("Login_ERR","Login Retrofit Err");
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("ERRRR","LOGIN_RETRO_ERR");
            }
        });
    }

    private void getGroupNums(){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        HashMap hashMap = new HashMap();
        hashMap.put("doing","getLinkGroups");

        Call<ArrayList<Integer>> getGroups = userService.getLinkGroups(hashMap);
        getGroups.enqueue(new Callback<ArrayList<Integer>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ArrayList<Integer>> call, Response<ArrayList<Integer>> response) {
                if(response.isSuccessful())
                    USession.getInstance().setConnectGroups(response.body());
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ArrayList<Integer>> call, Throwable t) {
                Log.d("ERRRR","GET_LINK_ERR");
            }
        });
    }

    private void getGroups(){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        HashMap hashMap = new HashMap();
        hashMap.put("doing","getGroups");

        Call<HashMap<String, ArrayList<GroupObject>>> getGroup = userService.getGroup(hashMap);
        getGroup.enqueue(new Callback<HashMap<String,ArrayList<GroupObject>>>() {
            @Override
            public void onResponse(Call<HashMap<String,ArrayList<GroupObject>>> call, Response<HashMap<String,ArrayList<GroupObject>>> response) {
                HashMap getHash = response.body();

                AllGroups.getInstance().setIsManagers((ArrayList<GroupObject>)getHash.get("isManager"));
                AllGroups.getInstance().setNotManagers((ArrayList<GroupObject>)getHash.get("notManager"));
            }

            @Override
            public void onFailure(Call<HashMap<String,ArrayList<GroupObject>>> call, Throwable t) {

            }
        });
    }


    private void getFriends(){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        HashMap hashMap = new HashMap();
        hashMap.put("doing","getFriends");

        Call<ArrayList<FriendObject>> getFriend = userService.getFriend(hashMap);
        getFriend.enqueue(new Callback<ArrayList<FriendObject>>() {
            @Override
            public void onResponse(Call<ArrayList<FriendObject>> call, Response<ArrayList<FriendObject>> response) {
                AllFriends.getInstance().setFriends(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<FriendObject>> call, Throwable t) {

            }
        });
    }


    private void getName(){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        HashMap hashMap = new HashMap();
        hashMap.put("doing","getName");

        Call<String> getName = userService.getName(hashMap);
        getName.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful())
                    USession.getInstance().setName(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("GET_NAME_ERR","GET_NAME_EXCEPTION");
            }
        });
    }

    private void doLogin(String id, int code){
        AlertDialog alertDialog = makeAlert();

        switch(code){
            case SUCCESS: getDatas(id);

                          Intent intent = new Intent(this, MainActivity.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                          startActivity(intent);
                          break;
            case ERR_LOG_ID:
            case ERR_LOG_PW: showAlert(alertDialog,code); break;
            default: showAlert(alertDialog,0); break;
        }
    }

    public void getDatas(String id){
        USession.getInstance().setId(id);
        USession.getInstance().setIsLogin(true);

        getName();
        getGroupNums();
        getGroups();
        getFriends();
    }

    private AlertDialog makeAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sorry :(");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        return builder.create();
    }

    private void showAlert(AlertDialog alertDialog, int code){
        String msg;

        switch(code){
            case ERR_LOG_ID: msg = "존재하지 않는 ID입니다."; break;
            case ERR_LOG_PW: msg = "잘못된 비밀번호입니다."; break;
            default: msg = "ERR!!"; break;
        }
        alertDialog.setMessage(msg);
        alertDialog.show();
    }
}
