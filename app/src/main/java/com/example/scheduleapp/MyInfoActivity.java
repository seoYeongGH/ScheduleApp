package com.example.scheduleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.USession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

public class MyInfoActivity extends AppCompatActivity {
    TextView txtName;
    TextView txtId;
    TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        txtName = findViewById(R.id.txtName);
        txtId = findViewById(R.id.txtId);
        txtEmail = findViewById(R.id.txtEmail);

        txtId.setText(USession.getInstance().getId());
        txtName.setText(USession.getInstance().getName());

        getCommunication();
    }

    public void onBtnExitClicked(View view){
        finish();
    }

    private void getCommunication (){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<String> getInfo = userService.getInfo("getEmail");
        getInfo.enqueue(new Callback<String>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<String> call, Response<String> response) {
                txtEmail.setText(response.body());
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
