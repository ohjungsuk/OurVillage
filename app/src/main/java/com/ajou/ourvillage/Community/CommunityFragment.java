package com.ajou.ourvillage.Community;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ajou.ourvillage.R;
import com.ajou.ourvillage.RecyclerViewDecoration;

import java.util.ArrayList;

public class CommunityFragment extends Fragment {

    private TextView btn_write;

    public CommunityFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        btn_write = view.findViewById(R.id.community_btn_write);
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CommunityWriteActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        RecyclerView recyclerView = getActivity().findViewById(R.id.community_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<CommunityPostItem> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add(new CommunityPostItem(R.drawable.ic_launcher_background, "이름임", "2021년 5월 3일 월요일", "맛집 찾음", "요 앞에 새로 생긴 곳 마싯더라구영", "5", "3", false));
        }

        CommunityPostAdapter communityPostAdapter = new CommunityPostAdapter(dataList);
        recyclerView.setAdapter(communityPostAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

}