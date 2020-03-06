package com.example.scheduleapp.retro;

import com.example.scheduleapp.structure.FriendObject;
import com.example.scheduleapp.structure.GroupObject;
import com.example.scheduleapp.structure.InviteObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface UserService {
    @GET("/schedule/user.do")
    Call<Integer> get_doService(@QueryMap() HashMap<String,Object> parameters);

    @GET("/schedule/user.do")
    Call<Boolean> get_getBoolean(@QueryMap() HashMap<String,String> parameters);

    @GET("/schedule/user.do")
    Call<HashMap<String,ArrayList<GroupObject>>> get_getGroup(@QueryMap HashMap<String,String> parameters);

    @GET("/schedule/user.do")
    Call<ArrayList<InviteObject>> get_getInvites(@QueryMap HashMap<String,String> parameters);

    @GET("/schedule/user.do")
    Call<ArrayList<Integer>> get_getLinkGroups(@QueryMap HashMap<String,String> parameters);

    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<Integer> doService(@FieldMap HashMap<String,Object> parameters);

    @FormUrlEncoded
    @POST("/schedule/user.do")    Call<HashMap> getService(@FieldMap HashMap<String,Object> parameters);

    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<ArrayList<FriendObject>> getFriend(@FieldMap HashMap<String,String> parameters);

    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<String> getName(@FieldMap HashMap<String,String> parameters);

}
