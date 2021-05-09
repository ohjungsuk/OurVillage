package com.ajou.ourvillage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ajou.ourvillage.Login.LoginActivity;
import com.ajou.ourvillage.Login.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUp_profile extends AppCompatActivity {

    private static final String TAG = "Memberinfo";

    EditText signUP_edt_name,signUP_edt_Nickname,signUP_edt_phoneNum,
            signUP_edt_Address,signUP_edt_Apart;
    Button signUP_profile_btn_done;

    boolean isProfileAllRegister = false;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_profile);

        setUp();
        signUP_profile_btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileUpdate();
            }
        });
    }

    private void profileUpdate(){
        String nickname = signUP_edt_Nickname.getText().toString();
        String name = signUP_edt_name.getText().toString();
        String phone_num = signUP_edt_phoneNum.getText().toString();
        String address = signUP_edt_Address.getText().toString();
        String apart = signUP_edt_Apart.getText().toString();


        if(name.length() > 0 && nickname.length() >0 && phone_num.length() == 11 && address.length() > 0 && apart.length() > 0) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            db = FirebaseFirestore.getInstance();

            if (firebaseUser != null) {
                MemberInfo memberinfo = new MemberInfo(nickname, name, phone_num, address, apart);
                for (UserInfo profile : firebaseUser.getProviderData()) {
                    String db_email = profile.getEmail();
                    db.collection(db_email).document(firebaseUser.getUid()).set(memberinfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(nickname)
                                            .build();
                                    firebaseUser.updateProfile(profileUpdate)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(),
                                                                "회원정보 등록 성공", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(SignUp_profile.this,LoginActivity.class));
                                                        finish();
                                                    }
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),
                                            "회원정보 등록 실패", Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "모든 정보를 올바르게 입력하세요", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(SignUp_profile.this)
                .setMessage("뒤로 가시면 내용이 저장되지 않습니다.")
                .setPositiveButton("뒤로가기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
//                        Intent intent = new Intent(SignUp_profile.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    public void setUp(){
        signUP_edt_name = (EditText)findViewById(R.id.signUP_edt_name);
        signUP_edt_Nickname = (EditText)findViewById(R.id.signUP_edt_Nickname);
        signUP_edt_phoneNum = (EditText)findViewById(R.id.signUP_edt_phoneNum);
        signUP_edt_Address = (EditText)findViewById(R.id.signUP_edt_Address);
        signUP_edt_Apart = (EditText)findViewById(R.id.signUP_edt_Apart);
        signUP_profile_btn_done = (Button) findViewById(R.id.signUP_profile_btn_done);
    }

    private void mStartActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}