package com.example.scheduleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleapp.fragment.AfterLoginFragment;
import com.example.scheduleapp.recyclerView.OnScheduleItemListener;
import com.example.scheduleapp.recyclerView.ScheduleAdapter;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.ScheduleService;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.AllSchedules;
import com.example.scheduleapp.structure.ScheduleObject;
import com.example.scheduleapp.structure.ScheduleViewObject;
import com.example.scheduleapp.structure.USession;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.scheduleapp.structure.Constant.CODE_MODIFY;
import static com.example.scheduleapp.structure.Constant.DOT_COLOR;
import static com.example.scheduleapp.structure.Constant.FLAG_ADD;
import static com.example.scheduleapp.structure.Constant.FLAG_ADD_GROUP;
import static com.example.scheduleapp.structure.Constant.FLAG_MODIFY;
import static com.example.scheduleapp.structure.Constant.SUCCESS;

public class GroupSchedulePage extends AppCompatActivity {
    Button btnConnect;
    Button btnDisConnect;

    int groupNum;
    boolean isConnect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_schedule_activity);

        Intent getIntent = getIntent();

        TextView txtGroup = findViewById(R.id.txtGroup);
        txtGroup.setPaintFlags(txtGroup.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);

        groupNum = getIntent.getIntExtra("groupNum",-1);

        btnConnect = findViewById(R.id.btnConnect);
        btnDisConnect = findViewById(R.id.btnDisconnect);

        ArrayList<Integer> groupNums = USession.getInstance().getConnectGroups();
        int groupSize = groupNums.size();
        isConnect = false;

        for(int i=0; i<groupSize; i++){
            if(groupNums.get(i)==groupNum) {
                isConnect = true;
            }
        }
        setBtnView(isConnect);

        txtGroup.setText(getIntent.getStringExtra("groupName"));

        AfterLoginFragment fragment  = new AfterLoginFragment(groupNum);
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
    }

    private void setBtnView(boolean isConnect){
        if(isConnect){
            btnConnect.setVisibility(View.GONE);
            btnDisConnect.setVisibility(View.VISIBLE);
        }
        else{
            btnConnect.setVisibility(View.VISIBLE);
            btnDisConnect.setVisibility(View.GONE);
        }

    }

    public void onBtnConnect(View view){
        HashMap hashMap = new HashMap();
        hashMap.put("doing","connectGroup");
        hashMap.put("groupNum",groupNum);

        doCommunication(hashMap);
    }

    public void onBtnDisconnect(View view){
        HashMap hashMap = new HashMap();
        hashMap.put("doing","disconnectGroup");
        hashMap.put("groupNum",groupNum);

        doCommunication(hashMap);
    }
    private void doCommunication(final HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.get_doService(hashMap);

        doService.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    if(response.body() == SUCCESS) {
                        isConnect = !isConnect;
                        setBtnView(isConnect);
                        if (isConnect)
                            USession.getInstance().addGroup(groupNum);
                        else
                            USession.getInstance().removeGroup(groupNum);
                    }
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
}
