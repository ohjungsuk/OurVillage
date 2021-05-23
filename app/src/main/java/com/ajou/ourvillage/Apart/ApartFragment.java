package com.ajou.ourvillage.Apart;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ajou.ourvillage.R;

import java.util.ArrayList;

public class ApartFragment extends Fragment {

    private Button btn_notice, btn_help, btn_community;

    public ApartFragment() {
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
        View view = inflater.inflate(R.layout.fragment_apart, container, false);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        RecyclerView recyclerView = getActivity().findViewById(R.id.apart_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewDecoration spaceDecoration = new RecyclerViewDecoration(30);
        recyclerView.addItemDecoration(spaceDecoration);

        ArrayList<ApartPostItem> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add(new ApartPostItem(R.drawable.ic_launcher_background, "이름임", "2021년 5월 3일 월요일", "맛집 찾음", "요 앞에 새로 생긴 곳 마싯더라구영", "5", "3", false));
        }

        ApartPostAdapter apartPostAdapter = new ApartPostAdapter(dataList);
        recyclerView.setAdapter(apartPostAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();
    }


}