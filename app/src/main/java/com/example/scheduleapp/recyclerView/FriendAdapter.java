package com.example.scheduleapp.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleapp.R;
import com.example.scheduleapp.structure.FriendObject;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{
    private ArrayList<FriendObject> friendObjects ;
    private OnFriendBtnListener listener;

    public FriendAdapter(){
        friendObjects = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        TextView txtId;
        Button btnDelete;

        public ViewHolder(View itemView, final OnFriendBtnListener listener){
            super(itemView);

            txtName= itemView.findViewById(R.id.txtName);
            txtId = itemView.findViewById(R.id.txtId);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null)
                        listener.onBtnClicked(ViewHolder.this, view, position);
                }
            });
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
        View menuView = inflater.inflate(R.layout.friend_view,parent,false);

        return new ViewHolder(menuView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        FriendObject obj = friendObjects.get(position);
        holder.setItem(obj);
    }

    public void setListener(OnFriendBtnListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        if(friendObjects == null)
            friendObjects = new ArrayList<>();

        return friendObjects.size();
    }

    public void setItems(ArrayList<FriendObject> list){
        friendObjects = list;
    }

    public FriendObject getItem(int position){
        return friendObjects.get(position);
    }

    public void removeItem(int position){
        friendObjects.remove(position);
    }
}