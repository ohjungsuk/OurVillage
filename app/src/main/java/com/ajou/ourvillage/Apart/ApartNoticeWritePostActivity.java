package com.ajou.ourvillage.Apart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ApartNoticeWritePostActivity extends AppCompatActivity {

    private Button btn_close;
    private TextView btn_write;
    private EditText et_title, et_content;
    private String title = null, content = null;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private static final String TAG = "ApartNoticeWritePost";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apart_notice_write_post);

        init();

        long now = System.currentTimeMillis();

        Date mDate = new Date(now);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String time = simpleDateFormat.format(mDate);

        System.out.println("시간2 " + time);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ApartNoticeWritePostActivity.this)
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

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = et_title.getText().toString();
                content = et_content.getText().toString();

                if (title.equals("")) {
                    new AlertDialog.Builder(ApartNoticeWritePostActivity.this)
                            .setMessage("제목을 입력해주세요")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                } else if (content.equals("")) {
                    new AlertDialog.Builder(ApartNoticeWritePostActivity.this)
                            .setMessage("내용을 입력해주세요")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                } else if (et_title != null && et_content != null) {
                    new AlertDialog.Builder(ApartNoticeWritePostActivity.this)
                            .setMessage("글을 등록하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    System.out.println("입력 제목 : " + title);
                                    System.out.println("입력 내용 : " + content);
                                    System.out.println("시간3" + time);

                                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                    WriteArticleInfo writeArticleInfo = new WriteArticleInfo(title, content, time, firebaseUser.getEmail(), 0, 0);
                                    uploadToDB(writeArticleInfo);


                                    //post();
                                    //dialogInterface.dismiss();
                                    // finish();
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                }
            }
        });
    }

    public void init() {
        btn_close = findViewById(R.id.apart_notice_write_post_btn_close);
        et_title = findViewById(R.id.apart_notice_write_post_tv_title);
        et_content = findViewById(R.id.apart_notice_write_post_tv_content);
        btn_write = findViewById(R.id.apart_notice_write_post_btn_write);
    }

    private void uploadToDB(WriteArticleInfo writeArticleInfo){
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("ApartNotice")
                .add(writeArticleInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),
                                "게시물 등록 완료", Toast.LENGTH_SHORT).show();
                        finish();
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getApplicationContext(),
                                "게시물 등록 실패", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}