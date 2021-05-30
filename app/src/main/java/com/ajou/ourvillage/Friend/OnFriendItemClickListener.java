package com.ajou.ourvillage.Friend;

import android.view.View;

import com.ajou.ourvillage.Main.MainPostAdapter;

public interface OnFriendItemClickListener {
    public void onItemClick(FriendAdapter.ViewHolder holder, View view, int position);
}
