package com.example.scheduleapp.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleapp.GroupSchedulePage;
import com.example.scheduleapp.R;
import com.example.scheduleapp.structure.FriendObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.ViewHolder> {
    Context context;
    ArrayList<FriendObject> friends;

    public NameAdapter(Context context){
        this.context = context;
        friends = new ArrayList<FriendObject>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;

        public ViewHolder(View itemView){
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);

        }

        public void setItem(String name){
            txtName.setText(name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.string_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = friends.get(position).getName();
        holder.setItem(name);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void setNames(ArrayList<FriendObject> friends){
        this.friends = friends;
    }
}
