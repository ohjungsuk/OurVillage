package com.ajou.ourvillage.Apart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.ourvillage.Login.LoginActivity;
import com.ajou.ourvillage.MainActivity;
import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ApartCommunityWritePostActivity extends AppCompatActivity {

    private Button btn_close;
    private TextView btn_write;
    private EditText et_title, et_content;
    private String title = null, content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apart_community_write_post);

        btn_close = findViewById(R.id.apart_community_write_post_btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ApartCommunityWritePostActivity.this)
                        .setMessage("글쓰기를 종료하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });

        et_title = findViewById(R.id.apart_community_write_post_tv_title);
        et_content = findViewById(R.id.apart_community_write_post_tv_content);

        btn_write = findViewById(R.id.apart_community_write_post_btn_write);
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = et_title.getText().toString();
                content = et_content.getText().toString();
                System.out.println("입력 제목 : " + title);
                System.out.println("입력 내용 : " + content);
                finish();
            }
        });
    }
}