package com.ajou.ourvillage.Apart;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ajou.ourvillage.R;

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

        btn_notice = view.findViewById(R.id.apart_btn_more_notice);
        btn_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ApartNoticeActivity.class);
                startActivity(intent);
            }
        });

        btn_help = view.findViewById(R.id.apart_btn_more_help);
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ApartHelpActivity.class);
                startActivity(intent);
            }
        });

        btn_community = view.findViewById(R.id.apart_btn_more_community);
        btn_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ApartCommunityActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}