package com.ajou.ourvillage.Apart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.ourvillage.R;

public class ApartNoticeWritePostActivity extends AppCompatActivity {

    private Button btn_close;
    private TextView btn_write;
    private EditText et_content;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apart_notice_write_post);

        btn_close = findViewById(R.id.apart_notice_write_post_btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "글쓰기가 취소되었습니다.", Toast.LENGTH_SHORT);
                finish();
            }
        });

        et_content = findViewById(R.id.apart_notice_write_post_tv_content);

        btn_write = findViewById(R.id.apart_notice_write_post_btn_write);
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = et_content.getText().toString();
                System.out.println("입력 내용 : " + content);
                finish();
            }
        });
    }
}