package com.ajou.ourvillage.Tasty;

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


public class TastyPostAdapter extends RecyclerView.Adapter<TastyPostAdapter.ViewHolder> {

    private final ArrayList<TastyPostItem> mDataList;

    public TastyPostAdapter(ArrayList<TastyPostItem> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new TastyPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TastyPostAdapter.ViewHolder holder, int position) {
        TastyPostItem item = mDataList.get(position);

        //holder.img_profile.setImageResource(item.getImg_profile());
        Glide.with(holder.itemView).load(item.getImg_content()).into(holder.img_content);
        holder.tv_writer.setText(item.getWriter());
        holder.tv_title.setText(item.getTitle());
        holder.tv_content.setText(item.getContent());
        holder.tv_date.setText(item.getDate());
        holder.tv_likecnt.setText(item.getLikeCnt());
        holder.tv_commentcnt.setText(item.getCommentCount());
        //holder.tv_location.setText(item.getLocation());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_profile, img_content;
        TextView tv_writer, tv_title, tv_content, tv_date, tv_likecnt, tv_commentcnt, tv_location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_profile = itemView.findViewById(R.id.post_img_profile);
            img_content = itemView.findViewById(R.id.post_iv_imgcontents);
            tv_writer = itemView.findViewById(R.id.post_tv_writer);
            tv_title = itemView.findViewById(R.id.post_tv_title);
            tv_content = itemView.findViewById(R.id.post_tv_content);
            tv_date = itemView.findViewById(R.id.post_tv_date);
            tv_likecnt = itemView.findViewById(R.id.post_tv_like_count);
            tv_commentcnt = itemView.findViewById(R.id.post_tv_commentcount);
            tv_location = itemView.findViewById(R.id.tasty_tv_showlocation);
        }
    }

}
