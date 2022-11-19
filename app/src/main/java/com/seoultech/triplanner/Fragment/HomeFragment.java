package com.seoultech.triplanner.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seoultech.triplanner.Model.PostItem;
import com.seoultech.triplanner.PostAdapter;
import com.seoultech.triplanner.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    AppBarLayout appBar;

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<PostItem> postLists; // 포스트(아이템) 리스트

    private List<String> followingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        appBar = view.findViewById(R.id.bar);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postLists);
        recyclerView.setAdapter(postAdapter);

        appBar.bringToFront();

        //checkFollowing();
        readPosts();

        return view;
    }

    private void checkFollowing(){
        followingList = new ArrayList<>();

        // 이 부분에서 현재 유저가 팔로우한 유저의 데이터를 가져오는 것이 아닌 가입된 모든 유저의 포스트 정보를 가져오도록 수정해야 합니다
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Triplanner")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("follwing");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    followingList.add(dataSnapshot.getKey());
                }

                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readPosts(){
        //DB로부터 post item 형태의 데이터를 읽어서 리사이클뷰(피드)의 포스트(아이템) 리스트에 추가합니다
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Triplanner").child("Post2");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postLists.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PostItem post = dataSnapshot.getValue(PostItem.class);
                    postLists.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
