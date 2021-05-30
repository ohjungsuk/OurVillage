package com.ajou.ourvillage.MyPage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.ourvillage.Login.LoginActivity;
import com.ajou.ourvillage.Main.MainFragment;
import com.ajou.ourvillage.Main.MainPostAdapter;
import com.ajou.ourvillage.Main.OnMainItemClickLIstener;
import com.ajou.ourvillage.Main.WriteFeedInfo;
import com.ajou.ourvillage.MainActivity;
import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;


public class MypageFragment extends Fragment {

    private FirebaseUser firebaseUser;
    private TextView mypage_email, mypage_Nickname;
    private Button mypage_add_post, mypage_modify_pw, mypage_cancel1, mypage_cancel2,mypage_NameModify,
            mypage_btn_MyGoodPost, mypage_MyCommentPost, mypage_MyPost, mypage_EmailAuth;
    private Button mypage_btn_logout, mypage_btn_signout;
    private CircleImageView mypage_imgbtn_profile;
    private LinearLayout mypage_linear_modify;
    private EditText mypage_edt_name, mypage_edt_PwChange_byEmail;
    private boolean activity_stack_check = true;


    private void init(View v){
        mypage_Nickname = (TextView)v.findViewById(R.id.mypage_Nickname);
        mypage_email = (TextView)v.findViewById(R.id.mypage_email);

        mypage_EmailAuth = (Button)v.findViewById(R.id.mypage_EmailAuth);
        mypage_linear_modify = (LinearLayout) v.findViewById(R.id.mypage_linear_modify);
        mypage_modify_pw = (Button) v.findViewById(R.id.mypage_modify_pw);

        mypage_cancel2 = (Button) v.findViewById(R.id.mypage_cancel2);

        mypage_edt_PwChange_byEmail= (EditText) v.findViewById(R.id.mypage_edt_PwChange_byEmail);
        mypage_btn_logout = (Button) v.findViewById(R.id.mypage_btn_logout);
        mypage_btn_signout = (Button) v.findViewById(R.id.mypage_btn_signout);
    }

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private String mf_nickname = null;
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



        mypage_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mypage_linear_modify.getVisibility() == v.VISIBLE) {
                    mypage_linear_modify.setVisibility(v.GONE);
                }
            }
        });

        mypage_btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("로그아웃을 하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseAuth.signOut();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
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

        mypage_btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("정말 계정을 삭제할까요?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        for (UserInfo profile : firebaseUser.getProviderData()) {
                                            if(activity_stack_check){
                                                db = FirebaseFirestore.getInstance();
                                                String db_email = profile.getEmail();
                                                db.collection(db_email).document(firebaseUser.getUid()).delete();
                                                firebaseAuth.signOut();
                                                firebaseUser.delete();
                                                Toast.makeText(getContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                startActivity(intent);
                                                activity_stack_check =false;
                                                getActivity().finish();
                                                dialogInterface.dismiss();
                                            }
                                        }
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
    @Override
    public void onResume() {
        super.onResume();

        ArrayList<WriteFeedInfo> dataList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Feed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot feedfile : task.getResult()) {
                                for (UserInfo profile : firebaseUser.getProviderData()) {
                                    mf_nickname = profile.getDisplayName();
                                }
                                if(feedfile.getData().get("writer").equals(mf_nickname)){
                                    dataList.add(new WriteFeedInfo(
                                            feedfile.getData().get("writer").toString(),
                                            feedfile.getData().get("date").toString(),
                                            feedfile.getData().get("title").toString(),
                                            feedfile.getData().get("img_profile").toString(),
                                            feedfile.getData().get("content").toString(),
                                            feedfile.getData().get("likeCnt").toString(),
                                            feedfile.getData().get("commentCount").toString(),
                                            feedfile.getId().toString()
                                    ));
                                }

                            }
                            RecyclerView recyclerView = getActivity().findViewById(R.id.mypage_recyclerview);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(layoutManager);

                            MainPostAdapter mainPostAdapter = new MainPostAdapter(dataList);
                            recyclerView.setAdapter(mainPostAdapter);
                            recyclerView.getAdapter().notifyDataSetChanged();
                            mainPostAdapter.setOnItemClicklistener(new OnMainItemClickLIstener() {
                                @Override
                                public void onItemClick(MainPostAdapter.ViewHolder holder, View view, int position) {
                                    WriteFeedInfo pos = mainPostAdapter.getItem(position);
                                    Log.d("rtest",String.valueOf(pos.getId()));
                                    //showPopup(view,pos);
                                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem item) {
                                            switch (item.getItemId()){
                                                case R.id.menu_refactor:

                                                    return true;
                                                case R.id.menu_delete:
                                                    if(mf_nickname.equals(pos.getWriter())){
                                                        db.collection("Feed").document(pos.getId())
                                                                .delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Log.d("rrtest", pos.getId());
                                                                        Toast.makeText(view.getContext(), "게시글을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(view.getContext(), "게시글 삭제를 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }else {
 //                                                       Toast.makeText(view.getContext(), "내 계정만 삭제할수 있습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                    return true;
                                                default:
                                                    return false;
                                            }
                                        }
                                    });
                                    MenuInflater inflater = popupMenu.getMenuInflater();
                                    inflater.inflate(R.menu.post_menu, popupMenu.getMenu());
                                    popupMenu.show();
                                }
                            });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}