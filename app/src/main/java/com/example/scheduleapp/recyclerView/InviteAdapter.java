package com.example.scheduleapp.recyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduleapp.R;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.AllGroups;
import com.example.scheduleapp.structure.GroupObject;
import com.example.scheduleapp.structure.InviteObject;
import com.example.scheduleapp.structure.USession;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.ViewHolder> {
    Context context;
    ArrayList<InviteObject> invitations;

    OnInviteBtnListener listener;

    public InviteAdapter(Context context){
        this.context = context;
        invitations = new ArrayList<InviteObject>();
        setListener();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtInvite;
        Button btnDeny;
        Button btnAccept;

        public ViewHolder(final View itemView, final OnInviteBtnListener listener){
            super(itemView);

            txtInvite = itemView.findViewById(R.id.txtInvite);
            btnDeny = itemView.findViewById(R.id.btnDeny);
            btnAccept = itemView.findViewById(R.id.btnAccept);

            btnDeny.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if(listener != null) {
                        listener.onBtnClicked(ViewHolder.this, view, position, "denyInvite");
                    }
                }
            });

            btnAccept.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onBtnClicked(ViewHolder.this, view, position, "acceptInvite");
                    }
                }
            });
        }

        public void setItem(InviteObject obj){
            txtInvite.setText(obj.getManagerId()+"님이 "+obj.getGroupName()+"그룹으로 초대하고 싶어합니다.");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.invite_view,parent,false);

        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InviteObject obj = invitations.get(position);
        holder.setItem(obj);
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }

    public InviteObject getItem(int position){
        return invitations.get(position);
    }

    public void setItems(ArrayList<InviteObject> list){
        invitations = list;
    }

    public void removeItem(int position){
        invitations.remove(position);
    }

    public void setListener() {
        listener = new OnInviteBtnListener() {
            @Override
            public void onBtnClicked(ViewHolder holder, View view, int position, String doing) {
                HashMap hashMap = new HashMap();
                hashMap.put("doing", doing);
                hashMap.put("groupNum", invitations.get(position).getGroupNum());

                doService(hashMap, position);
            }

        };
    }

    private void doService(final HashMap hashMap, final int position){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.doService(hashMap);
        doService.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    if("acceptInvite".equals(hashMap.get("doing")))
                        acceptProcess(invitations.get(position));

                    invitations.remove(position);
                    notifyDataSetChanged();
                }
                else{
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void acceptProcess(InviteObject iObj){
        boolean isManager;
        GroupObject gObj = new GroupObject();

        gObj.setGroupNum(iObj.getGroupNum());
        gObj.setGroupName(iObj.getGroupName());

        AllGroups.getInstance().addMemberGroup(gObj);
    }

}
