package com.ajou.ourvillage.Tasty;

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


public class TastyPostAdapter extends RecyclerView.Adapter<TastyPostAdapter.ViewHolder> {

    private final ArrayList<TastyPostItem> mDataList;
    private Context mContext;

    public TastyPostAdapter(ArrayList<TastyPostItem> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tasty, parent, false);
        return new TastyPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TastyPostItem item = mDataList.get(position);

        holder.tv_writer.setText(item.getWriter());
        holder.tv_date.setText(item.getDate());
        holder.tv_address.setText(item.getAddress());
        holder.tv_rate.setText(item.getRate());
        holder.tv_review.setText(item.getReview());
        holder.tv_recommend.setText(item.getRecommend());
        Glide.with(holder.itemView).load(item.getFoodImage()).into(holder.img_content);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mContext = view.getContext();
//
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_content;
        TextView tv_writer, tv_date, tv_address, tv_rate, tv_review, tv_recommend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_writer = itemView.findViewById(R.id.tasty_tv_writer);
            tv_date = itemView.findViewById(R.id.tasty_tv_date);
            tv_address = itemView.findViewById(R.id.tasty_tv_showlocation);
            tv_rate = itemView.findViewById(R.id.tasty_tv_score);
            tv_review = itemView.findViewById(R.id.tasty_tv_content);
            tv_recommend = itemView.findViewById(R.id.tasty_tv_recommend);
            img_content = itemView.findViewById(R.id.tasty_iv_imgcontents);
        }
    }

}
