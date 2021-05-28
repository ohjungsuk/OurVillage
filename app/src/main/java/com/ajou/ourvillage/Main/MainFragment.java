package com.ajou.ourvillage.Main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ajou.ourvillage.Friend.ShowAllUsers;
import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;
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
                Intent intent = new Intent(getActivity(), ShowAllUsers.class);
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


        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Feed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //    WriteFeedInfo writeFeedInfo = document.getData().get(WriteFeedInfo.class);

                                db.collection("friends")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                                        String m_nickname = null;
                                                        for (UserInfo profile : firebaseUser.getProviderData()) {
                                                            m_nickname = profile.getDisplayName();
                                                        }//|| document.getData().get("writer").toString().equals(m_nickname)
                                                        if(document.getData().get("writer").toString().equals(documentSnapshot.getData().get("nickname").toString())){
                                                            dataList.add(new WriteFeedInfo(
                                                                    document.getData().get("writer").toString(),
                                                                    document.getData().get("date").toString(),
                                                                    document.getData().get("title").toString(),
                                                                    document.getData().get("img_profile").toString(),
                                                                    document.getData().get("content").toString(),
                                                                    document.getData().get("likeCnt").toString(),
                                                                    document.getData().get("commentCount").toString()
                                                            ));
                                                        }
                                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                                    }
                                                    RecyclerView recyclerView = getActivity().findViewById(R.id.main_recyclerview);
                                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                                    recyclerView.setLayoutManager(layoutManager);

                                                    MainPostAdapter mainPostAdapter = new MainPostAdapter(dataList);
                                                    recyclerView.setAdapter(mainPostAdapter);
                                                    recyclerView.getAdapter().notifyDataSetChanged();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}