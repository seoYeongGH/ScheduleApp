package com.example.scheduleapp.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleapp.R;
import com.example.scheduleapp.structure.FriendObject;

import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    private ArrayList<FriendObject> members;
    private Boolean[] selectList;
    private int selectSize = 0;

    public MemberAdapter( ArrayList<FriendObject> friendList){
        members = friendList;

        int size = members.size();
        selectList = new Boolean[size];

        for(int i=0; i<size; i++){
            selectList[i] = false;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View memberView = inflater.inflate(R.layout.select_member_view,parent,false);

        return new ViewHolder(memberView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendObject obj = members.get(position);
        holder.setItem(obj);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        TextView txtId;
        CheckBox checkView;

        public ViewHolder(final View itemView){
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtId = itemView.findViewById(R.id.txtId);
            checkView = itemView.findViewById(R.id.chkMember);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                        if(!selectList[position])
                            checkView.setChecked(true);
                        else
                            checkView.setChecked(false);

                }
            });

            checkView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                        selectList[getAdapterPosition()] = true;
                        selectSize++;
                    }
                    else {
                        selectList[getAdapterPosition()] = false;
                        selectSize--;
                    }
                }
            });
        }

        void setItem(FriendObject obj){
            String strId = "("+obj.getId()+")";

            txtName.setText(obj.getName());
            txtId.setText(strId);
        }

    }

    public String getIds(){
        int selectSize = selectList.length;
        String strIds = "";

        for(int i=0; i<selectSize; i++){
            if(selectList[i]) {
                strIds = strIds.concat(members.get(i).getId()).concat(",");
            }
        }

        return strIds;
    }

    public int getInviteNum(){
        return selectSize;
    }

    public void setItem(int position,FriendObject obj){
        members.set(position,obj);
    }

    public FriendObject getItem(int position){
        return members.get(position);
    }

    public void setItems(ArrayList<FriendObject> list){
        members = list;
    }

}
