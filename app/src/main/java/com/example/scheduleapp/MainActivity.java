package com.example.scheduleapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleapp.fragment.AfterLoginFragment;
import com.example.scheduleapp.fragment.BeforeLoginFragment;
import com.example.scheduleapp.structure.USession;

public class MainActivity extends AppCompatActivity {
    BeforeLoginFragment beforeFragment;
    AfterLoginFragment afterFragment;

    ConstraintLayout menuBefore;
    ConstraintLayout menuAfter;

    TextView txtViewId;
    Button btnSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beforeFragment = new BeforeLoginFragment();
        afterFragment = new AfterLoginFragment();

        menuBefore = findViewById(R.id.menuBefore);
        menuAfter = findViewById(R.id.menuAfter);

        txtViewId = findViewById(R.id.txtViewId);
        btnSync = findViewById(R.id.btnSync);

        onTxtClicked();
        setDisplay();
    }

    private void setDisplay(){
        if(!USession.getInstance().getIsLogin()){
                getSupportFragmentManager().beginTransaction().remove(afterFragment).commit();
                getSupportFragmentManager().beginTransaction().add(R.id.container,beforeFragment).commit();

            menuBefore.setVisibility(View.VISIBLE);
            menuAfter.setVisibility(View.INVISIBLE);

            txtViewId.setText("로그인 후 이용하세요.");
            btnSync.setVisibility(View.INVISIBLE);
        }
        else{
            getSupportFragmentManager().beginTransaction().remove(beforeFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.container,afterFragment).commit();

            menuBefore.setVisibility(View.INVISIBLE);
            menuAfter.setVisibility(View.VISIBLE);

            String idText = USession.getInstance().getId()+"님의 일정입니다.";
            txtViewId.setText(idText);
            btnSync.setVisibility(View.VISIBLE);
        }
    }

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
    }
}
