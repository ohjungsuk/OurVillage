package com.ajou.ourvillage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ajou.ourvillage.Login.LoginActivity;
import com.ajou.ourvillage.Login.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = null;

    private FirebaseAuth firebaseAuth;

    CoordinatorLayout coordinatorLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private Button main_btn_logout,main_btn_signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUp();
        HashKey();
        out();

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> {

        });

        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();

        main_btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        main_btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("정말 계정을 삭제할까요?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

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

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_main));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_apartment));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_post));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_tasty));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_mypage));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        // TabPagerAdapter 생성
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabPagerAdapter);

        // PageChangeListener 생성
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // 최초로 앱 실행 시 홈화면이 뜨기 때문에, 홈 아이콘은 초기 아이콘 색상을 검은색으로 설정
        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_IN);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 뷰페이저에 탭의 포지션에 해당하는 레이아웃 설정
                viewPager.setCurrentItem(tab.getPosition());
                for (int i = 0; i < 5; i++) {
                    if (tab.getPosition() == i) {
                        // 탭이 선택되었을 경우, 아이콘 색상은 검은색
                        tabLayout.getTabAt(i).getIcon().setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_IN);
                    }
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                for (int i = 0; i < 6; i++) {
                    if (tab.getPosition() == i) {
                        // 탭이 선택되지 않았을 경우, 아이콘 색상은 회색
                        tabLayout.getTabAt(i).getIcon().setColorFilter(getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.SRC_IN);
                    }
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void setUp(){
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        coordinatorLayout = findViewById(R.id.root);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        main_btn_logout = (Button)findViewById(R.id.main_btn_logout);
        main_btn_signout = (Button)findViewById(R.id.main_btn_signout);
    }

    public void out(){
        main_btn_logout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });
        main_btn_signout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("탈퇴하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        int result = errorResult.getErrorCode();

                                        if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                            Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "회원탈퇴에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        Toast.makeText(getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onNotSignedUp() {
                                        Toast.makeText(getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onSuccess(Long result) {
                                        Toast.makeText(getApplicationContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        }); //
    }

    private void HashKey(){
        try {
            PackageInfo pkinfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : pkinfo.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                String result = new String(Base64.encode(messageDigest.digest(), 0));
                Log.d("해시", result);
            }
        }
        catch (Exception e) {
        }
    }
}