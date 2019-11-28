package com.example.scheduleapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.scheduleapp.recyclerView.FriendAdapter;
import com.example.scheduleapp.recyclerView.GroupAdapter;
import com.example.scheduleapp.recyclerView.OnFriendBtnListener;
import com.example.scheduleapp.recyclerView.OnGroupListener;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.AllFriends;
import com.example.scheduleapp.structure.AllGroups;
import com.example.scheduleapp.structure.FriendObject;
import com.example.scheduleapp.structure.GroupObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.scheduleapp.structure.Constant.ERR;
import static com.example.scheduleapp.structure.Constant.SUCCESS;
import static com.example.scheduleapp.structure.Constant.TO_FRIEND;

public class SocialPage extends AppCompatActivity {
    RecyclerView recFriend;
    FriendAdapter friendAdapter;

    RecyclerView recGroup;
    GroupAdapter groupAdapter;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_activity);

        recGroup = findViewById(R.id.recGroup);
        recFriend = findViewById(R.id.recFriend);

        groupAdapter = new GroupAdapter(getApplicationContext());

        HashMap hashMap = new HashMap();
        hashMap.put("doing","getGroups");
        getGroups(hashMap);

    }

    protected void onResume(){
        super.onResume();

        if(AllFriends.getInstance().isInit()){
            setFriendView(AllFriends.getInstance().getFriends());
        }
        else{
            HashMap hashMap = new HashMap();
            hashMap.put("doing","getFriends");

            getFriends(hashMap);
            AllFriends.getInstance().setInit(true);
        }
    }

    public void onBtnCreateFrdClicked(View view){
        Intent intent = new Intent(getApplicationContext(), AddFriendPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent,TO_FRIEND);
    }

    public void onBtnCreateGpClicked(View view){
        Intent intent = new Intent(getApplicationContext(),CreateGroupPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    private void setFriendView(ArrayList<FriendObject> list){
        friendAdapter = new FriendAdapter(getApplicationContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recFriend.setLayoutManager(layoutManager);

        friendAdapter.setListener(new OnFriendBtnListener() {
            @Override
            public void onBtnClicked(FriendAdapter.ViewHolder holder, View view, int position) {
                FriendObject obj = friendAdapter.getItem(position);
                String name= obj.getName();
                String id = obj.getId();

                HashMap hashMap = new HashMap();
                hashMap.put("doing","deleteFriend");
                hashMap.put("id",id);
                hashMap.put("name",name);

                alertDialog = makeAlert(position, hashMap);
                alertDialog.setMessage("친구목록에서 사용자 "+name+"("+id+") 님을 삭제하시겠습니까?");
                alertDialog.show();
            }
        });
        friendAdapter.setItems(list);
        recFriend.setAdapter(friendAdapter);

    }

    private void setGroupView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recGroup.setLayoutManager(layoutManager);

        groupAdapter.setListener(new OnGroupListener() {
            @Override
            public void onItemClicked(GroupAdapter.ViewHolder holder,  int position) { boolean isManager = false;
                if(position<groupAdapter.getManagerSize())
                    isManager = true;

                Intent intent = new Intent(getApplicationContext(), GroupSchedulePage.class);
                intent.putExtra("groupNum",groupAdapter.getItem(position).getGroupNum());
                intent.putExtra("groupName",groupAdapter.getItem(position).getGroupName());
                intent.putExtra("isManager",isManager);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }

            @Override
            public void onBtnDeleteClicked(GroupAdapter.ViewHolder holder, View view, int position) {
                GroupObject obj = groupAdapter.getItem(position);
                String name = obj.getGroupName();
                int groupNum = obj.getGroupNum();

                HashMap hashMap = new HashMap();
                hashMap.put("doing","deleteGroup");
                hashMap.put("name",name);
                hashMap.put("groupNum",groupNum);

                alertDialog = makeAlert(position,hashMap);
                alertDialog.setMessage("그룹 "+name+"에서 탈퇴하시겠습니까?");
                alertDialog.show();
            }

            @Override
            public void onBtnMemberClicked(GroupAdapter.ViewHolder holder, View view, int position) {
                boolean isManager = false;
                if(position<groupAdapter.getManagerSize())
                    isManager = true;

                Intent intent = new Intent(getApplicationContext(),MemberActivity.class);
                intent.putExtra("groupNum",groupAdapter.getItem(position).getGroupNum());
                intent.putExtra("groupName",groupAdapter.getItem(position).getGroupName());
                intent.putExtra("isManager",isManager);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        groupAdapter.setGroups(AllGroups.getInstance().getIsManagers(),AllGroups.getInstance().getNotManagers());
        recGroup.setAdapter(groupAdapter);
    }

    private AlertDialog makeAlert(final int position, final HashMap hashMap){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notice");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doService(hashMap,position);
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

        Call<ArrayList<FriendObject>> getFriend = userService.getFriend(hashMap);
        getFriend.enqueue(new Callback<ArrayList<FriendObject>>() {
            @Override
            public void onResponse(Call<ArrayList<FriendObject>> call, Response<ArrayList<FriendObject>> response) {
                AllFriends.getInstance().setFriends(response.body());
                setFriendView(AllFriends.getInstance().getFriends());
            }

            @Override
            public void onFailure(Call<ArrayList<FriendObject>> call, Throwable t) {

            }
        });
    }

    private void getGroups(HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<HashMap<String,ArrayList<GroupObject>>>  getGroup = userService.getGroup(hashMap);
        getGroup.enqueue(new Callback<HashMap<String,ArrayList<GroupObject>>>() {
            @Override
            public void onResponse(Call<HashMap<String,ArrayList<GroupObject>>> call, Response<HashMap<String,ArrayList<GroupObject>>> response) {
                HashMap getHash = response.body();

                AllGroups.getInstance().setIsManagers((ArrayList<GroupObject>)getHash.get("isManager"));
                AllGroups.getInstance().setNotManagers((ArrayList<GroupObject>)getHash.get("notManager"));

                setGroupView();
            }

            @Override
            public void onFailure(Call<HashMap<String,ArrayList<GroupObject>>> call, Throwable t) {

            }
        });
    }
    private void doService(final HashMap hashMap, final Integer position){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);
        doService.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    processCode(response.body(),(String)hashMap.get("doing"),position);
                }
                else{
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void processCode(int code, String doing, int position){
        if(code == SUCCESS){
            if("deleteFriend".equals(doing)) {
                friendAdapter.removeItem(position);
                recFriend.setAdapter(friendAdapter);
            }
            else if("deleteGroup".equals(doing)){
                groupAdapter.removeItem(position);
                recGroup.setAdapter(groupAdapter);
            }
        }
        else if(code == ERR){
            Toast.makeText(getApplicationContext(),"ERR!",Toast.LENGTH_SHORT).show();
        }
    }
}
