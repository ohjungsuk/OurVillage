package com.ajou.ourvillage.Apart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ajou.ourvillage.R;

import java.util.ArrayList;

public class ApartNoticeActivity extends AppCompatActivity {

    private Button btn_close;
    private TextView btn_search, btn_write;
    private EditText et_search;
    private String search_keyword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apart_notice);

        btn_close = findViewById(R.id.apart_notice_btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_search = findViewById(R.id.apart_notice_btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_keyword = et_search.getText().toString();
            }
        });

        btn_write = findViewById(R.id.apart_notice_btn_write);
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ApartCommunityWritePostActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        RecyclerView recyclerView = findViewById(R.id.apart_notice_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<ApartPostItem> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add(new ApartPostItem(R.drawable.ic_launcher_background, "이름임", "2021년 5월 3일 월요일", "공지드립니다.", "공사합니당", "5", "3", false));
        }

        ApartPostAdapter apartPostAdapter = new ApartPostAdapter(dataList);
        recyclerView.setAdapter(apartPostAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}