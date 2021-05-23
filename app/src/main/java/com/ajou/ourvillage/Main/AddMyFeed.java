package com.ajou.ourvillage.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ajou.ourvillage.Login.SignUp_profile;
import com.ajou.ourvillage.MainActivity;
import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddMyFeed extends AppCompatActivity {
    private static final String TAG = "AddMyFeed";

    private ImageButton btn_backToMain,imgbtn_upload;
    private Button btn_feed_write;
    private EditText edt_feed_comment,edt_feed_title;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private boolean activity_stack_check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_feed);
        init();
        btnMover();
    }

    public void init(){
        edt_feed_comment = (EditText)findViewById(R.id.edt_feed_comment);
        edt_feed_title = (EditText)findViewById(R.id.edt_feed_title);
        btn_feed_write = (Button)findViewById(R.id.btn_feed_write);
        btn_backToMain = (ImageButton)findViewById(R.id.btn_backToMain);
        imgbtn_upload = (ImageButton)findViewById(R.id.imgbtn_upload);
    }

    public void btnMover(){
        btn_backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AddMyFeed.this)
                        .setMessage("뒤로가시면 내용이 저장되지 않습니다.")
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
        btn_feed_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });
        imgbtn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //권한이 없을떄
                if(ContextCompat.checkSelfPermission(AddMyFeed.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddMyFeed.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                        ActivityCompat.requestPermissions(AddMyFeed.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }else{ //권한 다시 물어보는 경우
                        ActivityCompat.requestPermissions(AddMyFeed.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        Toast.makeText(getApplicationContext(),
                                "권한을 허용해주세요", Toast.LENGTH_SHORT).show();
                    }
                }else{//권한이 있을때
                    Intent intent = new Intent(AddMyFeed.this,GalleryAcitivity.class);
                    startActivity(intent);
                    //finish();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(AddMyFeed.this, GalleryAcitivity.class);
                    startActivity(intent);
                    //finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "권한을 허용해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void post(){
        final String title = edt_feed_title.getText().toString();
        final String comment = edt_feed_comment.getText().toString();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(title.length() > 0 && comment.length() > 0){
            WriteFeedInfo writeFeedInfo = new WriteFeedInfo(title,comment,firebaseUser.getEmail());
            uploadToDB(writeFeedInfo);
        }
    }
    private void uploadToDB(WriteFeedInfo writeFeedInfo){
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //반복문 유의
        db.collection("Feed")
                .add(writeFeedInfo)
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

//        for (UserInfo profile : firebaseUser.getProviderData()) {
//            String db_email = profile.getEmail();
//            db.collection(db_email).document(firebaseUser.getUid()).set(writeFeedInfo)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            if(activity_stack_check){
//                                Toast.makeText(getApplicationContext(),
//                                        "게시물 등록 완료", Toast.LENGTH_SHORT).show();
//                                finish();
//                                activity_stack_check = false;
//                            }
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getApplicationContext(),
//                                    "게시물 등록 실패", Toast.LENGTH_SHORT).show();
//                            Log.w(TAG, "Error writing document", e);
//                        }
//                    });
//        }
    }

}