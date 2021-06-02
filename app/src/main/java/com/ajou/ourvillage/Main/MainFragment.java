package com.ajou.ourvillage.Main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.ourvillage.Friend.ShowAllUsersActivity;
import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;



import java.util.ArrayList;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class MainFragment extends Fragment {

    private TextView btn_write,btn_searchFriend;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseUser firebaseUser;
    private String mf_nickname = null;
    private boolean is_upload = false;
    private int cnt_upload = 0;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        btn_searchFriend = view.findViewById(R.id.main_btn_searchFriend);
        btn_searchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowAllUsersActivity.class);
                startActivity(intent);
            }
        });
        btn_write = view.findViewById(R.id.main_btn_write);
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddMyFeed.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<WriteFeedInfo> dataList = new ArrayList<>();
        is_upload = false;

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot friendsfile : task.getResult()) {
                                Log.d("test", "point2");
                                for (UserInfo profile : firebaseUser.getProviderData()) {
                                    mf_nickname = profile.getDisplayName();
                                }
                                if(mf_nickname.equals(friendsfile.getData().get("my_nickname").toString())){
                                    Log.d("test", mf_nickname);
                                    CollectionReference collectionReference = db.collection("Feed");
                                    collectionReference.orderBy("date", Query.Direction.DESCENDING)
                                    //db.collection("Feed")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        for (QueryDocumentSnapshot feedfile : task.getResult()) {
                                                            if(friendsfile.getData().get("friend_nickname").equals(feedfile.getData().get("writer").toString())){
                                                                dataList.add(new WriteFeedInfo(
                                                                        feedfile.getData().get("writer").toString(),
                                                                        feedfile.getData().get("date").toString(),
                                                                        feedfile.getData().get("title").toString(),
                                                                        feedfile.getData().get("img_profile").toString(),
                                                                        feedfile.getData().get("content").toString(),
                                                                        feedfile.getData().get("likeCnt").toString(),
                                                                        feedfile.getData().get("commentCount").toString(),
                                                                        feedfile.getId().toString()
                                                                        ));
                                                            }
                                                            if(mf_nickname.equals(feedfile.getData().get("writer").toString())){
                                                                if(!is_upload){
                                                                    dataList.add(new WriteFeedInfo(
                                                                            feedfile.getData().get("writer").toString(),
                                                                            feedfile.getData().get("date").toString(),
                                                                            feedfile.getData().get("title").toString(),
                                                                            feedfile.getData().get("img_profile").toString(),
                                                                            feedfile.getData().get("content").toString(),
                                                                            feedfile.getData().get("likeCnt").toString(),
                                                                            feedfile.getData().get("commentCount").toString(),
                                                                            feedfile.getId().toString()
                                                                    ));
                                                                    is_upload =true;
                                                                }
                                                                cnt_upload ++;
                                                            }
                                                        }
                                                        RecyclerView recyclerView = getActivity().findViewById(R.id.main_recyclerview);
                                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                                        recyclerView.setLayoutManager(layoutManager);

                                                        MainPostAdapter mainPostAdapter = new MainPostAdapter(dataList);
                                                        recyclerView.setAdapter(mainPostAdapter);
                                                        recyclerView.getAdapter().notifyDataSetChanged();
                                                        mainPostAdapter.setOnCommentClicklistener(new OnCommentItemClickListener() {
                                                              @Override
                                                              public void onCommentClick(MainPostAdapter.ViewHolder holder, View view, int position) {
                                                                  WriteFeedInfo pos = mainPostAdapter.getItem(position);
                                                                  Log.d("commenttest",String.valueOf(pos.getId()));
                                                                  Intent intent = new Intent(getActivity(),WriteComment.class);
                                                                  intent.putExtra("feed_id",pos.getId());
                                                                  startActivity(intent);
                                                              }
                                                        });

                                                        mainPostAdapter.setOnItemClicklistener(new OnMainItemClickLIstener() {
                                                            @Override
                                                            public void onItemClick(MainPostAdapter.ViewHolder holder, View view, int position) {
                                                                WriteFeedInfo pos = mainPostAdapter.getItem(position);
                                                                Log.d("rtest", String.valueOf(pos.getId()));
                                                                //showPopup(view,pos);
                                                                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                                                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                                    @Override
                                                                    public boolean onMenuItemClick(MenuItem item) {
                                                                        switch (item.getItemId()) {
                                                                            case R.id.menu_refactor:

                                                                                return true;
                                                                            case R.id.menu_delete:
                                                                                if (mf_nickname.equals(pos.getWriter())) {
                                                                                    db.collection("Feed").document(pos.getId())
                                                                                            .delete()
                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {
                                                                                                    Log.d("rrtest", pos.getId());
                                                                                                    Toast.makeText(view.getContext(), "게시글을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            })
                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                @Override
                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                    Toast.makeText(view.getContext(), "게시글 삭제를 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            });
                                                                                } else {
                                                                                    Toast.makeText(view.getContext(), "내 계정만 삭제할수 있습니다.", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                                return true;
                                                                            default:
                                                                                return false;
                                                                        }
                                                                    }
                                                                });
                                                                MenuInflater inflater = popupMenu.getMenuInflater();
                                                                inflater.inflate(R.menu.post_menu, popupMenu.getMenu());
                                                                popupMenu.show();
                                                            }

                                                        });
                                                    }
                                                }
                                            });
                                }

                            }
                            Log.d("myfeedcheck", String.valueOf(cnt_upload));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}