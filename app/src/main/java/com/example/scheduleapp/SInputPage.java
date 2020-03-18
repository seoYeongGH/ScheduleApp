package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.ScheduleService;
import com.example.scheduleapp.structure.AllSchedules;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

import static com.example.scheduleapp.structure.Constant.ADD_SUCCESS;
import static com.example.scheduleapp.structure.Constant.CODE_ISCHANGED;
import static com.example.scheduleapp.structure.Constant.FLAG_ADD;
import static com.example.scheduleapp.structure.Constant.FLAG_MODIFY;
import static com.example.scheduleapp.structure.Constant.MOD_SUCCESS;

public class SInputPage extends AppCompatActivity {
    EditText iptSchedule;
    TimePicker startPicker;
    TimePicker endPicker;

    TextView txtWarn;

    private String date;
    private String flag;
    private boolean haveSchedule;
    private int dateIdx;
    private int groupNum = -1;
    private int scheduleIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sinput_activity);

        final Intent getIntent = getIntent();
        date = getIntent.getStringExtra("date");
        flag = getIntent.getStringExtra("flag");
        groupNum = getIntent.getIntExtra("groupNum",-1);
        haveSchedule = getIntent.getBooleanExtra("haveSchedule",false);
        dateIdx = getIntent.getIntExtra("dateIdx",-1);
        scheduleIdx = getIntent.getIntExtra("scheduleIdx",-1);

        Button btnSave = findViewById(R.id.btnSave);

        iptSchedule = findViewById(R.id.iptSchedule);
        startPicker = findViewById(R.id.startPicker);
        endPicker = findViewById(R.id.endPicker);

        txtWarn  = findViewById(R.id.txtWarn);

        TextView txtDate = findViewById(R.id.txtDate);
        txtDate.setText(date);

        if(FLAG_MODIFY.equals(flag))
            initModify(getIntent);

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String strSchedule = iptSchedule.getText().toString();

                String[] strTimes = new String[4];
                strTimes[0] = String.valueOf(startPicker.getHour());
                strTimes[1] = String.valueOf(startPicker.getMinute());
                strTimes[2] = String.valueOf(endPicker.getHour());
                strTimes[3] = String.valueOf(endPicker.getMinute());

                if(strSchedule.length()==0) {
                    txtWarn.setTextSize(15);
                    return;
                }

                for(int i=0; i<4; i++){
                    if(strTimes[i].length()==1)
                        strTimes[i] = "0"+strTimes[i];
                }
                txtWarn.setTextSize(0);

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("doing",flag);
                hashMap.put("date",date);
                hashMap.put("groupNum",groupNum);

                if(FLAG_ADD.equals(flag)){
                    hashMap.put("startTime",strTimes[0]+":"+strTimes[1]);
                    hashMap.put("endTime",strTimes[2]+":"+strTimes[3]);
                    hashMap.put("schedule",strSchedule);
                }
                else if(FLAG_MODIFY.equals(flag)){
                    hashMap.put("startTime",getIntent.getStringExtra("startTime"));
                    hashMap.put("endTime",getIntent.getStringExtra("endTime"));
                    hashMap.put("schedule",getIntent.getStringExtra("schedule"));
                    hashMap.put("aftStartTime",strTimes[0]+":"+strTimes[1]);
                    hashMap.put("aftEndTime",strTimes[2]+":"+strTimes[3]);
                    hashMap.put("aftSchedule",strSchedule);
                }

                doCommunication(hashMap);
            }
        });
    }
    private void initModify(Intent intent){
        Toast toast = Toast.makeText(getApplicationContext(),"수정하는 중 오류가 발생하였습니다.",Toast.LENGTH_SHORT);

        iptSchedule.setText(intent.getStringExtra("schedule"));

        String time = getIntent().getStringExtra("startTime");
        String[] tmpTime;

        if(time != null) {
            tmpTime = time.split(":");
            startPicker.setHour(Integer.parseInt(tmpTime[0]));
            startPicker.setMinute(Integer.parseInt(tmpTime[1]));
        }
        else{
            toast.show();
        }

        time = getIntent().getStringExtra("endTime");
        if(time != null) {
            tmpTime = time.split(":");
            endPicker.setHour(Integer.parseInt(tmpTime[0]));
            endPicker.setMinute(Integer.parseInt(tmpTime[1]));
        }
        else{
            toast.show();
        }
    }

    private void doCommunication(final HashMap<String,Object> hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        ScheduleService scheduleService = retrofit.create(ScheduleService.class);

        Call<Integer> doService = scheduleService.doService(hashMap);

        doService.enqueue(new Callback<Integer>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && response.body()!=null){
                    processCode(response.body(),hashMap);
                }
                else{
                    Log.d("SCHEDULES_ERR","Input Schedule Retrofit Err");
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Integer> call, Throwable t) {
            }
        });
    }

    public void processCode(int code, HashMap hashMap){
        if(code == ADD_SUCCESS){
            AllSchedules.getInstance().addSchedule(!haveSchedule,dateIdx,hashMap);
            Toast.makeText(getApplicationContext(), "Save!!", Toast.LENGTH_SHORT).show();
            setResult(CODE_ISCHANGED);
            finish();
        }
        else if(code == MOD_SUCCESS){
            doModify(hashMap);
            Toast.makeText(getApplicationContext(),"Update!!",Toast.LENGTH_SHORT).show();
            setResult(CODE_ISCHANGED);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
        }
    }

    public void btnExitClicked(View view){
        finish();
    }

    private void doModify(HashMap hashMap){
        AllSchedules.getInstance().modifySchedule(dateIdx,scheduleIdx,hashMap);
    }

}
