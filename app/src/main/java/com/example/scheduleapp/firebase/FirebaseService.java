package com.example.scheduleapp.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.scheduleapp.structure.Constant.SHARED_PREF_ISLOGIN;
import static com.example.scheduleapp.structure.Constant.SHARED_PREF_USER_CODE;

public class FirebaseService extends FirebaseMessagingService {
    FirebaseCom commutication = new FirebaseCom();

    public FirebaseService() { }

    public void onNewToken(@NonNull String token){
        SharedPreferences preferences = getSharedPreferences("schPref", Context.MODE_PRIVATE);

        if(preferences.getBoolean(SHARED_PREF_ISLOGIN,false))
            commutication.updateToken(preferences.getString(SHARED_PREF_USER_CODE,""), token);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SHARED_PREF_USER_CODE, token);
        editor.apply();

    }

    public void onMessageReceived (@NonNull RemoteMessage remoteMessage){
        Log.d("PUSHHH","SUCCESS");
    }
}
