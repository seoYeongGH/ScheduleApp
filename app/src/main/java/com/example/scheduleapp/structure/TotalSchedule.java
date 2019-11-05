package com.example.scheduleapp.structure;

import android.util.Log;

import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.ScheduleService;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TotalSchedule {
    private TotalSchedule instance;

    public TotalSchedule getInstance(){
        if(instance == null)
            instance = new TotalSchedule();
        return instance;
    }

    private TotalSchedule(){

    }

    private void doCommunication(final HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        ScheduleService scheduleService = retrofit.create(ScheduleService.class);

        Call<Integer> doService = scheduleService.doService(hashMap);

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
