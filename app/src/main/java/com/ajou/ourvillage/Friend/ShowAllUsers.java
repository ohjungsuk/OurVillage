package com.ajou.ourvillage.Friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kakao.usermgmt.response.model.User;

import java.util.ArrayList;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class ShowAllUsers extends AppCompatActivity {
    private ArrayList<UserListInfo> friendlist;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    private boolean isFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_users);
    }

    @Override
    public void onResume() {
        super.onResume();

//        RecyclerView recyclerView = getActivity().findViewById(R.id.friend_recyclerview);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//
//        ArrayList<FriendListItem> dataList = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            dataList.add(new FriendListItem(R.drawable.ic_launcher_background, "친구","동탄"));
//        }
//
//        FriendAdapter friendAdapter = new FriendAdapter(dataList);
//        recyclerView.setAdapter(friendAdapter);
//        recyclerView.getAdapter().notifyDataSetChanged();
        ArrayList<UserListInfo> dataList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //    WriteFeedInfo writeFeedInfo = document.getData().get(WriteFeedInfo.class);
                                dataList.add(new UserListInfo(
                                        document.getData().get("nickname").toString(),
                                        document.getData().get("address").toString()
                                ));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.allusers_recyclerview);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ShowAllUsers.this);
                            recyclerView.setLayoutManager(layoutManager);

                            AllUsersAdapter allUsersAdapter = new AllUsersAdapter(dataList);
                            recyclerView.setAdapter(allUsersAdapter);
                            recyclerView.getAdapter().notifyDataSetChanged();
                            allUsersAdapter.setOnItemClicklistener(new OnUserItemClickListener() {
                                @Override
                                public void onItemClick(AllUsersAdapter.ViewHolder holder, View view, int position) {
                                    UserListInfo item = allUsersAdapter.getItem(position);
                                    //Toast.makeText(getApplicationContext(),"아이템 선택 " + item.getNickname() + item.getAddress(), Toast.LENGTH_LONG).show();
                                    new AlertDialog.Builder(ShowAllUsers.this)
                                            .setMessage(item.getNickname()+ "를(을) 친구추가 할까요?")
                                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
//                                                    Intent intent = new Intent(getApplicationContext(), FriendFragment.class);
//                                                    intent.putExtra("nickname", item.getNickname());
//                                                    intent.putExtra("address", item.getAddress());
//                                                    FriendFragment friendFragment = new FriendFragment();
//
//                                                    Bundle bundle = new Bundle(2);
//                                                    bundle.putString("nname", item.getNickname());
//                                                    bundle.putString("addre", item.getAddress());
//                                                    friendFragment.setArguments(bundle);

                                                    String f_nickname = null;
                                                    UserListInfo userListInfo = new UserListInfo(item.getNickname(),item.getAddress());
                                                    for (UserInfo profile : firebaseUser.getProviderData()) {
                                                        f_nickname = profile.getDisplayName();
                                                    }
//                                                    firebaseFirestore.collection("friends").document(f_nickname).set(userListInfo)
//                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                @Override
//                                                                public void onSuccess(Void unused) {
//                                                                    Toast.makeText(getApplicationContext(),"친구등록 성공1 " + item.getNickname() + item.getAddress(), Toast.LENGTH_LONG).show();
//                                                                }
//                                                            });
                                                    firebaseFirestore.collection("friends")
                                                            .add(userListInfo)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    Toast.makeText(getApplicationContext(),
                                                                            "친구 등록 성공", Toast.LENGTH_SHORT).show();

                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getApplicationContext(),
                                                                            "친구등록 실패", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                    //finish();
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
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void addFriend(){


    }
}