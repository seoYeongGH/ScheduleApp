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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.scheduleapp.AddFriendPage;
import com.example.scheduleapp.R;
import com.example.scheduleapp.recyclerView.FriendAdapter;
import com.example.scheduleapp.recyclerView.OnFriendBtnListener;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.AllFriends;
import com.example.scheduleapp.structure.FriendObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

import static com.example.scheduleapp.structure.Constant.CODE_ISADDED;
import static com.example.scheduleapp.structure.Constant.TO_FRIEND;

public class FriendFragment extends Fragment {
    private RecyclerView recFriend;
    private FriendAdapter friendAdapter;

    public FriendFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_friend, container, false);
        recFriend = rootView.findViewById(R.id.recFriend);

        Context context = getContext();
        if(context != null) {
            recFriend.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            friendAdapter = new FriendAdapter();
        }
        setFriendView();

        Button btnAdd = rootView.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddFriendPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent,TO_FRIEND);

            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCOde, int resultCode, Intent data){
        if(resultCode == CODE_ISADDED){
            setFriendView();
        }
    }

    private void setFriendView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recFriend.setLayoutManager(layoutManager);

        friendAdapter.setListener(new OnFriendBtnListener() {
            @Override
            public void onBtnClicked(FriendAdapter.ViewHolder holder, View view, int position) {
                FriendObject obj = friendAdapter.getItem(position);
                String name= obj.getName();
                String id = obj.getId();

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("doing","deleteFriend");
                hashMap.put("id",id);
                hashMap.put("name",name);

                showAlert(hashMap,position);
            }
        });

        friendAdapter.setItems(AllFriends.getInstance().getFriends());
        recFriend.setAdapter(friendAdapter);
    }

    private void showAlert(final HashMap<String, String> hashMap, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Notice");
        builder.setMessage("친구목록에서 사용자 "+hashMap.get("name")+"("+hashMap.get("id")+") 님을 삭제하시겠습니까?");

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

    private void doService(final HashMap<String, String> hashMap, final Integer position){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);
        doService.enqueue(new Callback<Integer>() {

            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    friendAdapter.removeItem(position);
                    recFriend.setAdapter(friendAdapter);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

}
