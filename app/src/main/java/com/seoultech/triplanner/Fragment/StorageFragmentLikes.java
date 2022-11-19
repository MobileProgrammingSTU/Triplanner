package com.seoultech.triplanner.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.seoultech.triplanner.PostMain;
import com.seoultech.triplanner.R;
import com.seoultech.triplanner.bannerPostAdapter;

import java.util.ArrayList;

public class StorageFragmentLikes extends Fragment {

    ListView listView;
    bannerPostAdapter adapter;
    ArrayList<String> arrayLikesID = new ArrayList<String>(); // 좋아요 게시물 ID 리스트
    ArrayList<PostItem> listLikedPost = new ArrayList<PostItem>(); // post 배너 리스트뷰 아이템 리스트

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final String fbCurrentUserUID = mFirebaseAuth.getUid();
    private DatabaseReference mDatabaseRef;

    public StorageFragmentLikes() {

    }

    public static StorageFragmentLikes newInstance() {
        StorageFragmentLikes storageFragment = new StorageFragmentLikes();
        Bundle bundle = new Bundle();
        return storageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.storage_fragment_likes, container, false);

        listView = (ListView) view.findViewById(R.id.listPost);
        adapter = new bannerPostAdapter(getContext(), R.layout.place_selected_banner_item, listLikedPost, false);
        listView.setAdapter(adapter);

        // Database
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Triplanner");
        DatabaseReference userDataRef = mDatabaseRef.child("UserAccount").child(fbCurrentUserUID);

        userDataRef.child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayLikesID.clear();
                // 좋아요 pid 수집 : arrayLikesID 채우기
                for(DataSnapshot likesSnapshot : snapshot.getChildren()) {
                    String result = likesSnapshot.getKey(); // 키를 읽어옴
                    //System.out.println("출력"+result);
                    arrayLikesID.add(result);
                }

                // pid 검색하여 PostItem data 받아오기 : listLikedPost 채우기
                mDatabaseRef.child("Post").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listLikedPost.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            PostItem post = dataSnapshot.getValue(PostItem.class);
                            for (String pid : arrayLikesID) {
                                if (post.getPid().equals(pid))
                                    listLikedPost.add(post);
                            }
                        }
                        adapter.notifyDataSetChanged();

                        // 리스트뷰 아이템(배너) 클릭 이벤트
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                PostItem postItem = (PostItem) adapter.getItem(position);

                                Intent intent = new Intent(getContext(), PostMain.class);
                                intent.putExtra("pid", postItem.getPid());
                                getContext().startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("getFirebaseDatabase", "loadPost:onCancelled", error.toException());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", error.toException());
            }
        });

        return view;
    }
}
