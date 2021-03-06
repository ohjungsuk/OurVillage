package com.ajou.ourvillage.Friend;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajou.ourvillage.Main.AddMyFeed;
import com.ajou.ourvillage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class FriendFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;



    public FriendFragment() {
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
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        firebaseFirestore=FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reload();
    }
    public void reload(){
        ArrayList<FriendListInfo> dataList = new ArrayList<>();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String ff_nickname = null;
                                for (UserInfo profile : firebaseUser.getProviderData()) {
                                    ff_nickname = profile.getDisplayName();
                                }
                                if (ff_nickname.equals(document.getData().get("my_nickname").toString())){
                                    dataList.add(new FriendListInfo(
                                            document.getData().get("my_nickname").toString(),
                                            document.getData().get("friend_nickname").toString(),
                                            document.getData().get("address").toString(),
                                            document.getId().toString()
                                    ));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                //    WriteFeedInfo writeFeedInfo = document.getData().get(WriteFeedInfo.class);


                            }
                            RecyclerView recyclerView = getActivity().findViewById(R.id.friend_recyclerview);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(layoutManager);

                            FriendAdapter friendAdapter = new FriendAdapter(dataList);
                            recyclerView.setAdapter(friendAdapter);
                            recyclerView.getAdapter().notifyDataSetChanged();
                            friendAdapter.setOnItemClicklistener(new OnFriendItemClickListener() {
                                @Override
                                public void onItemClick(FriendAdapter.ViewHolder holder, View view, int position) {
                                    FriendListInfo pos = friendAdapter.getItem(position);
                                    new AlertDialog.Builder(getContext())
                                            .setMessage("???????????? ?????????????????????????")
                                            .setPositiveButton("???", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    firebaseFirestore.collection("friends").document(pos.getId())
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d("rrtest", pos.getId());
                                                                    Toast.makeText(view.getContext(), "??????????????????", Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(view.getContext(), "???????????? ??????", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                    dialogInterface.dismiss();
                                                    reload();
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
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
