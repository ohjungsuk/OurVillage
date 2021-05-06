package com.ajou.ourvillage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ajou.ourvillage.Login.LoginActivity;
import com.ajou.ourvillage.Login.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp_profile extends AppCompatActivity {

    EditText signUP_edt_name,signUP_edt_Nickname,signUP_edt_phoneNum,
            signUP_edt_Address,signUP_edt_Apart;
    Button signUP_profile_btn_done;

    boolean isProfileAllRegister = false;
    private FirebaseUser firebaseUser;
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
        String name = signUP_edt_name.getText().toString();
        String Nickname = signUP_edt_Nickname.getText().toString();
        if(Nickname.length() >0){
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                    .setDisplayName(Nickname)
                    .build();
            if(firebaseUser != null){
                firebaseUser.updateProfile(profileUpdate)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    startToast("회원정보 등록에 성공하였습니다.");
                                    startActivity(new Intent(SignUp_profile.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
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
//                        Intent intent = new Intent(SignUp_profile.this, SignUpActivity.class);
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
}