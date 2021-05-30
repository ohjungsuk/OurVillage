package com.ajou.ourvillage.Tasty;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.ourvillage.Main.MainPostAdapter;
import com.ajou.ourvillage.Main.WriteFeedInfo;
import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


public class TastyFragment extends Fragment {

    private TextView btn_write;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseUser firebaseUser;
    private Button btn_test;
    private String mf_nickname = null;
    private boolean is_upload = false;

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

        is_upload = false;
        db.collection("Tasty")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc: task.getResult()) {
                            TastyPostItem tastyPostItem = new TastyPostItem(
                                    doc.getString("writer"),
                                    doc.getString("date"),
                                    doc.getString("address"),
                                    doc.getString("rate"),
                                    doc.getString("review"),
                                    doc.getString("recommend"),
                                    doc.getString("foodImage"),
                                    doc.getString("latitude"),
                                    doc.getString("longitude"));
                            dataList.add(tastyPostItem);
                        }
                        TastyPostAdapter tastyPostAdapter = new TastyPostAdapter(dataList);
                        recyclerView.setAdapter(tastyPostAdapter);
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