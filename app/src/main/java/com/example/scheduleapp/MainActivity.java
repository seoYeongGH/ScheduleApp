package com.example.scheduleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.scheduleapp.fragment.AfterLoginFragment;
import com.example.scheduleapp.fragment.BeforeLoginFragment;
import com.example.scheduleapp.fragment.MyPageFragment;
import com.example.scheduleapp.fragment.SocialFragment;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.AllFriends;
import com.example.scheduleapp.structure.AllGroups;
import com.example.scheduleapp.structure.FriendObject;
import com.example.scheduleapp.structure.GroupObject;
import com.example.scheduleapp.structure.USession;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

import static com.example.scheduleapp.structure.Constant.ERR;
import static com.example.scheduleapp.structure.Constant.FOR_USER;
import static com.example.scheduleapp.structure.Constant.SHARED_PREF_ISLOGIN;
import static com.example.scheduleapp.structure.Constant.SHARED_PREF_USER_CODE;
import static com.example.scheduleapp.structure.Constant.SUCCESS;

public class MainActivity extends AppCompatActivity {
    BeforeLoginFragment beforeFragment;

    AfterLoginFragment afterFragment;
    SocialFragment socialFragment;
    MyPageFragment myPageFragment;

    FrameLayout container;

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beforeFragment = new BeforeLoginFragment();

