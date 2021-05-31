package com.ajou.ourvillage.Main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajou.ourvillage.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> implements OnCommentDeleteClickListener {
    private final ArrayList<WriteCommentInfo> mDataList;
    OnCommentDeleteClickListener listener;

    public CommentAdapter(ArrayList<WriteCommentInfo> mDataList) {
        this.mDataList = mDataList;
    }


    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);

        return new CommentAdapter.ViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        WriteCommentInfo item = mDataList.get(position);

        holder.comment_nickname.setText(item.getWriter());
        holder.comment_date.setText(item.getDate());
        holder.comment.setText(item.getComment());

    }
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onCommentDeleteClick(ViewHolder holder, View view, int position) {
        if(listener!= null){
            listener.onCommentDeleteClick(holder,view,position);
        }
    }

    public void setOnCommentDeleteClicklistener(OnCommentDeleteClickListener listener){
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView comment_nickname, comment_date, comment_tv_delete, comment;

        public ViewHolder(@NonNull View itemView,OnCommentDeleteClickListener listener) {
            super(itemView);
            comment_nickname = itemView.findViewById(R.id.comment_nickname);
            comment_date = itemView.findViewById(R.id.comment_date);
            comment_tv_delete = itemView.findViewById(R.id.comment_tv_delete);
            comment = itemView.findViewById(R.id.comment);

            itemView.findViewById(R.id.comment_tv_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(listener != null){
                        listener.onCommentDeleteClick(ViewHolder.this,v,pos);
                    }
                }
            });
        }
    }

    public WriteCommentInfo getItem(int position){
        return mDataList.get(position);
    }

}
