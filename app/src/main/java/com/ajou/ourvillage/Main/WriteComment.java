package com.ajou.ourvillage.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class WriteComment extends AppCompatActivity {
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String feed_id = null;
    private String date = null;
    private String writer = null;
    private TextView test,wc_tv_comment_write;
    private EditText wc_edt_comment;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        init();

        //test.setText(intent.getExtras().getString("feed_id"));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        wc_tv_comment_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });
    }
    public void postComment(){
        Intent intent = getIntent();
        final String commentt = wc_edt_comment.getText().toString();
        for (UserInfo profile : firebaseUser.getProviderData()) {
            writer = profile.getDisplayName();
        }
        feed_id = intent.getExtras().getString("feed_id");
        date = getTime().toString();
        WriteCommentInfo writeCommentInfo = new WriteCommentInfo(feed_id,writer,commentt,date);
        if (commentt!=null){
            uploadComment(writeCommentInfo);
        }else{
            Toast.makeText(getApplicationContext(),
                    "댓글을 입력하여 주세요", Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadComment(WriteCommentInfo writeCommentInfo){
        //반복문 유의
        firebaseFirestore.collection("Comment")
                .add(writeCommentInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),
                                "댓글 작성 성공", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(),
                                "댓글 작성 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    public void init(){
        test = (TextView)findViewById(R.id.test);
        wc_tv_comment_write = (TextView)findViewById(R.id.wc_tv_comment_write);
        wc_edt_comment = (EditText) findViewById(R.id.wc_edt_comment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<WriteCommentInfo> dataList = new ArrayList<>();

        Intent intent = getIntent();
        feed_id = intent.getExtras().getString("feed_id");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Comment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot commentfile : task.getResult()){
                                if(commentfile.getData().get("feed_id").toString().equals(feed_id)){
                                    dataList.add(new WriteCommentInfo(
                                        commentfile.getData().get("feed_id").toString(),
                                        commentfile.getData().get("writer").toString(),
                                        commentfile.getData().get("comment").toString(),
                                        commentfile.getData().get("date").toString(),
                                        commentfile.getId()
                                    ));
                                }
                            }
                            RecyclerView recyclerView = (WriteComment.this).findViewById(R.id.comment_recyclerview);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(WriteComment.this);
                            recyclerView.setLayoutManager(layoutManager);

                            CommentAdapter commentAdapter = new CommentAdapter(dataList);
                            recyclerView.setAdapter(commentAdapter);
                            recyclerView.getAdapter().notifyDataSetChanged();
                            commentAdapter.setOnCommentDeleteClicklistener(new OnCommentDeleteClickListener() {
                                @Override
                                public void onCommentDeleteClick(CommentAdapter.ViewHolder holder, View view, int position) {
                                    WriteCommentInfo pos = commentAdapter.getItem(position);
                                    for (UserInfo profile : firebaseUser.getProviderData()) {
                                        writer = profile.getDisplayName();
                                    }
                                    if (writer.equals(pos.getWriter())){
                                        firebaseFirestore.collection("Comment").document(pos.getComment_uid())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("rrtest", pos.getComment_uid());
                                                        Toast.makeText(view.getContext(), "댓글을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(view.getContext(), "댓글 삭제를 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }else {
                                        Toast.makeText(view.getContext(), "내 댓글이 아닙니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}