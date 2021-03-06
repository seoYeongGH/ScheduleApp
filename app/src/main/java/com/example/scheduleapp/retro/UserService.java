package com.example.scheduleapp.retro;

import com.example.scheduleapp.structure.FriendObject;
import com.example.scheduleapp.structure.GroupObject;
import com.example.scheduleapp.structure.InviteObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface UserService {
    @GET("/schedule/user.do")
    Call<Integer> get_doService(@QueryMap() HashMap<String,Object> parameters);

    @GET("/schedule/user.do")
    Call<HashMap<String,ArrayList<GroupObject>>> get_getGroup(@Query("doing") String doing);

    @GET("/schedule/user.do")
    Call<ArrayList<InviteObject>> get_getInvites(@QueryMap HashMap<String,String> parameters);

    @GET("/schedule/user.do")
    Call<ArrayList<Integer>> get_getLinkGroups(@Query("doing")  String doing);

    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<Integer> doService(@FieldMap HashMap<String,String> parameters);

    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<HashMap<String,Object>> getService(@FieldMap HashMap<String,Object> parameters);

    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<ArrayList<FriendObject>> getFriend(@FieldMap HashMap<String,String> parameters);

    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<String> getInfo(@Field("doing") String doing);
}
