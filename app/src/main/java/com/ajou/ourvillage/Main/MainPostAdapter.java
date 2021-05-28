package com.ajou.ourvillage.Main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajou.ourvillage.Apart.ApartPostAdapter;
import com.ajou.ourvillage.Apart.ApartPostItem;
import com.ajou.ourvillage.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainPostAdapter extends RecyclerView.Adapter<MainPostAdapter.ViewHolder> {
    private final ArrayList<WriteFeedInfo> mDataList;

    public MainPostAdapter(ArrayList<WriteFeedInfo> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new MainPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WriteFeedInfo item = mDataList.get(position);


        holder.tv_writer.setText(item.getWriter());
        holder.tv_date.setText(item.getDate());
        holder.tv_title.setText(item.getTitle());
        //holder.img_profile.setImageResource(item.getImg_profile());
        Glide.with(holder.itemView).load(item.getImg_profile()).into(holder.img_content);
        holder.tv_content.setText(item.getContent());
        holder.tv_likecnt.setText(item.getLikeCnt());
        holder.tv_commentcnt.setText(item.getCommentCount());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_profile,img_content;
        TextView tv_writer, tv_title, tv_content, tv_date, tv_likecnt, tv_commentcnt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_writer = itemView.findViewById(R.id.post_tv_writer);
            tv_date = itemView.findViewById(R.id.post_tv_date);
            tv_title = itemView.findViewById(R.id.post_tv_title);
            img_profile = itemView.findViewById(R.id.post_img_profile);
            img_content = itemView.findViewById(R.id.post_iv_imgcontents);
            tv_content = itemView.findViewById(R.id.post_tv_content);
            tv_likecnt = itemView.findViewById(R.id.post_tv_like_count);
            tv_commentcnt = itemView.findViewById(R.id.post_tv_commentcount);
        }
    }
}
