package com.example.scheduleapp.Retro;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {

    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<Integer> doService(@FieldMap HashMap<String,Object> parameters);
}
