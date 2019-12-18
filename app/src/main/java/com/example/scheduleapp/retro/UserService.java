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
    Call<ArrayList<FriendObject>> getFriend(@FieldMap HashMap<String,String> parameters);

    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<HashMap<String,ArrayList<GroupObject>>> getGroup(@FieldMap HashMap<String,String> parameters);

    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<ArrayList<InviteObject>> getInvites(@FieldMap HashMap<String,String> parameters);

    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<ArrayList<Integer>> getLinkGroups(@FieldMap HashMap<String,String> parameters);


    @FormUrlEncoded
    @POST("/schedule/user.do")
    Call<ArrayList<String>> getStrings(@FieldMap HashMap<String,String> parameters);
}
