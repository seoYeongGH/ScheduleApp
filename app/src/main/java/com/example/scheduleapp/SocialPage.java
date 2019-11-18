package com.example.scheduleapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.widget.Toast;

import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.AllFriends;
import com.example.scheduleapp.structure.FriendObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.scheduleapp.structure.Constant.ERR;
import static com.example.scheduleapp.structure.Constant.SUCCESS;
import static com.example.scheduleapp.structure.Constant.TO_FRIEND;
import static com.example.scheduleapp.structure.Constant.TO_GROUP;

public class SocialPage extends AppCompatActivity {
    RecyclerView recFriend;
    AlertDialog alertDialog;

    FriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_activity);

        recFriend = findViewById(R.id.recFriend);


        Log.d("CHKCHK","CRERATE");

    }

    protected void onResume(){
        super.onResume();

        if(AllFriends.getInstance().isInit()){
            setView(AllFriends.getInstance().getFriends());
        }
        else{
            HashMap hashMap = new HashMap();
            hashMap.put("doing","getFriends");

            getFriends(hashMap);
            AllFriends.getInstance().setInit(true);
        }
    }

    public void onBtnCreateFrdClicked(View view){
        Intent intent = new Intent(getApplicationContext(),AddFriendActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent,TO_FRIEND);
    }


    private void setView(ArrayList<FriendObject> list){
        adapter = new FriendAdapter(getApplicationContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recFriend.setLayoutManager(layoutManager);

        adapter.setListener(new OnFriendBtnListener() {
            @Override
            public void onBtnClicked(FriendAdapter.ViewHolder holder, View view, int position) {
                FriendObject obj = adapter.getItem(position);
                String name= obj.getName();
                String id = obj.getId();

                alertDialog = makeAlert(position, id, name);
                alertDialog.setMessage("친구목록에서 사용자 "+name+"("+id+") 님을 삭제하시겠습니까?");
                alertDialog.show();
            }
        });
        adapter.setItems(list);
        recFriend.setAdapter(adapter);
    }

    private AlertDialog makeAlert(final int position, final String id, final String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notice");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HashMap hashMap = new HashMap();
                hashMap.put("doing","deleteFriend");
                hashMap.put("name",name);
                hashMap.put("id",id);

                deleteToDb(hashMap,position);
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void getFriends(final HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<ArrayList<FriendObject>> getList = userService.getList(hashMap);
        getList.enqueue(new Callback<ArrayList<FriendObject>>() {
            @Override
            public void onResponse(Call<ArrayList<FriendObject>> call, Response<ArrayList<FriendObject>> response) {
                AllFriends.getInstance().setFriends(response.body());
                setView(AllFriends.getInstance().getFriends());
            }

            @Override
            public void onFailure(Call<ArrayList<FriendObject>> call, Throwable t) {

            }
        });
    }


    private void deleteToDb(HashMap hashMap,final int position){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);
        doService.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    processCode(response.body(),position);
                }
                else{
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void processCode(int code, int position){
        if(code == SUCCESS){
            adapter.removeItem(position);
            recFriend.setAdapter(adapter);
        }
        else if(code == ERR){
            Toast.makeText(getApplicationContext(),"ERR!",Toast.LENGTH_SHORT).show();
        }
    }
}
