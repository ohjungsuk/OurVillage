package com.ajou.ourvillage.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.ajou.ourvillage.MainActivity;
import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    Toolbar signup_toolbar;
    EditText signUP_edt_name,signUP_edt_email,signUP_edt_pw,sinUp_edt_pwCheck;
    Button signUP_btn_done;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUp();

        //파이어베이스 접근 설정
        // user = firebaseAuth.getCurrentUser();
        firebaseAuth =  FirebaseAuth.getInstance();
        // firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        signUP_btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!signUP_edt_email.getText().toString().equals("") && !signUP_edt_pw.getText().toString().equals("")) {
                    // 이메일과 비밀번호가 공백이 아닌 경우
                    createUser(signUP_edt_email.getText().toString(), signUP_edt_pw.getText().toString(), signUP_edt_name.getText().toString());
                } else {
                    // 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(SignUpActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createUser(String email, String password, String name){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        }else {
                            // 계정이 중복된 경우
                            Toast.makeText(SignUpActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(SignUpActivity.this)
                .setMessage("If you go back, the content is not saved")
                .setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    public void setUp(){
        //signup_toolbar = (Toolbar)findViewById(R.id.signup_toolbar);
        signUP_edt_name = (EditText)findViewById(R.id.signUP_edt_name);
        signUP_edt_email = (EditText)findViewById(R.id.signUP_edt_email);
        signUP_edt_pw = (EditText)findViewById(R.id.signUP_edt_pw);
        sinUp_edt_pwCheck = (EditText)findViewById(R.id.sinUp_edt_pwCheck);
        signUP_btn_done = (Button)findViewById(R.id.signUP_btn_done);
    }
}