package com.seoultech.triplanner.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seoultech.triplanner.Model.PostItem;
import com.seoultech.triplanner.Model.UserAccount;
import com.seoultech.triplanner.PostMain;
import com.seoultech.triplanner.R;
import com.seoultech.triplanner.bannerPostAdapter;

import java.util.ArrayList;

public class StorageFragmentMyPost extends Fragment {

    ListView listMyPost;
    bannerPostAdapter adapter;
    ArrayList<PostItem> listWritePost = new ArrayList<>();  // post 배너 리스트뷰 아이템 리스트

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final String fbCurrentUserUID = mFirebaseAuth.getUid();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef;
    private String currentUserFbName;

    public StorageFragmentMyPost () {}

    public static StorageFragmentMyPost newInstance() {
        StorageFragmentMyPost storageFragment = new StorageFragmentMyPost();
        Bundle bundle = new Bundle();
        return storageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.storage_fragment_mypost, container, false);

        listMyPost = (ListView) view.findViewById(R.id.listMyPost);
        adapter = new bannerPostAdapter(getContext(), R.layout.place_selected_banner_item,
                listWritePost, false);
        listMyPost.setAdapter(adapter);

        // Database
        mDatabaseRef = mDatabase.getReference("Triplanner");

        // 현재 로그인된 User 의 FbName 을 db 에서 읽어온다.
        mDatabaseRef.child("UserAccount").child(fbCurrentUserUID).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserAccount userAccount = snapshot.getValue(UserAccount.class);
                        currentUserFbName = userAccount.getFbName();

                        // Post2 에서 현재 로그인된 User 의 FbName 을 검색해서 해당되는 data 읽어오기
                        mDatabaseRef.child("Post2").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                listWritePost.clear();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    PostItem postItem = dataSnapshot.getValue(PostItem.class);

                                    // 현재 User 의 FbName 과 Post2 에 있는 게시물의 Publisher 가 동일한지 비교
                                    // 동일한 경우에만, 데이터를 가져온다.
                                    if (postItem.getPublisher().equals(currentUserFbName)){
                                        listWritePost.add(postItem);
                                    }
                                }

                                adapter.notifyDataSetChanged();

                                // 리스트뷰 아이템(배너) 클릭 이벤트
                                listMyPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                        PostItem postItem = (PostItem) adapter.getItem(position);
                                        ArrayList<String> listImgurl = new ArrayList<>();

                                        Intent intent = new Intent(getContext(), PostMain.class);
                                        intent.putExtra("pid", postItem.getPid());

                                        // 이미지 url 데이터를 리스트 형태로 보냄 : DB 에서 찾아서하면 로드 오류남
                                        listImgurl.addAll(postItem.getImages().values());
                                        intent.putExtra("images", listImgurl);

                                        getContext().startActivity(intent);
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }
}