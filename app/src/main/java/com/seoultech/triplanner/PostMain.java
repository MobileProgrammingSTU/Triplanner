package com.seoultech.triplanner;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.seoultech.triplanner.Model.PostItem;

import java.util.ArrayList;
import java.util.HashMap;

public class PostMain extends AppCompatActivity {
    RelativeLayout menu;
    ImageButton btnBack;
    ImageView btnSave, btnLike;

    TextView title, subtitle, publisher, content;
    Button location;
    Button typeRegion, typePlace;

    String postId;

    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;

    private ArrayList<String> images = new ArrayList<String>(); // imgUrl 리스트

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final String fbCurrentUserUID = mFirebaseAuth.getUid();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_post);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("Triplanner");    // DB table connect

        title = findViewById(R.id.postTitle);
        subtitle = findViewById(R.id.postSubtitle);
        publisher = findViewById(R.id.postPublisher);
        location = findViewById(R.id.postLocation);
        typeRegion = findViewById(R.id.postRegionType);
        typePlace = findViewById(R.id.postPlaceType);

        menu = findViewById(R.id.layoutMenu);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.save);
        btnLike = findViewById(R.id.like);

        menu.bringToFront();

        // 홈에서 클릭한 포스트의 pid 받아오기
        Intent intent = getIntent();
        postId = intent.getStringExtra("pid");
        // 찾아서하면 오류가 발생하여 url 리스트를 intent 로 받음
        images = intent.getStringArrayListExtra("images");

        // Firebase 에서 pid로 찾아 data 받아와서 매칭
        Query val = mDatabaseRef.child("Post2").orderByChild("pid").equalTo(postId);
        val.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    PostItem fbPost = postSnapshot.getValue(PostItem.class);
                    String fbTitle = fbPost.getTitle();
                    String fbSubtitle = fbPost.getSubtitle();
                    String fbPublisher = fbPost.getPublisher();

                    String fbTypeRegion = fbPost.getTypeRegion();
                    String fbTypePlace = fbPost.getTypePlace();

                    HashMap<String, String> fbImages = fbPost.getImages();

                    title.setText(fbTitle);
                    subtitle.setText(fbSubtitle);
                    publisher.setText(fbPublisher + " 님");

                    // place 타입 설정
                    if(fbTypePlace.contains("cafe") || fbTypePlace.contains("카페")) {
                        typePlace.setText("카 페");
                        typePlace.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2BD993")));
                    }
                    else if(fbTypePlace.contains("rest") || fbTypePlace.contains("맛집")) {
                        typePlace.setText("맛 집");
                        typePlace.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F26C73")));
                    }
                    else if(fbTypePlace.contains("att") || fbTypePlace.contains("명소")) {
                        typePlace.setText("명 소");
                        typePlace.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFD37A")));
                    }
                    else{
                        typePlace.setText("기 타");
                    }

                    // region 타입 설정
                    if(fbTypeRegion.contains("N") || fbTypeRegion.contains("북부")) {
                        typeRegion.setText("북 부");
                        typeRegion.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5B4FBB")));
                    }
                    else if(fbTypeRegion.contains("S") || fbTypeRegion.contains("남부")) {
                        typeRegion.setText("남 부");
                        typeRegion.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F26C73")));
                    }
                    else{
                        typePlace.setText("기 타");
                    }

                    // 슬라이드 인디케이터
                    setupIndicators(images.size());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase", "loadPost:onCancelled", databaseError.toException());
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
                    mDatabaseRef.child("UserAccount").child(fbCurrentUserUID).child("Likes")
                            .child(postId).setValue(true);
                    Toast.makeText(getApplicationContext(), "좋아요를 눌렀습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    mDatabaseRef.child("UserAccount").child(fbCurrentUserUID).child("Likes")
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

        //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = mDatabaseRef.child("UserAccount").child(fbCurrentUserUID)
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
