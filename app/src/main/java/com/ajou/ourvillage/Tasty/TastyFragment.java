package com.ajou.ourvillage.Tasty;

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
import com.ajou.ourvillage.Apart.ApartWriteActivity;
import com.ajou.ourvillage.R;
import com.ajou.ourvillage.RecyclerViewDecoration;

import java.util.ArrayList;


public class TastyFragment extends Fragment {

    private TextView btn_write;

    public TastyFragment() {
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
        View view = inflater.inflate(R.layout.fragment_tasty, container, false);

        btn_write = view.findViewById(R.id.tasty_btn_write);
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ApartWriteActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        RecyclerView recyclerView = getActivity().findViewById(R.id.tasty_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewDecoration spaceDecoration = new RecyclerViewDecoration(30);
        recyclerView.addItemDecoration(spaceDecoration);

        ArrayList<TastyPostItem> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add(new TastyPostItem(R.drawable.ic_launcher_background, "이름임", "2021년 5월 3일 월요일", "맛집 찾음", "요 앞에 새로 생긴 곳 마싯더라구영", "5", "3", false));
        }

        TastyPostAdapter tastyPostAdapter = new TastyPostAdapter(dataList);
        recyclerView.setAdapter(tastyPostAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}