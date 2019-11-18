package com.example.scheduleapp.retro;

import com.example.scheduleapp.structure.FriendObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {

    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<Integer> doService(@FieldMap HashMap<String,Object> parameters);

    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<HashMap> getService(@FieldMap HashMap<String,String> parameters);

    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<ArrayList<FriendObject>> getList(@FieldMap HashMap<String,String> parameters);
}
