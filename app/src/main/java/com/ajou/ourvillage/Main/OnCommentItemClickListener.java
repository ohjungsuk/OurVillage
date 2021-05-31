package com.ajou.ourvillage.Main;

import android.view.View;

public interface OnCommentItemClickListener {
    public void onCommentClick(MainPostAdapter.ViewHolder holder, View view, int position);
}
