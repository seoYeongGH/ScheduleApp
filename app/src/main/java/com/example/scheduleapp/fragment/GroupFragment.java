package com.example.scheduleapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.scheduleapp.CreateGroupPage;
import com.example.scheduleapp.GroupSchedulePage;
import com.example.scheduleapp.MemberActivity;
import com.example.scheduleapp.R;
import com.example.scheduleapp.recyclerView.GroupAdapter;
import com.example.scheduleapp.recyclerView.OnGroupListener;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.AllGroups;
import com.example.scheduleapp.structure.GroupObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

import static com.example.scheduleapp.structure.Constant.ADD_GROUP;
import static com.example.scheduleapp.structure.Constant.CODE_ISADDED;

public class GroupFragment extends Fragment {
    private RecyclerView recGroup;
    private GroupAdapter groupAdapter;

    public GroupFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView =  (ViewGroup)inflater.inflate(R.layout.fragment_group, container, false);
        recGroup = rootView.findViewById(R.id.recGroup);

        Button btnCreateGroup = rootView.findViewById(R.id.btnAdd);
        btnCreateGroup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateGroupPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent,ADD_GROUP);
            }
        });

        Context context = getContext();
        if(context != null) {
            groupAdapter = new GroupAdapter(context);
            recGroup.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        }
        else{
            Log.d("ERRRR","Group Context Null");
        }

        setGroupView();

        return rootView;
    }


    private void setGroupView(){
        groupAdapter.clearList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recGroup.setLayoutManager(layoutManager);

        groupAdapter.setListener(new OnGroupListener() {
            @Override
            public void onItemClicked(GroupAdapter.ViewHolder holder,  int position) {
                boolean isManager = false;
                if(position<groupAdapter.getManagerSize())
                    isManager = true;

                Intent intent = new Intent(getContext(), GroupSchedulePage.class);
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

                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("doing","deleteGroup");
                hashMap.put("name",name);
                hashMap.put("groupNum",groupNum);

                showAlert(hashMap,position);
            }

            @Override
            public void onBtnMemberClicked(GroupAdapter.ViewHolder holder, View view, int position) {
                boolean isManager = false;
                if(position<groupAdapter.getManagerSize())
                    isManager = true;

                Intent intent = new Intent(getContext(), MemberActivity.class);
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

    public void onActivityResult(int requestCode, int resultCode, Intent dataIntent){
        super.onActivityResult(requestCode, resultCode, dataIntent);

        if(resultCode == CODE_ISADDED)
            setGroupView();
    }

    private void showAlert(final HashMap<String,Object> hashMap, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Notice");
        builder.setMessage("그룹 "+hashMap.get("name")+"에서 탈퇴하시겠습니까?");

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

        builder.create().show();
    }

    private void doService(final HashMap<String,Object> hashMap, final Integer position){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.get_doService(hashMap);
        doService.enqueue(new Callback<Integer>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    AllGroups.getInstance().removeGroup(position);
                    setGroupView();
                }
                else{
                    Log.d("ERRRR","Response_Error");
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

}
