package com.ajou.ourvillage.Friend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajou.ourvillage.Main.MainPostAdapter;
import com.ajou.ourvillage.Main.OnMainItemClickLIstener;
import com.ajou.ourvillage.Main.WriteFeedInfo;
import com.ajou.ourvillage.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> implements OnFriendItemClickListener{
    private final ArrayList<FriendListInfo> mDataList;
    OnFriendItemClickListener listener;

    public FriendAdapter(ArrayList<FriendListInfo> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendAdapter.ViewHolder(view,this);
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

    public void setOnItemClicklistener(OnFriendItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(FriendAdapter.ViewHolder holder, View view, int position) {
        if(listener!= null){
            listener.onItemClick(holder,view,position);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView friend_img_profile;
        TextView friend_nickname, friend_address,friend_tv_more;

        public ViewHolder(@NonNull View itemView, final OnFriendItemClickListener listener) {
            super(itemView);

            friend_img_profile = itemView.findViewById(R.id.friend_img_profile);
            friend_address = itemView.findViewById(R.id.friend_address);
            friend_nickname = itemView.findViewById(R.id.friend_nickname);
            friend_tv_more = itemView.findViewById(R.id.friend_tv_more);

            itemView.findViewById(R.id.friend_tv_more).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(FriendAdapter.ViewHolder.this,v,pos);
                    }
                }
            });
        }
    }


    public FriendListInfo getItem(int position){
        return mDataList.get(position);
    }

}
