package com.ajou.ourvillage.Community;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

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

public class CommunityWriteActivity extends AppCompatActivity implements CommunityImageInterface {

    private static final String TAG = "CommunityWrite";

    private ImageButton btn_backToMain;
    private ImageView img_upload;
    private Button btn_community_write;
    private EditText et_community_content, et_community_title;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private boolean activity_stack_check = true;

    private Uri mImgUri;
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
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void btnMover(){
        btn_backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(CommunityWriteActivity.this)
                        .setMessage("??????????????? ????????? ???????????? ????????????.")
                        .setPositiveButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {
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

            }
        });
    }


    private void post(){
        final String title = et_community_title.getText().toString();
        final String content = et_community_content.getText().toString();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(title.length() > 0 && content.length() > 0){
            CommunityPostItem communityPostItem = new CommunityPostItem(R.drawable.ic_launcher_background, firebaseUser.getEmail(), "2021.05.23.Sun 08:24", title, content, "5", "3", false);
            uploadToDB(communityPostItem);
        }
    }
    private void uploadToDB(CommunityPostItem communityPostItem){
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //????????? ??????
        db.collection("Community")
                .add(communityPostItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),
                                "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                        finish();
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getApplicationContext(),
                                "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
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
                    // ????????? ??????????????? ????????? ??????
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // ??????????????? ??????
                    img_upload.setImageBitmap(img);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                uploadImage(image);
            }
        }
    }

    // Uri??? FireBase??? ????????????
    private void uploadImage(Uri imgUri) {
        final CommunityImageService communityImageService = new CommunityImageService(this);
        communityImageService.uploadFileToFireBase(imgUri);
    }

    // ?????? ????????????
    @Override
    public void uploadFireBaseSuccess(Uri uri) {
        mImgUri = uri;
        //Toast.makeText(getApplicationContext(), "firebase uri : " + mImgUri, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void uploadFireBaseFailure() {
        Toast.makeText(getApplicationContext(), "firebase upload fail ", Toast.LENGTH_SHORT).show();
    }
}