package com.ajou.ourvillage.Tasty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TastyWriteActivity extends AppCompatActivity implements TastyImageInterface{

    private static final String TAG = "TastyWrite";
    static String tasty_location = "";
    private TextView tv_set_location;
    static String address_longtitude, address_latitude;

    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private ImageButton btn_backToMain;
    private ImageView img_upload;
    private Button btn_tasty_write, btn_tasty_add_location;
    private TextView tv_tasty_score;
    private EditText et_tasty_title, et_tasty_review, et_tasty_recommend;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private boolean activity_stack_check = true;

    private Uri mImgUri;
    final int GET_GALLERY_IMAGE = 200;
    final int REQUEST_IMAGE_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasty_write);

        tasty_location = "";
        init();
        btnMover();
    }

    public void init(){
        et_tasty_title = (EditText)findViewById(R.id.tasty_write_et_title);
        et_tasty_recommend = (EditText) findViewById(R.id.tasty_write_et_recommend);
        et_tasty_review = (EditText) findViewById(R.id.tasty_write_et_review);
        tv_tasty_score = (TextView) findViewById(R.id.tasty_tv_score);
        btn_tasty_write = (Button)findViewById(R.id.tasty_write_btn_complete);
        btn_backToMain = (ImageButton) findViewById(R.id.tasty_write_btn_close);
        img_upload = (ImageView) findViewById(R.id.tasty_write_btn_img);
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        btn_tasty_add_location = (Button) findViewById(R.id.tasty_btn_add_map);
        tv_set_location = (TextView) findViewById(R.id.tasty_tv_location);
    }

    public void btnMover(){
        btn_backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(TastyWriteActivity.this)
                        .setMessage("뒤로 가시면 내용이 저장되지 않습니다.")
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
        btn_tasty_write.setOnClickListener(new View.OnClickListener() {
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

        btn_tasty_add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TastyChooseMapActivity.class);
                startActivity(intent);
            }
        });

        tv_tasty_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = new String[] {"★★★★★", "★★★★", "★★★", "★★", "★"};
                final int[] selectedIndex = {0};

                AlertDialog.Builder dialog = new AlertDialog.Builder(TastyWriteActivity.this);
                dialog.setTitle("평점을 선택해주세요.")
                        .setSingleChoiceItems(items,0
                                ,new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        selectedIndex[0] = i;
                                    }
                                })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                tv_tasty_score.setText(items[selectedIndex[0]]);

                                System.out.println(items[selectedIndex[0]]);
                            }
                        }).create().show();
            }
        });
    }

    private void post(){
        final String title = et_tasty_title.getText().toString();
        final String review = et_tasty_review.getText().toString();
        final String location = tv_set_location.getText().toString();
        final String score = tv_tasty_score.getText().toString();
        final String recommend = et_tasty_recommend.getText().toString();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(title.length() > 0 && review.length() > 0){
            ArrayList<String> contentsList = new ArrayList<>();
            if(review.length() > 0){
                contentsList.add(review);
            }
            String name = null;
            for (UserInfo profile : firebaseUser.getProviderData()) {
                name = profile.getDisplayName();
            }
            System.out.println("위치" + location);
            TastyPostItem tastyPostItem = new TastyPostItem(name, getTime(), location, score, review, recommend, mImgUri.toString(), address_latitude, address_longtitude);
            uploadToDB(tastyPostItem);
        }
    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    private void uploadToDB(TastyPostItem tastyPostItem){
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //반복문 유의
        db.collection("Tasty")
                .add(tastyPostItem)
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
        final TastyImageService tastyImageService = new TastyImageService(this);
        tastyImageService.uploadFileToFireBase(imgUri);
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

    @Override
    protected void onResume() {
        super.onResume();

        tv_set_location.setText(tasty_location);
    }
}