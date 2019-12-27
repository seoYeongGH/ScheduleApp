package com.example.scheduleapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleapp.InvitePage;
import com.example.scheduleapp.MainActivity;
import com.example.scheduleapp.MyInfoActivity;
import com.example.scheduleapp.CheckPwActivity;
import com.example.scheduleapp.R;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.USession;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyPageFragment extends Fragment {
    TextView txtName;

    TextView txtInvite;
    TextView txtGetInfo;
    TextView txtChangeEmail;
    TextView txtChangePw;
    TextView txtLogout;
    TextView txtWithdraw;

    boolean isInviteExist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_my_page, container, false);

        txtName = rootView.findViewById(R.id.txtName);
        txtName.setText(USession.getInstance().getName());

        txtInvite = rootView.findViewById(R.id.txtInvite);
        txtGetInfo = rootView.findViewById(R.id.txtGetInfo);
        txtChangeEmail = rootView.findViewById(R.id.txtChangeEmail);
        txtChangePw = rootView.findViewById(R.id.txtChangePw);
        txtLogout = rootView.findViewById(R.id.txtLogout);
        txtWithdraw = rootView.findViewById(R.id.txtWithdraw);

        getCommunication();

        setTextClick();

        return rootView;
    }

    private void setTextClick(){
        txtInvite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), InvitePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        txtGetInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        txtChangeEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CheckPwActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("doing","changeEmail");
                startActivity(intent);
            }
        });
        txtChangePw.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getContext(), CheckPwActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("doing","changePw");
                startActivity(intent);
            }
        });

        txtWithdraw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CheckPwActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("doing","withdraw");
                startActivity(intent);
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });
    }

    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("LOGOUT");
        builder.setMessage("로그아웃하시겠습니까?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {USession.getInstance().setId(null);
                USession.getInstance().setIsLogin(false);

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                Toast.makeText(getContext(),"LOGOUT",Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create().show();
    }

    private void getCommunication (){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        HashMap hashMap = new HashMap();
        hashMap.put("doing", "getInviteExist");

        Call<Boolean> getBoolean = userService.getBoolean(hashMap);
        getBoolean.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()) {
                    isInviteExist = response.body();

                    if (isInviteExist)
                        txtInvite.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.new_icon, 0);
                    else
                        txtInvite.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }
}
