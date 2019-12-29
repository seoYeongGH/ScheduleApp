package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.scheduleapp.recyclerView.InviteAdapter;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.InviteObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InvitePage extends AppCompatActivity {
    RecyclerView recInvite;
    InviteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_activity);

        recInvite = findViewById(R.id.recInvite);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recInvite.setLayoutManager(layoutManager);

        adapter = new InviteAdapter(getApplicationContext());

        HashMap hashMap = new HashMap();
        hashMap.put("doing","getInvites");

        getInvites(hashMap);

        recInvite.setAdapter(adapter);
        recInvite.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
    }

    protected void onResume(){
        super.onResume();
    }

    private void getInvites(final HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<ArrayList<InviteObject>> getInvites = userService.get_getInvites(hashMap);
        getInvites.enqueue(new Callback<ArrayList<InviteObject>>() {
            @Override
            public void onResponse(Call<ArrayList<InviteObject>> call, Response<ArrayList<InviteObject>> response) {
                if (response.isSuccessful()) {
                    adapter.setItems(response.body());
                    recInvite.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<InviteObject>> call, Throwable t) {

            }
        });
    }
}