        afterFragment = new AfterLoginFragment(FOR_USER);
        socialFragment = new SocialFragment();
        myPageFragment = new MyPageFragment();

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);

        imageView1 = new ImageView(this);
        imageView1.setImageResource(R.drawable.back_before3);
        imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView1.setLayoutParams(layoutParams);

        imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.back_before2);
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView2.setLayoutParams(layoutParams);

        imageView3 = new ImageView(this);
        imageView3.setImageResource(R.drawable.back_before1);
        imageView3.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView3.setLayoutParams(layoutParams);

        container = findViewById(R.id.container);

        navigationView = findViewById(R.id.navigationView);

        setDisplay();
    }

    public void onResume(){
        super.onResume();

        if(!USession.getInstance().getIsLogin()) {
            SharedPreferences preferences = getSharedPreferences("schPref", Context.MODE_PRIVATE);
            if (preferences != null && preferences.getBoolean(SHARED_PREF_ISLOGIN, false)) {
                autoLogin(preferences.getInt(SHARED_PREF_USER_CODE, -1));
            }
        }
    }
    @Override
    public void onBackPressed() {
        BackButton.getInstance().onBtnPressed(getApplicationContext(),this);
    }

    private void setDisplay(){
        if(USession.getInstance().getIsLogin()){
            container.removeAllViews();

            navigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                            switch(menuItem.getItemId()){
                                case R.id.tabSch:
                                    getSupportFragmentManager().beginTransaction().replace(R.id.container,afterFragment).commit();
                                    return true;

                                case R.id.tabSocial:
                                    getSupportFragmentManager().beginTransaction().replace(R.id.container,socialFragment).commit();
                                    return true;

                                case R.id.tabMy:
                                    getSupportFragmentManager().beginTransaction().replace(R.id.container,myPageFragment).commit();
                                    return true;
                            }
                            return false;
                        }
                    }
            );
            navigationView.setSelectedItemId(R.id.tabSch);
        }
        else{
            container.addView(imageView1,0);
            container.addView(imageView2,1);
            container.addView(imageView3,2);

            getSupportFragmentManager().beginTransaction().remove(afterFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.container,beforeFragment).commit();

            navigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                            switch(menuItem.getItemId()){
                                case R.id.tabSch:
                                    imageView1.setVisibility(View.VISIBLE);
                                    imageView2.setVisibility(View.INVISIBLE);
                                    imageView3.setVisibility(View.INVISIBLE);
                                    return true;

                                case R.id.tabSocial:
                                    imageView1.setVisibility(View.INVISIBLE);
                                    imageView2.setVisibility(View.VISIBLE);
                                    imageView3.setVisibility(View.INVISIBLE);
                                    return true;

                                case R.id.tabMy:
                                    imageView1.setVisibility(View.INVISIBLE);
                                    imageView2.setVisibility(View.INVISIBLE);
                                    imageView3.setVisibility(View.VISIBLE);
                                    return true;
                            }
                            return false;
                        }
                    }
            );
            navigationView.setSelectedItemId(R.id.tabSch);
        }
    }

    private void autoLogin(int userCode) {
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("doing", "autoLogin");
        hashMap.put("userCode", userCode);

        Call<HashMap> getService = userService.getService(hashMap);
        getService.enqueue(new Callback<HashMap>() {
            @Override
            public void onResponse(Call<HashMap> call, Response<HashMap> response) {
                if(response.isSuccessful()){
                    double tmpCode = (double)response.body().get("code");
                    doLogin(response.body().get("id").toString(), (int)tmpCode);
                }
            }

            @Override
            public void onFailure(Call<HashMap> call, Throwable t) {

            }
        });
    }
    private void doLogin(String id, int code){
        switch(code){
            case SUCCESS: getDatas(id);

                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default: Toast.makeText(getApplicationContext(),"자동 로그인에 실패했습니다. :(", Toast.LENGTH_SHORT).show();
                     break;
        }
    }
    private void getGroupNums(){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        HashMap hashMap = new HashMap();
        hashMap.put("doing","getLinkGroups");

        Call<ArrayList<Integer>> getGroups = userService.get_getLinkGroups(hashMap);
        getGroups.enqueue(new Callback<ArrayList<Integer>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ArrayList<Integer>> call, Response<ArrayList<Integer>> response) {
                if(response.isSuccessful())
                    USession.getInstance().setConnectGroups(response.body());
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ArrayList<Integer>> call, Throwable t) {
                Log.d("ERRRR","GET_LINK_ERR");
            }
        });
    }

    private void getGroups(){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        HashMap hashMap = new HashMap();
        hashMap.put("doing","getGroups");

        Call<HashMap<String, ArrayList<GroupObject>>> getGroup = userService.get_getGroup(hashMap);
        getGroup.enqueue(new Callback<HashMap<String,ArrayList<GroupObject>>>() {
            @Override
            public void onResponse(Call<HashMap<String,ArrayList<GroupObject>>> call, Response<HashMap<String,ArrayList<GroupObject>>> response) {
                HashMap getHash = response.body();

                AllGroups.getInstance().setIsManagers((ArrayList<GroupObject>)getHash.get("isManager"));
                AllGroups.getInstance().setNotManagers((ArrayList<GroupObject>)getHash.get("notManager"));
            }

            @Override
            public void onFailure(Call<HashMap<String,ArrayList<GroupObject>>> call, Throwable t) {

            }
        });
    }


    private void getFriends(){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        HashMap hashMap = new HashMap();
        hashMap.put("doing","getFriends");

        Call<ArrayList<FriendObject>> getFriend = userService.getFriend(hashMap);
        getFriend.enqueue(new Callback<ArrayList<FriendObject>>() {
            @Override
            public void onResponse(Call<ArrayList<FriendObject>> call, Response<ArrayList<FriendObject>> response) {
                AllFriends.getInstance().setFriends(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<FriendObject>> call, Throwable t) {

            }
        });
    }


    private void getName(){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        HashMap hashMap = new HashMap();
        hashMap.put("doing","getName");

        Call<String> getName = userService.getName(hashMap);
        getName.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful())
                    USession.getInstance().setName(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("GET_NAME_ERR","GET_NAME_EXCEPTION");
            }
        });
    }

    public void getDatas(String id){
        USession.getInstance().setId(id);
        USession.getInstance().setIsLogin(true);

        getName();
        getGroupNums();
        getGroups();
        getFriends();
    }
}
