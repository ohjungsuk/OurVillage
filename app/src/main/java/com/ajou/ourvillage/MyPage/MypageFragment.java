package com.ajou.ourvillage.MyPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.ourvillage.Login.LoginActivity;
import com.ajou.ourvillage.MainActivity;
import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;


public class MypageFragment extends Fragment {

    private TextView mypage_email, mypage_Nickname;
    private Button mypage_add_post, mypage_modify_pw, mypage_cancel1, mypage_cancel2,mypage_NameModify,
            mypage_btn_MyGoodPost, mypage_MyCommentPost, mypage_MyPost, mypage_EmailAuth;
    private ImageButton mypage_imgbtn_profile;
    private LinearLayout mypage_linear_modify;
    private EditText mypage_edt_name, mypage_edt_PwChange_byEmail;

    private void init(View v){
        mypage_Nickname = (TextView)v.findViewById(R.id.mypage_Nickname);
        mypage_email = (TextView)v.findViewById(R.id.mypage_email);
        mypage_NameModify =(Button)v.findViewById(R.id.mypage_NameModify);
        mypage_EmailAuth = (Button)v.findViewById(R.id.mypage_EmailAuth);
        mypage_linear_modify = (LinearLayout) v.findViewById(R.id.mypage_linear_modify);
        mypage_modify_pw = (Button) v.findViewById(R.id.mypage_modify_pw);
        mypage_cancel1 = (Button) v.findViewById(R.id.mypage_cancel1);
        mypage_cancel2 = (Button) v.findViewById(R.id.mypage_cancel2);
        mypage_edt_name = (EditText) v.findViewById(R.id.mypage_edt_name);
        mypage_edt_PwChange_byEmail= (EditText) v.findViewById(R.id.mypage_edt_PwChange_byEmail);
    }

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    String emailEdit;

    public MypageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        init(view);

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            for(UserInfo profile : user.getProviderData()){
                String email = profile.getEmail();
                String name = profile.getDisplayName();
                mypage_Nickname.setText(name);
                mypage_email.setText(email);
            }
        }

        mypage_NameModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = mypage_edt_name.getText().toString();
                if(nickname.length() > 0){
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nickname)
                            .build();
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        if(user!=null){
                                            for(UserInfo profile : user.getProviderData()){
                                                String name = profile.getDisplayName();
                                                mypage_Nickname.setText(name);
                                                Toast.makeText(getContext(), "수정완료", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            });
                }else {
                    Toast.makeText(getContext(), "닉네임을 입력해야 수정가능해요!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mypage_EmailAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();   //비밀번호 변경을 위해 입력한 이메일로 메일 전송
            }
        });


        mypage_modify_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mypage_linear_modify.getVisibility() == v.GONE) {
                    mypage_linear_modify.setVisibility(v.VISIBLE);
                }

            }
        });

        mypage_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mypage_linear_modify.getVisibility() == v.VISIBLE) {
                    mypage_linear_modify.setVisibility(v.GONE);
                }
            }
        });

        mypage_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mypage_linear_modify.getVisibility() == v.VISIBLE) {
                    mypage_linear_modify.setVisibility(v.GONE);
                }
            }
        });

        return view;
    }

    private void send() {
        String emailEdit = mypage_edt_PwChange_byEmail.getText().toString();
        Toast.makeText(getContext(),emailEdit, Toast.LENGTH_SHORT).show();
//        String v = "dtps@ajou.ac.kr";
        firebaseAuth.sendPasswordResetEmail(emailEdit)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "이메일을 보냈습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//        if (emailEdit.length() > 0) {
//            firebaseAuth.sendPasswordResetEmail(emailEdit)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Toast.makeText(getContext(), "이메일을 보냈습니다.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        } else {
//            Toast.makeText(getContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
//        }
//
    }

}