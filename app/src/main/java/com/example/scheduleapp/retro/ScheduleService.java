package com.example.scheduleapp.retro;

import com.example.scheduleapp.structure.ScheduleObject;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ScheduleService {
    @FormUrlEncoded
    @POST("/schedule/sch.do")
    Call<Integer> doService(@FieldMap HashMap<String,Object> parameters);

    @FormUrlEncoded
    @POST("/schedule/sch.do")
    Call<List<ScheduleObject>> getSchedules(@FieldMap HashMap<String, Object> parameters);
}
