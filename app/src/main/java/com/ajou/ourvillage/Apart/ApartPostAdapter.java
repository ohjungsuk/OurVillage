package com.ajou.ourvillage.Apart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajou.ourvillage.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ApartPostAdapter extends RecyclerView.Adapter<ApartPostAdapter.ViewHolder>{

    private final ArrayList<ApartPostItem> mDataList;
    private Context mContext;

    public ApartPostAdapter(ArrayList<ApartPostItem> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApartPostItem item = mDataList.get(position);

        holder.tv_writer.setText(item.getWriter());
        holder.tv_title.setText(item.getTitle());
        holder.tv_content.setText(item.getContent());
        holder.tv_date.setText(item.getDate());
        holder.tv_likecnt.setText(item.getLikeCnt());
        holder.tv_commentcnt.setText(item.getCommentCount());
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
