package com.ajou.ourvillage.Apart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ajou.ourvillage.R;

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
}