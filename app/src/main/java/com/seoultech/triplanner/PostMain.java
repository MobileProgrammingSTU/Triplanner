package com.seoultech.triplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.seoultech.triplanner.Model.PostItem;

import java.util.ArrayList;

public class PostMain extends AppCompatActivity {
    ImageButton btnBack;
    ImageView btnSave, btnLike;

    TextView title, subtitle, publisher, content;
    Button location;
    Button typeRegion, typePlace;

    private Intent intent;
    String postId;

    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;

    private ArrayList<String> images = new ArrayList<String>();

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_post);

        mDatabase = FirebaseDatabase.getInstance().getReference("Triplanner").child("Post");

        title = findViewById(R.id.postTitle);
        subtitle = findViewById(R.id.postSubtitle);
        publisher = findViewById(R.id.postPublisher);
        location = findViewById(R.id.postLocation);
        typeRegion = findViewById(R.id.postRegionType);
        typePlace = findViewById(R.id.postPlaceType);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.save);
        btnLike = findViewById(R.id.like);

        // 홈에서 클릭한 포스트의 pid 받아오기
        intent = getIntent();
        postId = intent.getStringExtra("pid");

        // Firebase 에서 pid로 찾아 data 받아와서 매칭
        Query val = mDatabase.orderByChild("pid").equalTo(postId);
        val.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    PostItem fbPost = postSnapshot.getValue(PostItem.class);
                    String fbTitle = fbPost.getTitle();
                    String fbSubtitle = fbPost.getSubtitle();
                    String fbPublisher = fbPost.getPublisher();
                    String fbImgurl = fbPost.getImgurl();

                    title.setText(fbTitle);
                    subtitle.setText(fbSubtitle);
                    publisher.setText(fbPublisher + " 님");
                    images.add(fbImgurl);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // 이미지 좌/우 슬라이드
        sliderViewPager = findViewById(R.id.sliderViewPager);
        layoutIndicator = findViewById(R.id.layoutIndicators);
        sliderViewPager.setOffscreenPageLimit(3);
        sliderViewPager.setAdapter(new ImageSliderAdapter(this, images));
        sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
        // 슬라이드 인디케이터
        setupIndicators(images.size());

        //뒤로가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //게시물 플랜 저장 버튼 클릭
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnSave.isSelected()) {
                    btnSave.setImageResource(R.drawable.ic_save);
                }
                else {
                    btnSave.setImageResource(R.drawable.ic_save_selected);
                    Toast.makeText(getApplicationContext(),"플랜을 보관함에 저장했습니다", Toast.LENGTH_SHORT).show();
                }
                btnSave.setSelected(!btnSave.isSelected());
            }
        });

        isLiked(postId, btnLike);
        //하트 버튼 클릭
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnLike.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference("Triplanner").child("Likes")
                            .child(postId).setValue(true);
                    Toast.makeText(getApplicationContext(), "좋아요를 눌렀습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseDatabase.getInstance().getReference("Triplanner").child("Likes")
                            .child(postId).removeValue();
                }
            }
        });

    }

    //게시물 슬라이드 인디케이터(점) 생성
    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.activity_main_layoutindicators_inactive));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }
    //현재 게시물 슬라이드 인디케이터(점)
    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.activity_main_layoutindicators_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.activity_main_layoutindicators_inactive
                ));
            }
        }
    }

    private void isLiked(String postid, ImageView imageView) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Triplanner")
                .child("Likes").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if (snapshot.child(firebaseUser.getUid()).exists()) {
                if (snapshot.exists()) {
                    imageView.setImageResource(R.drawable.ic_heart_filled);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_heart);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
