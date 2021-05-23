package com.ajou.ourvillage.Community;

import android.app.ProgressDialog;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.ajou.ourvillage.Apart.ApartImageInterface;
import com.ajou.ourvillage.Apart.ApartWriteActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CommunityImageService {
    private final CommunityImageInterface mCommunityImageInterface;

    public CommunityImageService(CommunityImageInterface mCommunityImageInterface) {
        this.mCommunityImageInterface = mCommunityImageInterface;
    }

    public void uploadFileToFireBase(Uri imgUri) {
        final ProgressDialog progressDialog = new ProgressDialog((CommunityWriteActivity) mCommunityImageInterface);
        progressDialog.setTitle("Uploading..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference storageRef = storage.getReferenceFromUrl("gs://ourvillage-d0fd0.appspot.com/")
                .child("community/" + imgUri.getLastPathSegment());

        UploadTask uploadTask = storageRef.putFile(imgUri);
        storageRef.putFile(imgUri)
                //성공시
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                    }
                })
                //실패시
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        mCommunityImageInterface.uploadFireBaseFailure();
                    }
                })
                //진행중
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests")
                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                    }
                });
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();  // downloadUri -> 이게 업로드 완료된  url임
                    progressDialog.dismiss();
                    mCommunityImageInterface.uploadFireBaseSuccess(downloadUri);
                } else {
                    progressDialog.dismiss();
                    mCommunityImageInterface.uploadFireBaseFailure();
                }
            }
        });
    }
}
