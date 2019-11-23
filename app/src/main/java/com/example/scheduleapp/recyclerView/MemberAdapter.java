package com.example.scheduleapp.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleapp.R;
import com.example.scheduleapp.structure.FriendObject;

import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    Context context;

    boolean isView;
    ArrayList<FriendObject> members;
    ArrayList<String> selectIds;

    public MemberAdapter(Context context, boolean isView, ArrayList<FriendObject> friendList){
        this.context = context;
        this.isView = isView;
        members = friendList;
        selectIds = new ArrayList<String>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View memberView = inflater.inflate(R.layout.select_member_view,parent,false);

        return new ViewHolder(memberView, isView, selectIds);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendObject obj = members.get(position);

        if(isView)
            holder.setViewItem(obj);
        else
            holder.setItem(obj);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox chkMember;
        TextView txtName;
        TextView txtId;

        public ViewHolder(View itemView, final boolean isView, final ArrayList<String> ids){
            super(itemView);

            chkMember = itemView.findViewById(R.id.chkMember);
            txtName = itemView.findViewById(R.id.txtName);
            txtId = itemView.findViewById(R.id.txtId);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(!isView) {
                        if(chkMember.isChecked()){
                            ids.remove(txtId.getText().toString());
                            chkMember.setChecked(false);
                        }
                        else{
                            ids.add(txtId.getText().toString());
                            chkMember.setChecked(true);
                        }
                    }
                }
            });
        }

        public void setItem(FriendObject obj){
            chkMember.setVisibility(View.VISIBLE);
            txtName.setText(obj.getName());
            txtId.setText("("+obj.getId()+")");
        }

        public void setViewItem(FriendObject obj){
            chkMember.setVisibility(View.GONE);
            txtName.setText(obj.getName());
            txtId.setText("("+obj.getId()+")");
        }
    }

    public String getIds(){
        int idSize = selectIds.size();
        String strIds = "";

        for(int i=0; i<idSize; i++){
                strIds += selectIds.get(i);
        }

        return strIds;
    }

    public int getInvitNum(){
        return selectIds.size();
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
