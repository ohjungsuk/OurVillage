package com.ajou.ourvillage.Main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajou.ourvillage.Apart.ApartPostAdapter;
import com.ajou.ourvillage.Apart.ApartPostItem;
import com.ajou.ourvillage.Friend.FriendListInfo;
import com.ajou.ourvillage.Friend.OnUserItemClickListener;
import com.ajou.ourvillage.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class MainPostAdapter extends RecyclerView.Adapter<MainPostAdapter.ViewHolder> implements OnMainItemClickLIstener{
    private final ArrayList<WriteFeedInfo> mDataList;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private String mpa_nickname = null;
    OnMainItemClickLIstener listener;

    public MainPostAdapter(ArrayList<WriteFeedInfo> mDataList) {
        this.mDataList = mDataList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);

        return new MainPostAdapter.ViewHolder(view,this);
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

    public void setOnItemClicklistener(OnMainItemClickLIstener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener!= null){
            listener.onItemClick(holder,view,position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView post_btn_more;
        ImageView img_profile,img_content;
        TextView tv_writer, tv_title, tv_content, tv_date, tv_likecnt, tv_commentcnt;

        public ViewHolder(@NonNull View itemView,final OnMainItemClickLIstener listener) {
            super(itemView);

            post_btn_more = itemView.findViewById(R.id.post_btn_more);
            tv_writer = itemView.findViewById(R.id.post_tv_writer);
            tv_date = itemView.findViewById(R.id.post_tv_date);
            tv_title = itemView.findViewById(R.id.post_tv_title);
            img_profile = itemView.findViewById(R.id.post_img_profile);
            img_content = itemView.findViewById(R.id.post_iv_imgcontents);
            tv_content = itemView.findViewById(R.id.post_tv_content);
            tv_likecnt = itemView.findViewById(R.id.post_tv_like_count);
            tv_commentcnt = itemView.findViewById(R.id.post_tv_commentcount);


            itemView.findViewById(R.id.post_btn_more).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this,v,pos);
                    }
                }
            });

        }
    }

    public WriteFeedInfo getItem(int position){
        return mDataList.get(position);
    }

}
