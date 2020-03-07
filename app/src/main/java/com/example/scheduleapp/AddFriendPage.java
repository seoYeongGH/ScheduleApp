package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.ScheduleService;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.AllFriends;
import com.example.scheduleapp.structure.FriendObject;
import com.example.scheduleapp.structure.USession;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.scheduleapp.structure.Constant.CODE_ADD;
import static com.example.scheduleapp.structure.Constant.CODE_ISADDED;
import static com.example.scheduleapp.structure.Constant.ERR;
import static com.example.scheduleapp.structure.Constant.NO_DATA;
import static com.example.scheduleapp.structure.Constant.SUCCESS;

public class AddFriendPage extends AppCompatActivity {
    EditText iptName;
    EditText iptId;

    TextView txtWarn;
    TextView txtWarnExist;
    TextView txtInfo;

    LinearLayout layoutFInd;
    Button btnAdd;

    HashMap hashMap = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriend_activity);

        iptName = findViewById(R.id.iptName);
        iptId = findViewById(R.id.iptId);

        txtWarn = findViewById(R.id.txtWarn);
        txtWarnExist = findViewById(R.id.txtWarnExist);
        txtInfo = findViewById(R.id.txtInfo);

        layoutFInd = findViewById(R.id.layoutFind);
        btnAdd = findViewById(R.id.btnAdd);
    }

    public void onBtnFindClicked(View view){
        String strName = iptName.getText().toString();
        String strId = iptId.getText().toString();

        String msg;
        txtWarnExist.setTextSize(0);
        if(strName.length()==0 || strId.length()==0) {
            msg = "* 모든 항목을 입력해주세요.";

            txtWarn.setText(msg);
            txtWarn.setTextSize(16);
            return ;
        }

        if(strId.equals(USession.getInstance().getId())){
            msg = "* 본인의 ID는 검색할 수 없습니다 :)";

            txtWarn.setText(msg);
            txtWarn.setTextSize(16);
            return ;
        }

        txtWarn.setTextSize(0);

        hashMap.put("name",strName);
        hashMap.put("id",strId);
        hashMap.put("doing","findFriend");
        doCommunication(hashMap);
    }

    public void onBtnAddClicked(View view){
        FriendObject obj = new FriendObject();
        obj.setId(hashMap.get("id").toString());
        obj.setName(hashMap.get("name").toString());

        if(AllFriends.getInstance().getExist(obj.getId())) {
            txtWarnExist.setTextSize(15);
            return;
        }
        txtWarnExist.setTextSize(0);

        hashMap.put("doing","addFriend");
        doCommunication(hashMap);
    }

    public void onBtnCancelClicked(View view){
        finish();
    }

    private void doCommunication(final HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);
        doService.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                processCode(hashMap.get("doing").toString(),response.body());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    public void processCode(String doing, int code){
        if("findFriend".equals(doing)){
            switch(code){
                case SUCCESS: txtInfo.setTextSize(18);
                              txtInfo.setText("이름: "+hashMap.get("name")+"   ID: "+hashMap.get("id"));
                              btnAdd.setVisibility(View.VISIBLE);
                              layoutFInd.setVisibility(View.VISIBLE);
                              break;
                case NO_DATA: layoutFInd.setVisibility(View.VISIBLE);
                              txtInfo.setText("(일치하는 친구가 없습니다.)");
                              txtInfo.setTextSize(16);
                              btnAdd.setVisibility(View.INVISIBLE);
                              break;
            }
        }
        else if("addFriend".equals(doing)){
            if(code == SUCCESS){
                FriendObject obj = new FriendObject();
                obj.setName(hashMap.get("name").toString());
                obj.setId(hashMap.get("id").toString());

                AllFriends.getInstance().addFriend(obj);
                Toast.makeText(getApplicationContext(),"친구 추가 완료 :)",Toast.LENGTH_SHORT).show();

                setResult(CODE_ISADDED);
                finish();
            }
        }

        if(code == ERR)
            Toast.makeText(getApplicationContext(),"ERR!!",Toast.LENGTH_SHORT).show();
    }
}
