package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.ScheduleService;
import com.example.scheduleapp.structure.AllSchedules;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.scheduleapp.structure.Constant.ADD_SUCCESS;
import static com.example.scheduleapp.structure.Constant.DELETE_SUCCESS;
import static com.example.scheduleapp.structure.Constant.FLAG_ADD;
import static com.example.scheduleapp.structure.Constant.FLAG_MODIFY;
import static com.example.scheduleapp.structure.Constant.MOD_SUCCESS;
import static com.example.scheduleapp.structure.Constant.SUCCESS;

public class SInputPage extends AppCompatActivity {
    EditText iptSchedule;
    EditText iptStartH;
    EditText iptStartM;
    EditText iptEndH;
    EditText iptEndM;
    TextView txtWarn;

    private String date;
    private String flag;
    private int groupNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sinput_activity);

        final Intent getIntent = getIntent();
        date = getIntent.getStringExtra("date");
        flag = getIntent.getStringExtra("flag");
        groupNum = getIntent.getIntExtra("groupNum",-1);

        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnSave = findViewById(R.id.btnSave);

        iptSchedule = findViewById(R.id.iptSchedule);
        iptStartH = findViewById(R.id.iptStartH);
        iptStartM = findViewById(R.id.iptStartM);
        iptEndH = findViewById(R.id.iptEndH);
        iptEndM = findViewById(R.id.iptEndM);
        txtWarn  = findViewById(R.id.txtWarn);

        CheckBox chkAll = findViewById(R.id.chkAll);
        chkAll.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton checkBox, boolean isChecked){
                if(isChecked){
                    iptStartH.setText("00");
                    iptStartM.setText("00");
                    iptEndH.setText("23");
                    iptEndM.setText("59");
                }
            }
        });

        TextView txtDate = findViewById(R.id.txtDate);
        txtDate.setText(date);

        if(FLAG_ADD.equals(flag)) {
            btnDelete.setVisibility(View.INVISIBLE);
        }
        else if(FLAG_MODIFY.equals(flag)) {
            btnDelete.setVisibility(View.VISIBLE);
            initModify(getIntent);
        }

        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HashMap hashMap = new HashMap();
                hashMap.put("doing","deleteSchedule");
                hashMap.put("scheduleDate",date);
                hashMap.put("schedule",getIntent.getStringExtra("schedule"));
                hashMap.put("time",getIntent.getStringExtra("time"));

                doCommunication(hashMap);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String strSchedule = iptSchedule.getText().toString();

                String[] strTimes = new String[4];
                strTimes[0] = iptStartH.getText().toString();
                strTimes[1] = iptStartM.getText().toString();
                strTimes[2] = iptEndH.getText().toString();
                strTimes[3] = iptEndM.getText().toString();

                if(strSchedule.length()==0) {
                    txtWarn.setTextSize(15);
                    return;
                }

                for(int i=0; i<4; i++){
                    int length = strTimes[i].length();

                    if(length==0){
                        txtWarn.setTextSize(15);
                        return;
                    }
                    if(Integer.parseInt(strTimes[i])<0)
                        strTimes[i] = "00";

                    if(i%2==1){
                        if(Integer.parseInt(strTimes[i])>59)
                            strTimes[i] = "59";
                    }
                    else{
                        if(Integer.parseInt(strTimes[i])>23)
                            strTimes[i] = "23";
                    }

                    if(length==1)
                        strTimes[i] = "0"+strTimes[i];
                }
                txtWarn.setTextSize(0);

                HashMap hashMap = new HashMap();
                hashMap.put("doing",flag);
                hashMap.put("date",date);
                hashMap.put("groupNum",groupNum);

                if(FLAG_ADD.equals(flag)){
                    hashMap.put("startTime",strTimes[0]+":"+strTimes[1]);
                    hashMap.put("endTime",strTimes[2]+":"+strTimes[3]);
                    hashMap.put("schedule",strSchedule);
                }
                else if(FLAG_MODIFY.equals(flag)){
                    hashMap.put("time",getIntent.getStringExtra("time"));
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
        iptSchedule.setText(intent.getStringExtra("schedule"));

        String[] tmpTimes = intent.getStringExtra("time").split("~");

        String tmpTime[] = tmpTimes[0].split(":");
        iptStartH.setText(tmpTime[0]);
        iptStartM.setText(tmpTime[1]);

        tmpTime = tmpTimes[1].split(":");
        iptEndH.setText(tmpTime[0]);
        iptEndM.setText(tmpTime[1]);
    }


    private void doCommunication(HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        ScheduleService scheduleService = retrofit.create(ScheduleService.class);

        Call<Integer> doService = scheduleService.doService(hashMap);

        doService.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    processCode(response.body().intValue());
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

    public void processCode(int code){
        if(code == ADD_SUCCESS){
            Toast.makeText(getApplicationContext(), "Save!!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if(code == DELETE_SUCCESS){
            Toast.makeText(getApplicationContext(),"Delete!",Toast.LENGTH_SHORT).show();
            finish();
        }
        else if(code == MOD_SUCCESS){
            Toast.makeText(getApplicationContext(),"Update!!",Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_LONG).show();
        }
    }

    public void btnExitClicked(View view){
        finish();
    }

}
