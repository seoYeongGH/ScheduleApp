package com.example.scheduleapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleapp.recyclerView.MemberAdapter;
import com.example.scheduleapp.recyclerView.NameAdapter;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.AllFriends;
import com.example.scheduleapp.structure.FriendObject;
import com.example.scheduleapp.structure.USession;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.scheduleapp.structure.Constant.SUCCESS;

public class MemberActivity extends AppCompatActivity {
    RecyclerView  recMember;
    NameAdapter adapter;
    MemberAdapter memberAdapter;

    LinearLayout beforeClk;
    LinearLayout afterClk;

    ArrayList<FriendObject> members;
    FriendObject managerObj;
    int managerPos;

    HashMap btnHashMap;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_activity);

        Intent getIntent = getIntent();

        FrameLayout frameBtn = findViewById(R.id.frameBtn);
        beforeClk = findViewById(R.id.layoutBeforeClk);
        afterClk = findViewById(R.id.layoutAfterClk);

        TextView txtName = findViewById(R.id.txtGpName);

        recMember = findViewById(R.id.recMember);
        recMember.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));

        String groupName = getIntent.getStringExtra("groupName");
        txtName.setText(groupName);
        if(getIntent.getBooleanExtra("isManager",false)) {
            frameBtn.setVisibility(View.VISIBLE);
            beforeClk.setVisibility(View.VISIBLE);
            afterClk.setVisibility(View.INVISIBLE);

            btnHashMap = new HashMap();
            btnHashMap.put("name",groupName);
            btnHashMap.put("groupNum",getIntent.getIntExtra("groupNum",-1));

            alertDialog = makeAlert();
        }
        else{
            frameBtn.setVisibility(View.GONE);
        }
        members = new ArrayList<>();

        HashMap hashMap = new HashMap();
        hashMap.put("doing","getMembers");
        hashMap.put("groupNum",getIntent.getIntExtra("groupNum",-1));

        getFriends(hashMap);
    }

    public void onBtnExitClicked(View view){
         finish();
    }

    public void onBtnAddClicked(View view){
        ArrayList<FriendObject> friends = AllFriends.getInstance().getFriends();
        ArrayList<FriendObject> notMembers = new ArrayList<>();
        int j=0, memberSize = members.size();

        for(int i=0; i<friends.size(); i++){
            if(j<memberSize) {
                if(members.get(j).getId().equals(USession.getInstance().getId()))
                    j++;

                if(j<memberSize && !friends.get(i).getId().equals(members.get(j).getId()))
                    notMembers.add(friends.get(i));
                else if(j>=memberSize)
                    notMembers.add(friends.get(i));
                else
                    j++;
            }
            else{
                notMembers.add(friends.get(i));
            }
        }
        memberAdapter = new MemberAdapter(notMembers);
        recMember.setAdapter(memberAdapter);

        btnHashMap.put("doing","sendInvite");

        beforeClk.setVisibility(View.INVISIBLE);
        afterClk.setVisibility(View.VISIBLE);
    }

    public void onBtnDeleteClicked(View view){
        String id = USession.getInstance().getId();
        int size = members.size();

        for(int i=0; i<size; i++){
            if(id.equals(members.get(i).getId())) {
                managerPos = i;
                managerObj = members.get(i);

                members.remove(i);
                break;
            }
        }

        memberAdapter = new MemberAdapter(members);
        recMember.setAdapter(memberAdapter);

        btnHashMap.put("doing","withdrawMember");

        beforeClk.setVisibility(View.INVISIBLE);
        afterClk.setVisibility(View.VISIBLE);
    }

    public void onBtnSaveClicked(View view){
        if(memberAdapter.getInviteNum() != 0) {
            btnHashMap.put("ids", memberAdapter.getIds());

            Retrofit retrofit = RetroController.getInstance().getRetrofit();
            UserService userService = retrofit.create(UserService.class);

            Call<Integer> doService = userService.doService(btnHashMap);
            doService.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful())
                        showAlert();
                    else
                        Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
        }
        else{
            btnHashMap.put("doing","selectedNull");
            showAlert();
        }
    }

    public void onBtnCancelClicked(View view){
        recMember.setAdapter(adapter);

        beforeClk.setVisibility(View.VISIBLE);
        afterClk.setVisibility(View.INVISIBLE);
    }


    private void getFriends(final HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<ArrayList<FriendObject>> getName = userService.getFriend(hashMap);
        getName.enqueue(new Callback<ArrayList<FriendObject>>() {
            @Override
            public void onResponse(Call<ArrayList<FriendObject>> call, Response<ArrayList<FriendObject>> response) {
                if(response.isSuccessful()) {
                    members = response.body();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    recMember.setLayoutManager(layoutManager);

                    adapter = new NameAdapter();
                    adapter.setNames(members);

                    recMember.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FriendObject>> call, Throwable t) {

            }
        });
    }

    private void showAlert(){
        if("sendInvite".equals(btnHashMap.get("doing"))){
            alertDialog.setMessage("새로운 친구들에게 초대 신청을 보냈습니다. :)");
        }
        else if("withdrawMember".equals(btnHashMap.get("doing"))){
            alertDialog.setMessage(btnHashMap.get("name")+"그룹에서 친구들을 삭제했습니다. :(");
        }
        else if("selectedNull".equals(btnHashMap.get("doing"))){
            alertDialog.setMessage("아무에게도 신청을 보내지 않았습니다.");
        }

        alertDialog.show();
    }

    private AlertDialog makeAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notice");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        return  builder.create();
    }

}
