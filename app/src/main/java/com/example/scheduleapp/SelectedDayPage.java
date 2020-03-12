package com.example.scheduleapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleapp.recyclerView.OnScheduleItemListener;
import com.example.scheduleapp.recyclerView.ScheduleAdapter;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.ScheduleService;
import com.example.scheduleapp.structure.AllSchedules;
import com.example.scheduleapp.structure.ScheduleObject;
import com.example.scheduleapp.structure.ScheduleViewObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.scheduleapp.structure.Constant.CODE_ADD;
import static com.example.scheduleapp.structure.Constant.CODE_ISCHANGED;
import static com.example.scheduleapp.structure.Constant.CODE_MODIFY;
import static com.example.scheduleapp.structure.Constant.CODE_NOTCHANGED;
import static com.example.scheduleapp.structure.Constant.DELETE_SUCCESS;
import static com.example.scheduleapp.structure.Constant.ERR;
import static com.example.scheduleapp.structure.Constant.FLAG_ADD;
import static com.example.scheduleapp.structure.Constant.FLAG_MODIFY;

public class SelectedDayPage extends AppCompatActivity {
    String strDate;
    int groupNum;
    private boolean haveSchedule;
    private int dateIdx;
    ScheduleObject schedules;

    RecyclerView recSchedules;
    ScheduleAdapter scheduleAdapter;

    boolean isChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_day);

        recSchedules = findViewById(R.id.recSchedules);

        Intent getIntent = getIntent();

        groupNum = getIntent.getIntExtra("groupNum",-1);

        Button btnAdd = findViewById(R.id.btnAdd);
        if(!getIntent.getBooleanExtra("isManager",true))
            btnAdd.setVisibility(View.INVISIBLE);

        strDate = getIntent.getStringExtra("date");
        TextView txtDate = findViewById(R.id.txtDate);
        txtDate.setText(strDate);

        haveSchedule = getIntent.getBooleanExtra("haveSchedule",false);
        dateIdx = getIntent.getIntExtra("scheduleIdx", ERR);

        isChanged = false;
    }

    public void onResume(){
        super.onResume();

        setView(false);
    }

    public boolean dispatchTouchEvent(MotionEvent me){
        Rect dialogBounds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBounds);

        if(!dialogBounds.contains((int)me.getX(),(int)me.getY()))
            return false;

        return super.dispatchTouchEvent(me);
    }

    public void onBackPressed() {
        if(isChanged)
            setResult(CODE_ISCHANGED);
        else
            setResult(CODE_NOTCHANGED);

        finish();
    }

    private void setView(boolean isDeleted){
        if(haveSchedule) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recSchedules.setLayoutManager(layoutManager);

            if (!isDeleted) {
                scheduleAdapter = new ScheduleAdapter();
                schedules = AllSchedules.getInstance().getSchedule(dateIdx);
                int schSize = schedules.getScheduleSize();

                for (int i = 0; i < schSize; i++) {
                    String startTime = schedules.getStartTimes().get(i);
                    String endTime = schedules.getEndTimes().get(i);

                    ScheduleViewObject schObj = new ScheduleViewObject(schedules.getSchedules().get(i), startTime + "~" + endTime);
                    scheduleAdapter.addItem(schObj);
                }
                scheduleAdapter.setListener(new OnScheduleItemListener() {
                    @Override
                    public void doModify(int position) {
                        setModify(position);
                    }

                    @Override
                    public void doDelete(int position) {
                        confirmDelete(position);
                    }
                });
                recSchedules.setAdapter(scheduleAdapter);
                recSchedules.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
            }
            else{
                recSchedules.setAdapter(null);
            }
        }
    }

    public void onBtnExitClicked(View view){
        if(isChanged)
            setResult(CODE_ISCHANGED);
        else
            setResult(CODE_NOTCHANGED);

        finish();
    }

    public void onBtnAddClicked(View view){
        Intent intent = new Intent(getApplicationContext(), SInputPage.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("groupNum", groupNum);
        intent.putExtra("flag", FLAG_ADD);
        intent.putExtra("date", strDate);
        intent.putExtra("haveSchedule",haveSchedule);
        intent.putExtra("dateIdx",dateIdx);
        startActivityForResult(intent,CODE_ADD);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == CODE_ISCHANGED) {
            isChanged  = true;
            haveSchedule = true;
        }
    }


    private void setModify(int position){
        Intent intent = new Intent(getApplicationContext(), SInputPage.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("flag", FLAG_MODIFY);
        intent.putExtra("groupNum",groupNum);
        intent.putExtra("date",strDate);
        intent.putExtra("schedule",schedules.getSchedules().get(position));
        intent.putExtra("startTime",schedules.getStartTimes().get(position));
        intent.putExtra("endTime",schedules.getEndTimes().get(position));
        intent.putExtra("dateIdx",dateIdx);
        intent.putExtra("scheduleIdx",position);
        startActivityForResult(intent,CODE_MODIFY);
    }

    private void confirmDelete(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notice");

        builder.setMessage("일정 '"+schedules.getSchedules().get(position)+"'를 정말 삭제하시겠습니까?");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setDelete(position,schedules.getScheduleSize());
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setDelete(final int position, final int scheduleSize){
        HashMap hashMap = new HashMap<>();
        hashMap.put("doing","deleteSchedule");
        hashMap.put("date", AllSchedules.getInstance().getSchedule(dateIdx).getDate());
        hashMap.put("schedule",schedules.getSchedules().get(position));
        hashMap.put("startTime",schedules.getStartTimes().get(position));
        hashMap.put("endTime",schedules.getEndTimes().get(position));
        hashMap.put("groupNum",groupNum);

        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        ScheduleService scheduleService = retrofit.create(ScheduleService.class);

        Call<Integer> doService = scheduleService.doService(hashMap);

        doService.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    if(response.body() == DELETE_SUCCESS) {
                        AllSchedules.getInstance().deleteSchedule(dateIdx,position);
                        Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();

                        isChanged = true;
                        Log.d("CHKCHK","DELTE:"+isChanged);
                        if(scheduleSize == 1) {
                            setView(true);
                            haveSchedule = false;
                        }else{
                            setView(false);
                        }

                    }
                }
                else{
                    Log.d("SCHEDULES_ERR","Input Schedule Retrofit Err");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
            }
        });
    }
}
