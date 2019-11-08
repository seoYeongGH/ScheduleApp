package com.example.scheduleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.USession;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyPage extends AppCompatActivity{
    TextView txtInfo;
    TextView txtWithdraw;
    RecyclerView menuList;

    private String id;
    private boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_activity);

        id = USession.getInstance().getId();
        txtInfo = findViewById(R.id.txtInfo);
        txtWithdraw = findViewById(R.id.txtWithdraw);

        txtWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),WithdrawPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        HashMap hashMap = new HashMap();
        hashMap.put("doing","getInfo");
        hashMap.put("id",id);

        getCommunication(hashMap);

        /*
        final String[] arrMenu = {"▶ 쪽지함","▶ 내가 작성한 글", "▶ 내 정보보기", "▶ 비밀번호 변경"};

        isShow = false;


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrMenu);

        ListView lstMenu = findViewById(R.id.lstMyMenu);
        lstMenu.setAdapter(adapter);

        lstMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==2){
                    if(isShow)
                        txtInfo.setTextSize(0);
                    else
                        txtInfo.setTextSize(20);

                    isShow = !isShow;
                }
                else if(i==3){
                    Intent intent = new Intent(getApplicationContext(),ChangePwPage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            }
        });*/

        menuList = findViewById(R.id.listMyMenu);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        menuList.setLayoutManager(layoutManager);
        MenuAdapter menuAdapter = new MenuAdapter(getApplicationContext());
        menuAdapter.addItem("▶ 쪽지함");
        menuAdapter.addItem("▶ 내가 작성한 글");
        menuAdapter.addItem("▶ 내 정보보기");
        menuAdapter.addItem("▶ 비밀번호 변경");
        menuList.setAdapter(menuAdapter);

        isShow = false;
        menuAdapter.setOnMenuItemClickedListener(new OnMenuItemClickedListener() {
            @Override
            public void onItemClick(MenuAdapter.ViewHolder holder, View view, int position) {
                if(position == 2){
                    if(isShow)
                        txtInfo.setTextSize(0);
                    else
                        txtInfo.setTextSize(20);

                    isShow = !isShow;
                }
                else if(position ==3){
                    Intent intent = new Intent(getApplicationContext(),ChangePwPage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            }
        });
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(menuList.getContext(),new LinearLayoutManager(this).getOrientation());
        menuList.addItemDecoration(dividerItemDecoration);

        RecyclerDecoration recyclerDecoration = new RecyclerDecoration(15);
        menuList.addItemDecoration(recyclerDecoration);
    }

    private void getCommunication (HashMap hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<HashMap> getService = userService.getService(hashMap);

        getService.enqueue(new Callback<HashMap>() {
            @Override
            public void onResponse(Call<HashMap> call, Response<HashMap> response) {
                if(response.isSuccessful()){
                    Log.d("DODO","START");
                    HashMap hashInfo =  response.body();
                    txtInfo.setText("My Info\n\nID: "+id+"\n이름: "+hashInfo.get("name")+"\nE-mail: "+hashInfo.get("email"));
                }
                else{
                    Log.d("INFO_ERR","Info Retrofit Err");
                }
            }

            @Override
            public void onFailure(Call<HashMap> call, Throwable t) {
                Log.d("ERRRRR",t.getMessage());
            }
        });
    }

    public class RecyclerDecoration extends RecyclerView.ItemDecoration {

        private final int divHeight;


        public RecyclerDecoration(int divHeight) {
            this.divHeight = divHeight;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = divHeight;
                outRect.top = divHeight;

        }
    }
}
