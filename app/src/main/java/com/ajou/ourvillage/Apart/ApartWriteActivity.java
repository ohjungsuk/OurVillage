package com.ajou.ourvillage.Apart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ajou.ourvillage.Main.AddMyFeed;
import com.ajou.ourvillage.Main.GalleryAcitivity;
import com.ajou.ourvillage.Main.WriteFeedInfo;
import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class ApartWriteActivity extends AppCompatActivity implements ApartImageInterface {

    private static final String TAG = "ApartWrite";

    private ImageButton btn_backToMain;
    private ImageView img_upload;
    private Button btn_apart_write;
    private EditText et_apart_content, et_apart_title;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private boolean activity_stack_check = true;

    private Uri mImgUri;
    final int GET_GALLERY_IMAGE = 200;
    final int REQUEST_IMAGE_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apart_write);

        init();
        btnMover();
    }

    public void init(){
        et_apart_content = (EditText)findViewById(R.id.apart_write_et_content);
        et_apart_title = (EditText)findViewById(R.id.apart_write_et_title);
        btn_apart_write = (Button)findViewById(R.id.apart_write_btn_complete);
        btn_backToMain = (ImageButton) findViewById(R.id.apart_write_btn_close);
        img_upload = (ImageView) findViewById(R.id.apart_write_btn_img);
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void btnMover(){
        btn_backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ApartWriteActivity.this)
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
        btn_apart_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });
        img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_CODE);
            }
        });
    }

    private void post(){
        final String title = et_apart_title.getText().toString();
        final String comment = et_apart_content.getText().toString();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CODE) {
            Uri image = data.getData();
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지뷰에 세팅
                    img_upload.setImageBitmap(img);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                uploadImage(image);
            }
        }
    }

    // Uri를 FireBase에 전송하고
    private void uploadImage(Uri imgUri) {
        final ApartImageService apartImageService = new ApartImageService(this);
        apartImageService.uploadFileToFireBase(imgUri);
    }

    // 경로 받아오기
    @Override
    public void uploadFireBaseSuccess(Uri uri) {
        mImgUri = uri;
        Toast.makeText(getApplicationContext(), "firebase uri : " + mImgUri, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void uploadFireBaseFailure() {
        Toast.makeText(getApplicationContext(), "firebase upload fail ", Toast.LENGTH_SHORT).show();
    }
}