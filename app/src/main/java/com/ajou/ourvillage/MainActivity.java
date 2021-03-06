package com.ajou.ourvillage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.PorterDuff;
import android.os.Build;
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
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private PermissionSupport permission;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    CoordinatorLayout coordinatorLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private Button main_btn_logout,main_btn_signout;
    private boolean activity_stack_check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        petrmissionCheck(); // ?????? ??????

        Log.d("stack","MainActivity");
        setUp();
        //HashKey();
        //out();

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(view -> {

        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //????????? ????????? ?????? ?????? ???????????? null ???????????? ??? ??????????????? ???????????? ????????? ??????????????? ??????.
        if(firebaseUser == null) {
            Toast.makeText(getApplicationContext(),"nonUser",Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(intent);
            mStartActivity(LoginActivity.class);
            finish();

        }else { //login //????????? ?????????, null??? ????????? ?????? ??????

        }

//        main_btn_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new AlertDialog.Builder(MainActivity.this)
//                        .setMessage("??????????????? ???????????????????")
//                        .setPositiveButton("???", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                firebaseAuth.signOut();
//                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        }).show();
//
//            }
//        });
//
//        main_btn_signout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_main));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_apartment));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_tasty));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_friend));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_mypage));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        // TabPagerAdapter ??????
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabPagerAdapter);

        // PageChangeListener ??????
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // ????????? ??? ?????? ??? ???????????? ?????? ?????????, ??? ???????????? ?????? ????????? ????????? ??????????????? ??????
        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.theme), PorterDuff.Mode.SRC_IN);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // ??????????????? ?????? ???????????? ???????????? ???????????? ??????
                viewPager.setCurrentItem(tab.getPosition());
                for (int i = 0; i < 5; i++) {
                    if (tab.getPosition() == i) {
                        // ?????? ??????????????? ??????, ????????? ????????? ?????????
                        tabLayout.getTabAt(i).getIcon().setColorFilter(getResources().getColor(R.color.theme), PorterDuff.Mode.SRC_IN);
                    }
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                for (int i = 0; i < 6; i++) {
                    if (tab.getPosition() == i) {
                        // ?????? ???????????? ????????? ??????, ????????? ????????? ??????
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
//        main_btn_logout = (Button)findViewById(R.id.main_btn_logout);
//        main_btn_signout = (Button)findViewById(R.id.main_btn_signout);
    }

    public void out(){  //????????????
        main_btn_logout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "??????????????? ???????????????????????????.", Toast.LENGTH_SHORT).show();

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
                        .setMessage("?????????????????????????")
                        .setPositiveButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        int result = errorResult.getErrorCode();

                                        if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                            Toast.makeText(getApplicationContext(), "???????????? ????????? ??????????????????. ?????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "??????????????? ??????????????????. ?????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        Toast.makeText(getApplicationContext(), "????????? ????????? ???????????????. ?????? ???????????? ?????????.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onNotSignedUp() {
                                        Toast.makeText(getApplicationContext(), "???????????? ?????? ???????????????. ?????? ???????????? ?????????.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onSuccess(Long result) {
                                        Toast.makeText(getApplicationContext(), "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("?????????", new DialogInterface.OnClickListener() {
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
                Log.d("??????", result);
            }
        }
        catch (Exception e) {
        }
    }

    private void mStartActivity(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // ?????? ??????
    private void permissionCheck(){

        // SDK 23?????? ?????? ??????????????? Permission ?????????
        if(Build.VERSION.SDK_INT >= 23){
            // ?????? ??? ???????????? ????????? ?????? ??????
            permission = new PermissionSupport(this, this);

            // ?????? ????????? ?????? ????????? false??? ???????????????
            if (!permission.checkPermission()){
                // ?????? ????????? ?????????.
                permission.requestPermission();
            }
        }
    }

    // Request Permission??? ?????? ?????? ?????? ????????? ??? ????????????.
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // ???????????? ????????? false??? ??????????????? (???????????? ?????? ????????? ??????????????????)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!permission.permissionResult(requestCode, permissions, grantResults)) {
            // ????????? ?????? Permission ??????
            permission.requestPermission();
        }
    }
}