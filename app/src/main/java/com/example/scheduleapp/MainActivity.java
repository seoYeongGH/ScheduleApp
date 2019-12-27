package com.example.scheduleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleapp.fragment.AfterLoginFragment;
import com.example.scheduleapp.fragment.BeforeLoginFragment;
import com.example.scheduleapp.fragment.MyPageFragment;
import com.example.scheduleapp.fragment.SocialFragment;
import com.example.scheduleapp.structure.USession;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.scheduleapp.structure.Constant.FOR_USER;

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
        imageView1.setLayoutParams(layoutParams);

        imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.back_before2);
        imageView2.setLayoutParams(layoutParams);

        imageView3 = new ImageView(this);
        imageView3.setImageResource(R.drawable.back_before1);
        imageView3.setLayoutParams(layoutParams);

        container = findViewById(R.id.container);

        navigationView = findViewById(R.id.navigationView);

        setDisplay();
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
/*
    private void onTxtClicked(){
       TextView txtLogin = findViewById(R.id.txtLogin);
       TextView txtJoin = findViewById(R.id.txtJoin);
       TextView txtLogout = findViewById(R.id.txtLogout);

       txtLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(),LoginPage.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

               startActivity(intent);
           }
       });

        txtJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),JoinPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });
    }

    public void onBtnSyncClicked(View view){
        if(afterFragment != null)
            afterFragment.refreshData();
    }

    public void onBtnMenuClicked(View view){
        Intent intent = new Intent(getApplicationContext(), MenuPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
    }

    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("LOGOUT");
        builder.setMessage("로그아웃하시겠습니까?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {USession.getInstance().setId(null);
                USession.getInstance().setIsLogin(false);

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                Toast.makeText(getApplicationContext(),"LOGOUT",Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create().show();
    }*/
}
