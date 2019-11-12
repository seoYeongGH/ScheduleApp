package com.example.scheduleapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleapp.fragment.AfterLoginFragment;
import com.example.scheduleapp.fragment.BeforeLoginFragment;
import com.example.scheduleapp.structure.USession;

public class MainActivity extends AppCompatActivity {
    USession session;

    BeforeLoginFragment beforeFragment;
    AfterLoginFragment afterFragment;

    LinearLayout txtBefore;
    LinearLayout txtAfter;
    TextView txtViewId;
    Button btnSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = USession.getInstance();

        beforeFragment = new BeforeLoginFragment();
        afterFragment = new AfterLoginFragment();

        txtBefore = findViewById(R.id.txtBefore);
        txtAfter = findViewById(R.id.txtAfter);

        txtViewId = findViewById(R.id.txtViewId);
        btnSync = findViewById(R.id.btnSync);

        View txtLayout = findViewById(R.id.txtBefore);
        onTxtClicked(txtLayout);

        setDisplay();
    }

    private void setDisplay(){
        if(!session.getIsLogin()){
                getSupportFragmentManager().beginTransaction().remove(afterFragment).commit();
                getSupportFragmentManager().beginTransaction().add(R.id.container,beforeFragment).commit();

            txtBefore.setVisibility(View.VISIBLE);
            txtAfter.setVisibility(View.INVISIBLE);

            txtViewId.setText("로그인 후 이용하세요.");
            btnSync.setVisibility(View.INVISIBLE);
        }
        else{
                getSupportFragmentManager().beginTransaction().remove(beforeFragment).commit();
                getSupportFragmentManager().beginTransaction().add(R.id.container,afterFragment).commit();

            txtBefore.setVisibility(View.INVISIBLE);
            txtAfter.setVisibility(View.VISIBLE);

            txtViewId.setText(USession.getInstance().getId()+"님의 일정입니다.");
            btnSync.setVisibility(View.VISIBLE);
        }
    }

    public void onTxtClicked(View view){
       TextView txtLogin = findViewById(R.id.txtLogin);
       TextView txtJoin = findViewById(R.id.txtJoin);
       TextView txtMypage = findViewById(R.id.txtMypage);
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

        txtMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MyPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = makeAlert();
                alertDialog.show();
            }
        });
    }

    public void onBtnSyncClicked(View view){
        AfterLoginFragment afterFragment =  (AfterLoginFragment)getSupportFragmentManager().findFragmentById(R.id.container);
        if(afterFragment != null)
            afterFragment.refreshData();
    }

    private AlertDialog makeAlert(){
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

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
