package com.ajou.ourvillage.Tasty;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ajou.ourvillage.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


public class TastyFragment extends Fragment {

    private TextView btn_write;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseUser firebaseUser;
    private Button btn_test;

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
                Intent intent = new Intent(getActivity(), TastyWriteActivity.class);
                startActivity(intent);
            }
        });

        btn_test = view.findViewById(R.id.tast_btn_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TastyShowMapActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<TastyPostItem> dataList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        RecyclerView recyclerView = getActivity().findViewById(R.id.tasty_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        for (int i = 0; i < 10; i++) {
            dataList.add(new TastyPostItem("writer", "20210530", "seoul", "3", "맛잇당", "양념치킨", "ㅁㄴㅇ", "위도", "경도"));
        }

        TastyPostAdapter tastyPostAdapter = new TastyPostAdapter(dataList);
        recyclerView.setAdapter(tastyPostAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();

    }
}