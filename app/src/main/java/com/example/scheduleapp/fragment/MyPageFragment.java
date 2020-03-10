package com.example.scheduleapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.example.scheduleapp.structure.AllInvites;
import com.example.scheduleapp.structure.InviteObject;
import com.example.scheduleapp.structure.USession;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.scheduleapp.structure.Constant.ERR;
import static com.example.scheduleapp.structure.Constant.SHARED_PREF_USER_CODE;
import static com.example.scheduleapp.structure.Constant.SUCCESS;

public class MyPageFragment extends Fragment {
    TextView txtName;

    TextView txtInvite;
    TextView txtGetInfo;
    TextView txtChangeEmail;
    TextView txtChangePw;
    TextView txtLogout;
    TextView txtWithdraw;

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

        getInvites();

        setTextClick();

        return rootView;
    }

    public void onResume(){
        super.onResume();

        setNewIcon(AllInvites.getInstance().isEmpty());
    }
    private void setTextClick(){
        txtInvite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), InvitePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent,100);
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
            public void onClick(DialogInterface dialogInterface, int i) {
                doLogout();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create().show();
    }

    private void getInvites(){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("doing","getInvites");

        Call<ArrayList<InviteObject>> getInvites = userService.get_getInvites(hashMap);
        getInvites.enqueue(new Callback<ArrayList<InviteObject>>() {
            @Override
            public void onResponse(Call<ArrayList<InviteObject>> call, Response<ArrayList<InviteObject>> response) {
                if (response.isSuccessful()) {
                    AllInvites.getInstance().setInvites(response.body());

                    setNewIcon(AllInvites.getInstance().isEmpty());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<InviteObject>> call, Throwable t) {

            }
        });
    }

    private void doLogout(){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Context context = getContext();
        SharedPreferences preferences = context.getSharedPreferences("schPref",context.MODE_PRIVATE);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("doing","logout");
        hashMap.put("userCode",preferences.getString(SHARED_PREF_USER_CODE,""));

        Call<Integer> doService = userService.get_doService(hashMap);
        doService.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && response.body()==SUCCESS){
                    USession.getInstance().setId(null);
                    USession.getInstance().setIsLogin(false);

                    Context context = getContext();

                    SharedPreferences preferences = context.getSharedPreferences("schPref",context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.clear();
                    editor.commit();

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    Toast.makeText(getContext(),"LOGOUT",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("LOG_OUT_ERR",t.getMessage());
            }
        });
    }

    private void setNewIcon(boolean isEmpty){
        if (!isEmpty)
            txtInvite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.small_check, 0, R.drawable.new_icon, 0);
        else
            txtInvite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.small_check, 0, 0, 0);
    }
}
