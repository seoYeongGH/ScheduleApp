package com.example.scheduleapp.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.scheduleapp.R;
import com.google.android.material.tabs.TabLayout;

public class SocialFragment extends Fragment {
    Fragment fragmentGroup;
    Fragment fragmentFriend;

    TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView =  (ViewGroup)inflater.inflate(R.layout.fragment_social, container, false);

        fragmentGroup = new GroupFragment();
        fragmentFriend = new FriendFragment();

        tabLayout = rootView.findViewById(R.id.tabLayout);
        setTabListener();

        getChildFragmentManager().beginTransaction().replace(R.id.subContainer, fragmentGroup).commit();

        return rootView;
    }

    private void setTabListener(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                if(pos == 0){
                    getChildFragmentManager().beginTransaction().replace(R.id.subContainer, fragmentGroup).commit();
                }
                else if(pos == 1)
                    getChildFragmentManager().beginTransaction().replace(R.id.subContainer, fragmentFriend).commit();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
