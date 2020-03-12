package com.example.scheduleapp.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleapp.R;
import com.example.scheduleapp.structure.GroupObject;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private ArrayList<GroupObject> groups;
    private OnGroupListener listener;

    private int managerSize=0;

    public GroupAdapter(){
        groups = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        Button btnDelete;
        Button btnMember;

        public ViewHolder(View itemView, final OnGroupListener listener){
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnMember = itemView.findViewById(R.id.btnMember);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onItemClicked(ViewHolder.this, getAdapterPosition());
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)
                        listener.onBtnDeleteClicked(ViewHolder.this,view,getAdapterPosition());
                }
            });

            btnMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null)
                        listener.onBtnMemberClicked(ViewHolder.this,view,getAdapterPosition());
                }
            });
        }

        void setItem(GroupObject obj,boolean isManager){
            txtName.setText(obj.getGroupName());

            if(isManager)
                txtName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.manager,0,0,0);
            else
                txtName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.member,0,0,0);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.group_view,parent,false);

        return new GroupAdapter.ViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupObject obj = groups.get(position);

        if(position<managerSize)
            holder.setItem(obj,true);
        else
            holder.setItem(obj,false);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void setGroups(ArrayList<GroupObject> isManagers, ArrayList<GroupObject> notManagers) {
        if(isManagers != null) {
            this.groups.addAll(isManagers);
            managerSize = isManagers.size();
        }
        else{
            managerSize = 0;
        }

        if(notManagers != null)
            groups.addAll(notManagers);
    }

    public int getManagerSize(){
        return managerSize;
    }

    public void setListener(OnGroupListener listener){
        this.listener = listener;
    }

    public GroupObject getItem(int position){
        return  groups.get(position);
    }

    public void clearList(){
        groups.clear();
    }
}
