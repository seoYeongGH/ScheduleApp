package com.example.scheduleapp.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleapp.GroupSchedulePage;
import com.example.scheduleapp.R;
import com.example.scheduleapp.structure.GroupObject;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    Context context;
    OnGroupListener listener;

    ArrayList<GroupObject> groups;
    int managerSize=0;

    public GroupAdapter(Context context){
        this.context = context;
        groups = new ArrayList<GroupObject>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        Button btnDelete;
        Button btnMember;

        public ViewHolder(final Context context, View itemView, final OnGroupListener listener){
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

        public void setItem(GroupObject obj,boolean isManager){
            txtName.setText(" "+obj.getGroupName());

            if(isManager)
                txtName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.manager,0,0,0);
            else
                txtName.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.group_view,parent,false);

        return new GroupAdapter.ViewHolder(context, view,listener);
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
        this.groups.addAll(isManagers);
        managerSize = isManagers.size();

        groups.addAll(notManagers);
    }

    public int getManagerSize(){
        return managerSize;
    }
    public void addManagerGroup(int index, GroupObject obj){
        groups.add(index,obj);
        managerSize++;
    }

    public void addMemberGroup(int index, GroupObject obj){
        groups.add(index,obj);
    }

    public void setListener(OnGroupListener listener){
        this.listener = listener;
    }

    public GroupObject getItem(int position){
        return  groups.get(position);
    }

    public void removeItem(int position){
        if(position<managerSize)
            managerSize--;
        groups.remove(position);
    }
}
