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

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private final ArrayList<FriendListInfo> mDataList;

    public FriendAdapter(ArrayList<FriendListInfo> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        FriendListInfo item = mDataList.get(position);

        //holder.friend_img_profile.setImageResource(item.getFriend_img_profile());
        holder.friend_address.setText(item.getAddress());
        holder.friend_nickname.setText(item.getFriend_nickname());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView friend_img_profile;
        TextView friend_nickname, friend_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            friend_img_profile = itemView.findViewById(R.id.friend_img_profile);
            friend_address = itemView.findViewById(R.id.friend_address);
            friend_nickname = itemView.findViewById(R.id.friend_nickname);

        }
    }
}
