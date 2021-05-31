package com.ajou.ourvillage.Community;

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

import java.util.ArrayList;

public class CommunityPostAdapter extends RecyclerView.Adapter<CommunityPostAdapter.ViewHolder> {
    private final ArrayList<CommunityPostItem> mDataList;

    public CommunityPostAdapter(ArrayList<CommunityPostItem> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public CommunityPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new CommunityPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommunityPostItem item = mDataList.get(position);

        holder.img_profile.setImageResource(item.getImg_profile());
        holder.tv_writer.setText(item.getWriter());
        holder.tv_title.setText(item.getTitle());
        holder.tv_content.setText(item.getContent());
        holder.tv_date.setText(item.getDate());

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_profile;
        TextView tv_writer, tv_title, tv_content, tv_date, tv_likecnt, tv_commentcnt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_profile = itemView.findViewById(R.id.post_img_profile);
            tv_writer = itemView.findViewById(R.id.post_tv_writer);
            tv_title = itemView.findViewById(R.id.post_tv_title);
            tv_content = itemView.findViewById(R.id.post_tv_content);
            tv_date = itemView.findViewById(R.id.post_tv_date);

        }
    }
}