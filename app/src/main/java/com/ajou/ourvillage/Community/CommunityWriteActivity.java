package com.ajou.ourvillage.Community;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.ajou.ourvillage.Apart.ApartWriteActivity;
import com.ajou.ourvillage.Main.GalleryAcitivity;
import com.ajou.ourvillage.Main.WriteFeedInfo;
import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;

public class CommunityWriteActivity extends AppCompatActivity {

    private static final String TAG = "CommunityWrite";

    private ImageButton btn_backToMain;
    private ImageView img_upload;
    private Button btn_community_write;
    private EditText et_community_content, et_community_title;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private boolean activity_stack_check = true;

    final int GET_GALLERY_IMAGE = 200;
    final int REQUEST_IMAGE_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_write);

        init();
        btnMover();
    }

    public void init(){
        et_community_content = (EditText)findViewById(R.id.community_write_et_content);
        et_community_title = (EditText)findViewById(R.id.community_write_et_title);
        btn_community_write = (Button)findViewById(R.id.community_write_btn_complete);
        btn_backToMain = (ImageButton) findViewById(R.id.community_write_btn_close);
        img_upload = (ImageView) findViewById(R.id.community_write_btn_img);
    }

    public void btnMover(){
        btn_backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(CommunityWriteActivity.this)
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
        btn_community_write.setOnClickListener(new View.OnClickListener() {
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


//                //권한이 없을떄
//                if(ContextCompat.checkSelfPermission(ApartWriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//                    if(ActivityCompat.shouldShowRequestPermissionRationale(ApartWriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
//                        ActivityCompat.requestPermissions(ApartWriteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                    }else{ //권한 다시 물어보는 경우
//                        ActivityCompat.requestPermissions(ApartWriteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                        Toast.makeText(getApplicationContext(),
//                                "권한을 허용해주세요", Toast.LENGTH_SHORT).show();
//                    }
//                } else{//권한이 있을때
//                    Intent intent = new Intent(ApartWriteActivity.this, GalleryAcitivity.class);
//                    startActivity(intent);
//                    //finish();
//                }
            }
        });
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 1: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Intent intent = new Intent(CommunityWriteActivity.this, GalleryAcitivity.class);
//                    startActivity(intent);
//                    //finish();
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "권한을 허용해주세요", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

    private void post(){
        final String title = et_community_title.getText().toString();
        final String comment = et_community_content.getText().toString();
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

//                uploadImage(image);
            }
        }
    }

//    //찍은 사진의 Uri를 FireBase에 전송하고
//    private void uploadImage(Uri imgUri) {
//        final BandCreateService bandCreateService = new BandCreateService(this);
//        bandCreateService.uploadFileToFireBase(imgUri);
//    }
//    //그 경로를 받아옵니다.
//    @Override
//    public void upLoadFireBaseSuccess(Uri uri) {
//        mImgUri = uri;
//        showCustomToast("firebase uri: " + mImgUri);
////        try {
//        //upLoadUri(String.valueOf(mImgUri));
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
//    }
}