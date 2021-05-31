package com.ajou.ourvillage.Main;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class MainPostAdapter extends RecyclerView.Adapter<MainPostAdapter.ViewHolder> implements OnMainItemClickLIstener,OnCommentItemClickListener{
    private final ArrayList<WriteFeedInfo> mDataList;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private String mpa_nickname = null;
    private String comment = null;
    OnMainItemClickLIstener listener;
    OnCommentItemClickListener listener2;


    public MainPostAdapter(ArrayList<WriteFeedInfo> mDataList) {
        this.mDataList = mDataList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);

        return new MainPostAdapter.ViewHolder(view,this,this);
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

    public void setOnCommentClicklistener(OnCommentItemClickListener listener2){
        this.listener2 = listener2;
    }

    @Override
    public void onCommentClick(ViewHolder holder, View view, int position) {
        if(listener2!=null){
             listener2.onCommentClick(holder,view,position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView post_btn_more,post_img_like,post_img_comment;
        ImageView img_profile,img_content;
        TextView tv_writer, tv_title, tv_content, tv_date, tv_likecnt, tv_commentcnt,post_tv_write_comment,post_tv_comment_cnt;

        public ViewHolder(@NonNull View itemView,final OnMainItemClickLIstener listener,final OnCommentItemClickListener listener1) {
            super(itemView);

            post_tv_write_comment = itemView.findViewById(R.id.post_tv_write_comment);
            post_btn_more = itemView.findViewById(R.id.post_btn_more);
            tv_writer = itemView.findViewById(R.id.post_tv_writer);
            tv_date = itemView.findViewById(R.id.post_tv_date);
            tv_title = itemView.findViewById(R.id.post_tv_title);
            img_profile = itemView.findViewById(R.id.post_img_profile);
            img_content = itemView.findViewById(R.id.post_iv_imgcontents);
            tv_content = itemView.findViewById(R.id.post_tv_content);



            itemView.findViewById(R.id.post_btn_more).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this,v,pos);
                    }
                }
            });

//            itemView.findViewById(R.id.post_layout_comment1).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    if(listener1 != null){
//                        listener1.onCommentClick(ViewHolder.this,v,pos);
//                    }
//                }
//            });

            itemView.findViewById(R.id.post_tv_write_comment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(listener1 != null){
                        listener1.onCommentClick(ViewHolder.this,v,pos);
                    }
                }
            });

        }
    }

    public WriteFeedInfo getItem(int position){
        return mDataList.get(position);
    }

}
