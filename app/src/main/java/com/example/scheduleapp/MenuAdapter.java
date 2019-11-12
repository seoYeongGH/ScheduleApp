package com.example.scheduleapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>implements OnMenuItemClickedListener{
    private OnMenuItemClickedListener selectListener;
    Context context;

    ArrayList<String> strMenus = new ArrayList<String>();

    public MenuAdapter(Context context){
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public ViewHolder(View itemView, final OnMenuItemClickedListener listener){
            super(itemView);
            textView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    int position = getAdapterPosition();
                    if(listener !=null)
                        listener.onItemClick(ViewHolder.this, v, position);
                }
            });
        }

        public void setItem(String str){
            textView.setText(str);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View menuView = inflater.inflate(R.layout.one_textview,parent,false);

        return new ViewHolder(menuView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String strMenu = strMenus.get(position);
        holder.setItem(strMenu);
    }

    public void onItemClick(ViewHolder holder, View view, int position){
        if(selectListener != null)
            selectListener.onItemClick(holder,view,position);
    }

    public void setOnMenuItemClickedListener(OnMenuItemClickedListener listener){
        selectListener = listener;
    }
    @Override
    public int getItemCount() {
        return strMenus.size();
    }

    public void setItem(int position,String str){
        strMenus.set(position,str);
    }

    public String getItem(int position){
        return strMenus.get(position);
    }

    public void addItem(String str){
        strMenus.add(str);
    }

    public void setItems(ArrayList<String> strList){
        strMenus = strList;
    }

}