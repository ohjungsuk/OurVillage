package com.ajou.ourvillage.Friend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajou.ourvillage.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.ViewHolder> implements OnUserItemClickListener{
    private ArrayList<UserListInfo> mDataList;
    OnUserItemClickListener listener;

    public AllUsersAdapter(ArrayList<UserListInfo> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public AllUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new AllUsersAdapter.ViewHolder(view,this);
    }


    @Override
    public void onBindViewHolder(@NonNull AllUsersAdapter.ViewHolder holder, int position) {
        UserListInfo item = mDataList.get(position);
        holder.setItem(item);

        //holder.friend_img_profile.setImageResource(item.getFriend_img_profile());
//        holder.friend_address.setText(item.getAddress());
//        holder.friend_nickname.setText(item.getNickname());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setOnItemClicklistener(OnUserItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder,view,position);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView friend_img_profile;
        TextView friend_nickname, friend_address;

        public ViewHolder(@NonNull View itemView,final OnUserItemClickListener listener) {
            super(itemView);

            friend_img_profile = itemView.findViewById(R.id.friend_img_profile);
            friend_address = itemView.findViewById(R.id.friend_address);
            friend_nickname = itemView.findViewById(R.id.friend_nickname);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, pos);
                    }
                }
            });
        }
        public void setItem(UserListInfo item){
            friend_nickname.setText(item.getNickname());
            friend_address.setText(item.getAddress());
        }

    }
    public void addItem(UserListInfo item){
        mDataList.add(item);
    }
    public void setItems(ArrayList<UserListInfo> mDataList){
        this.mDataList = mDataList;
    }
    public UserListInfo getItem(int position){
        return mDataList.get(position);
    }
    public void setItem(int position, UserListInfo item){
        mDataList.set(position,item);
    }

}
