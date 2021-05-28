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

import com.ajou.ourvillage.Apart.ApartPostAdapter;
import com.ajou.ourvillage.Apart.ApartPostItem;
import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class MainFragment extends Fragment {

    private TextView btn_write;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        btn_write = view.findViewById(R.id.main_btn_write);
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddMyFeed.class);
                startActivity(intent);
            }
        });

        ArrayList<WriteFeedInfo> dataList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        db.collection("Feed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                            //    WriteFeedInfo writeFeedInfo = document.getData().get(WriteFeedInfo.class);
                                dataList.add(new WriteFeedInfo(
                                        document.getData().get("writer").toString(),
                                        document.getData().get("date").toString(),
                                        document.getData().get("title").toString(),
                                        document.getData().get("img_profile").toString(),
                                        document.getData().get("content").toString(),
                                        document.getData().get("likeCnt").toString(),
                                        document.getData().get("commentCount").toString()
                                ));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            RecyclerView recyclerView = getActivity().findViewById(R.id.main_recyclerview);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(layoutManager);

                            MainPostAdapter mainPostAdapter = new MainPostAdapter(dataList);
                            recyclerView.setAdapter(mainPostAdapter);
                            recyclerView.getAdapter().notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

//        RecyclerView recyclerView = getActivity().findViewById(R.id.main_recyclerview);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//
//        MainPostAdapter mainPostAdapter = new MainPostAdapter(dataList);
//        recyclerView.setAdapter(mainPostAdapter);
//        recyclerView.getAdapter().notifyDataSetChanged();


 //       dataList.add(new WriteFeedInfo("이름임", "2021년 5월 3일 월요일", "맛집 찾음", "gs://ourvillage-d0fd0.appspot.com/apart/2355","요 앞에 새로 생긴 곳 마싯더라구영", "5", "3"));

    }
}