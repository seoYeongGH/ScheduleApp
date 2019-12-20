package com.example.scheduleapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.scheduleapp.FindPage;
import com.example.scheduleapp.JoinPage;
import com.example.scheduleapp.LoginPage;
import com.example.scheduleapp.R;

public class BeforeLoginFragment extends Fragment {

    public BeforeLoginFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_before_login, container, false);

        setBtnListeners(rootView);
        
        return rootView;
    }

    private void setBtnListeners(ViewGroup rootView){
        Button btnLogin = rootView.findViewById(R.id.btnLogin);
        Button btnJoin = rootView.findViewById(R.id.btnJoin);
        Button btnFind = rootView.findViewById(R.id.btnFind);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), JoinPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FindPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

    }
}
