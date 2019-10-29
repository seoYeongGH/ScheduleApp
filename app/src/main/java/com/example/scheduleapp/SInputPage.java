package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.ScheduleStruct;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.scheduleapp.structure.Constant.ERR;

public class SInputPage extends AppCompatActivity {
    EditText iptSchedule;
    EditText iptStartH;
    EditText iptStartM;
    EditText iptEndH;
    EditText iptEndM;
    TextView txtWarn;

    private String date;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinput_page);

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

        Intent getIntent = getIntent();
        date = getIntent.getStringExtra("date");
        flag = getIntent.getStringExtra("flag");

        TextView txtDate = findViewById(R.id.txtDate);
        txtDate.setText(date);
    }

    public void onBtnSaveClicked(View view){
        String strSchedule = iptSchedule.getText().toString();
        String strStartH = iptStartH.getText().toString();
        String strStartM = iptStartM.getText().toString();
        String strEndH = iptEndH.getText().toString();
        String strEndM = iptEndM.getText().toString();

        if(strSchedule.length()==0 || strStartH.length()==0 || strStartM.length()==0 || strEndH.length()==0 || strEndM.length()==0) {
            txtWarn.setTextSize(15);
        }
        else {
            txtWarn.setTextSize(0);
            ScheduleStruct schStruct = new ScheduleStruct(date,strStartH,strStartM,strEndH,strEndM,strSchedule);

            HashMap hashMap = new HashMap();
            hashMap.put("flag",flag);
            hashMap.put("schedule",schStruct);

            doCommunication(hashMap);
        }
    }


    private void doCommunication(final HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);

        doService.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                }
                else{
                    Log.d("INPUT_ERR","Input Schedule Retrofit Err");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }
}
