package com.example.scheduleapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scheduleapp.recyclerView.MemberAdapter;
import com.example.scheduleapp.retro.RetroController;
import com.example.scheduleapp.retro.UserService;
import com.example.scheduleapp.structure.AllFriends;
import com.example.scheduleapp.structure.AllGroups;
import com.example.scheduleapp.structure.GroupObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

import static com.example.scheduleapp.structure.Constant.CODE_ISADDED;

public class CreateGroupPage extends AppCompatActivity {
    EditText iptGpName;
    TextView txtWarn;

    RecyclerView recMember;
    MemberAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_gp_activity);

        iptGpName = findViewById(R.id.iptGpName);
        txtWarn = findViewById(R.id.txtWarn);
        recMember = findViewById(R.id.recMember);

        adapter = new MemberAdapter(AllFriends.getInstance().getFriends());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        recMember.setLayoutManager(layoutManager);
        recMember.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));

        recMember.setAdapter(adapter);
    }

    public void onBtnCancelClicked(View view){
        finish();
    }

    public void onBtnCreateClicked(View view){
        String name = iptGpName.getText().toString();

        if(name.length()==0){
            txtWarn.setVisibility(View.VISIBLE);
            return;
        }
        txtWarn.setVisibility(View.GONE);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("doing","createGroup");
        hashMap.put("name",name);
        hashMap.put("ids",adapter.getIds());

        SendInvite(hashMap);
    }

    private void SendInvite(final HashMap<String,Object> hashMap){
        Retrofit retrofit = RetroController.getInstance().getRetrofit();
        UserService userService = retrofit.create(UserService.class);

        Call<Integer> doService = userService.get_doService(hashMap);
        doService.enqueue(new Callback<Integer>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    GroupObject newGroup = new GroupObject();
                    newGroup.setGroupNum(response.body());
                    newGroup.setGroupName(hashMap.get("name").toString());

                    AllGroups.getInstance().addManagerGroup(newGroup);

                    showAlert(adapter.getInviteNum());
                }
                else
                    Toast.makeText(getApplicationContext(),"Error!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void showAlert(int friendNum){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notice");
        builder.setMessage("총 "+friendNum+"명의 친구에게 가입 신청을 보냈습니다.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setResult(CODE_ISADDED);

                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
