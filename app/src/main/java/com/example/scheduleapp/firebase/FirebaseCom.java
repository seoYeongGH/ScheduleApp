package com.example.scheduleapp.firebase;

import android.util.Log;

import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

import static com.example.scheduleapp.structure.Constant.ERR;

public class FirebaseCom {
    public void updateToken(String oldToken, String newToken){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("doing", "updateCode");
        hashMap.put("oldCode", oldToken);
        hashMap.put("newCode", newToken);

        Call<Integer> doService = userService.get_doService(hashMap);
        doService.enqueue(new Callback<Integer>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && (response.body()!=null)){
                    if(response.body() == ERR)
                        Log.d("ERRRR", "UPDATE_ERR1");
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("ERRRR", "UPDATE_ERR2");
            }
        });
    }
}
