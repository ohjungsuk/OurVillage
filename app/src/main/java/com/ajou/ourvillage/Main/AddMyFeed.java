package com.ajou.ourvillage.Main;

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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ajou.ourvillage.Apart.ApartImageService;
import com.ajou.ourvillage.Apart.ApartPostItem;
import com.ajou.ourvillage.Apart.ImageInterface;
import com.ajou.ourvillage.Login.SignUp_profile;
import com.ajou.ourvillage.MainActivity;
import com.ajou.ourvillage.R;
import com.bumptech.glide.Glide;
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

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddMyFeed extends AppCompatActivity implements ImageInterface {
    private static final String TAG = "AddMyFeed";

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private LinearLayout image_layout;
    private ImageView imageView;
    private ImageButton btn_backToMain,imgbtn_upload;
    private Button btn_feed_write;
    private EditText edt_feed_comment,edt_feed_title;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private boolean activity_stack_check = true;
    private ArrayList<String> pathList = new ArrayList<>();

    private Uri mImgUri;
    final int GET_GALLERY_IMAGE = 200;
    final int REQUEST_IMAGE_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_feed);
        init();
        btnMover();
    }

    public void init(){
        image_layout = (LinearLayout)findViewById(R.id.image_layout);
        imageView = (ImageView)findViewById(R.id.imageView);
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
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_CODE);
            }
        });
    }


    private void post(){
        final String title = edt_feed_title.getText().toString();
        final String content = edt_feed_comment.getText().toString();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(title.length() > 0 && content.length() > 0) {
            ArrayList<String> contentsList = new ArrayList<>();
            if(content.length() > 0){
                contentsList.add(content);
            }
            String name = null;
            String id = null;
            for (UserInfo profile : firebaseUser.getProviderData()) {
                name = profile.getDisplayName();
                id = profile.getUid();
            }                                                                   //mImgUri.toString(),
            WriteFeedInfo writeFeedInfo = new WriteFeedInfo(name, getTime(), title, mImgUri.toString(), content, "0", "0");
            uploadToDB(writeFeedInfo);
        }
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
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
//                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
//                    pathList.add(img);
//                    // 이미지뷰에 세팅
//                    imageView.setImageBitmap(img);
//                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                    ImageView mimageView = new ImageView(AddMyFeed.this);
//                    mimageView.setLayoutParams(layoutParams);
                    Glide.with(this).load(img).override(1000).into(imageView);
//                    image_layout.addView(imageView);
//
//                    EditText editText = new EditText(AddMyFeed.this);
//                    editText.setLayoutParams(layoutParams);
//                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
//                    image_layout.addView(editText);

                    if (imageView.getVisibility()==View.GONE){
                        imageView.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                uploadImage(image);
            }
        }
    }
    // Uri를 FireBase에 전송하고
    private void uploadImage(Uri imgUri) {
        final FeedImageService feedImageService = new FeedImageService(this);
        feedImageService.uploadFileToFireBase(imgUri);
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