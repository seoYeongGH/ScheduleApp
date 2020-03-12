package com.example.scheduleapp.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleapp.R;
import com.example.scheduleapp.structure.FriendObject;

import java.util.ArrayList;

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.ViewHolder> {
    private ArrayList<FriendObject> friends;

    public NameAdapter(){
        friends = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        TextView txtId;

        public ViewHolder(View itemView){
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtId = itemView.findViewById(R.id.txtId);
        }

        void setItem(FriendObject obj){
            String strId = "("+obj.getId()+")";

            txtName.setText(obj.getName());
            txtId.setText(strId);
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
        FriendObject obj = friends.get(position);
        holder.setItem(obj);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void setNames(ArrayList<FriendObject> friends){
        this.friends = friends;
    }
}
