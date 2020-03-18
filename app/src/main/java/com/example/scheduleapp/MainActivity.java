package com.example.scheduleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.AllFriends;
import com.example.scheduleapp.structure.AllGroups;
import com.example.scheduleapp.structure.FriendObject;
import com.example.scheduleapp.structure.GroupObject;
import com.example.scheduleapp.structure.USession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

import static com.example.scheduleapp.structure.Constant.AUTO_LOG_SUCCESS;
import static com.example.scheduleapp.structure.Constant.ERR_AUTO_LOG_IN;
import static com.example.scheduleapp.structure.Constant.ERR_LOG_ID;
import static com.example.scheduleapp.structure.Constant.ERR_LOG_PW;
import static com.example.scheduleapp.structure.Constant.LOG_IN_SUCCESS;
import static com.example.scheduleapp.structure.Constant.SHARED_PREF_ISLOGIN;
import static com.example.scheduleapp.structure.Constant.SHARED_PREF_USER_CODE;

public class MainActivity extends AppCompatActivity {
    private EditText iptId;
    private EditText iptPw;
    private TextView warnLogin;

    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iptId = findViewById(R.id.loginId);
        iptPw = findViewById(R.id.loginPw);
        warnLogin = findViewById(R.id.txtWarnLogin);

        alertDialog = makeAlert();

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful() && task.getResult()!=null) {
                    String token = task.getResult().getToken();

                    SharedPreferences preferences = getSharedPreferences("schPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString(SHARED_PREF_USER_CODE, token);
                    editor.apply();
                }
                else{
                    Log.d("ERRRRR", "getInstanceId failed: "+task.getException());
                }

            }
        });
    }

    public void onResume(){
        super.onResume();

        SharedPreferences preferences = getSharedPreferences("schPref", Context.MODE_PRIVATE);
        if (preferences!=null && preferences.getBoolean(SHARED_PREF_ISLOGIN, false)) {
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("doing", "autoLogin");
            hashMap.put("userCode", preferences.getString(SHARED_PREF_USER_CODE, ""));

            loginCommunication(hashMap);
        }

    }

    @Override
    public void onBackPressed() {
        BackButton.getInstance().onBtnPressed(getApplicationContext(),this);
    }

    public void onBtnLoginClicked(View view){
        String id = iptId.getText().toString();
        String pw = iptPw.getText().toString();

        if(id.length()==0 || pw.length()==0) {
            warnLogin.setVisibility(View.VISIBLE);
        }
        else {
            warnLogin.setVisibility(View.GONE);

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("doing","login");
            hashMap.put("id",id);
            hashMap.put("password",pw);

            SharedPreferences preferences = getSharedPreferences("schPref",Context.MODE_PRIVATE);
            hashMap.put("userCode", preferences.getString(SHARED_PREF_USER_CODE,""));

            loginCommunication(hashMap);
        }
    }

    public void onBtnJoinClicked(View view){
        Intent intent = new Intent(getApplicationContext(), JoinPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
    }

    public void onBtnFindClicked(View view){
        Intent intent = new Intent(getApplicationContext(), FindPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
    }

    private void doLogin(String getId, int code){
        String id = "";

        switch(code){
            case LOG_IN_SUCCESS: id = iptId.getText().toString();

                SharedPreferences preferences = getSharedPreferences("schPref",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean(SHARED_PREF_ISLOGIN,true);
                editor.apply();

            case AUTO_LOG_SUCCESS: if(id.equals("")) id = getId;
                getDatas(id);

                Intent intent = new Intent(this, AfterLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case ERR_AUTO_LOG_IN: Toast.makeText(getApplicationContext(),"자동 로그인에 실패했습니다. :(", Toast.LENGTH_SHORT).show();
                                  break;

            case ERR_LOG_ID:
            case ERR_LOG_PW: showAlert(alertDialog,code); break;
            default: showAlert(alertDialog,0); break;
        }
    }

    private void loginCommunication(HashMap<String, Object> hashMap) {
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<HashMap<String,Object>> getService = userService.getService(hashMap);
        getService.enqueue(new Callback<HashMap<String,Object>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<HashMap<String,Object>> call, Response<HashMap<String,Object>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    double tmpCode = (double)response.body().get("code");
                    String tmpId = (String)response.body().get("id");

                    doLogin(tmpId, (int)tmpCode);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<HashMap<String,Object>> call, Throwable t) {

            }
        });
    }

    private void getGroupNums(){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<ArrayList<Integer>> getGroups = userService.get_getLinkGroups("getLinkGroups");
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

        Call<HashMap<String, ArrayList<GroupObject>>> getGroup = userService.get_getGroup("getGroups");
        getGroup.enqueue(new Callback<HashMap<String,ArrayList<GroupObject>>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<HashMap<String,ArrayList<GroupObject>>> call, Response<HashMap<String,ArrayList<GroupObject>>> response) {
                HashMap<String, ArrayList<GroupObject>> getHash = response.body();

                if(getHash != null) {
                    AllGroups.getInstance().setIsManagers(getHash.get("isManager"));
                    AllGroups.getInstance().setNotManagers(getHash.get("notManager"));
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<HashMap<String,ArrayList<GroupObject>>> call, Throwable t) {

            }
        });
    }

    private void getFriends(){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("doing","getFriends");

        Call<ArrayList<FriendObject>> getFriend = userService.getFriend(hashMap);
        getFriend.enqueue(new Callback<ArrayList<FriendObject>>() {

            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ArrayList<FriendObject>> call, Response<ArrayList<FriendObject>> response) {
                AllFriends.getInstance().setFriends(response.body());
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ArrayList<FriendObject>> call, Throwable t) {

            }
        });
    }

    private void getName(){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<String> getName = userService.getInfo("getName");
        getName.enqueue(new Callback<String>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful())
                    USession.getInstance().setName(response.body());
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("GET_NAME_ERR","GET_NAME_EXCEPTION");
            }
        });
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
