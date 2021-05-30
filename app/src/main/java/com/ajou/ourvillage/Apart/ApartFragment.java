package com.ajou.ourvillage.Apart;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.ourvillage.R;
import com.ajou.ourvillage.Tasty.TastyPostAdapter;
import com.ajou.ourvillage.Tasty.TastyPostItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ApartFragment extends Fragment {

    private TextView btn_write;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseUser firebaseUser;

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

        btn_write = view.findViewById(R.id.apart_btn_write);
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WriteActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<ApartPostItem> dataList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        RecyclerView recyclerView = getActivity().findViewById(R.id.apart_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        db.collection("Apart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc: task.getResult()) {
                            ApartPostItem apartPostItem = new ApartPostItem(
                                    doc.getString("img_profile"),
                                    doc.getString("writer"),
                                    doc.getString("date"),
                                    doc.getString("title"),
                                    doc.getString("content"),
                                    doc.getString("likeCnt"),
                                    doc.getString("commentCount"),
                                    doc.getBoolean("like"));
                            dataList.add(apartPostItem);
                        }
                        ApartPostAdapter apartPostAdapter = new ApartPostAdapter(dataList);
                        recyclerView.setAdapter(apartPostAdapter);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "NO", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}