package com.example.scheduleapp.firebase;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.scheduleapp.MainActivity;
import com.example.scheduleapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.scheduleapp.structure.Constant.SHARED_PREF_ISLOGIN;
import static com.example.scheduleapp.structure.Constant.SHARED_PREF_USER_CODE;

public class FirebaseService extends FirebaseMessagingService {
    FirebaseCom commutication = new FirebaseCom();

    public FirebaseService() {
    }

    public void onNewToken(String token){
        SharedPreferences preferences = getSharedPreferences("schPref", Context.MODE_PRIVATE);

        if(preferences.getBoolean(SHARED_PREF_ISLOGIN,false))
            commutication.updateToken(preferences.getString(SHARED_PREF_USER_CODE,""), token);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SHARED_PREF_USER_CODE, token);
        editor.apply();

    }

    public void onMessageReceived (RemoteMessage remoteMessage){
        Log.d("PUSH","SUCCESS");
    }
}
