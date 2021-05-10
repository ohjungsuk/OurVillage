package com.ajou.ourvillage.Main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajou.ourvillage.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainFragment extends Fragment {

    private FloatingActionButton floating_add;

    public MainFragment() {
        // Required empty public constructor
    }

    private void init(View v){
        floating_add = (FloatingActionButton)v.findViewById(R.id.floating_add);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        init(view);
        floatingbtn();

        return view;
    }

    private void floatingbtn(){
        floating_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddMyFeed.class);
                startActivity(intent);
                //finish();
            }
        });
    }
}