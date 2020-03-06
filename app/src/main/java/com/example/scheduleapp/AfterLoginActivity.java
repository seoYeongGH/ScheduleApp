package com.example.scheduleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.scheduleapp.fragment.AfterLoginFragment;
import com.example.scheduleapp.fragment.MyPageFragment;
import com.example.scheduleapp.fragment.SocialFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.scheduleapp.structure.Constant.FOR_USER;

public class AfterLoginActivity extends AppCompatActivity {
    FrameLayout container;

    AfterLoginFragment afterFragment;
    SocialFragment socialFragment;
    MyPageFragment myPageFragment;

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        afterFragment = new AfterLoginFragment(FOR_USER);
        socialFragment = new SocialFragment();
        myPageFragment = new MyPageFragment();

        container = findViewById(R.id.container);

        navigationView = findViewById(R.id.navigationView);

        setDisplay();
    }

    public void onBackPressed() {
        BackButton.getInstance().onBtnPressed(getApplicationContext(),this);
    }

    public void setDisplay(){
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

}
