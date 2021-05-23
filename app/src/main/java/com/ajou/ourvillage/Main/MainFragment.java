package com.ajou.ourvillage.Main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ajou.ourvillage.Apart.ApartPostAdapter;
import com.ajou.ourvillage.Apart.ApartPostItem;
import com.ajou.ourvillage.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private TextView btn_write;

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        RecyclerView recyclerView = getActivity().findViewById(R.id.main_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<WriteFeedInfo> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add(new WriteFeedInfo("제목", "댓글", "이름"));
        }

        MainPostAdapter mainPostAdapter = new MainPostAdapter(dataList);
        recyclerView.setAdapter(mainPostAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}