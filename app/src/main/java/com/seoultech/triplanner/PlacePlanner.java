package com.seoultech.triplanner;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seoultech.triplanner.Model.PlaceIntent;
import com.seoultech.triplanner.Model.PostItem;

import java.util.ArrayList;
import java.util.Objects;

public class PlacePlanner extends AppCompatActivity {

    ListView bannerListView;
    bannerPostAdapter adapter;
    ArrayList<PostItem> placeDataList; // 장소 data 리스트
    ArrayList<String> arrayLikesID = new ArrayList<String>(); // 좋아요 게시물 ID 리스트

    ImageButton imgBtnBack;

    TextView textView;
    Button btnAttraction, btnRestaurant, btnCafe;
    
    Bundle bundle;
    String typeRegion; // Region Place 에서 클릭한 지역 정보

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final String fbCurrentUserUID = mFirebaseAuth.getUid();
    private DatabaseReference mDatabaseRef;

    int colBlue;
    int colBG1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_place);

        colBlue = ContextCompat.getColor(getApplicationContext(), R.color.colorBrandBlue);
        colBG1 = ContextCompat.getColor(getApplicationContext(), R.color.colorGrayBG1);

        // Database
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Triplanner");
        DatabaseReference userDataRef = mDatabaseRef.child("UserAccount").child(fbCurrentUserUID);

        imgBtnBack = (ImageButton) findViewById(R.id.imgBtnBack);

        textView = (TextView) findViewById(R.id.textView);
        btnAttraction = (Button) findViewById(R.id.btnAtt);
        btnRestaurant = (Button) findViewById(R.id.btnRes);
        btnCafe = (Button) findViewById(R.id.btnCafe);
        btnAttraction.setSelected(false);
        btnRestaurant.setSelected(false);
        btnCafe.setSelected(false);

        bannerListView = (ListView) findViewById(R.id.bannerList);
        placeDataList = new ArrayList<>();

        //어댑터에 배너 기본 형식과 장소 data 리스트 적용
        adapter = new bannerPostAdapter(this, R.layout.place_selected_banner_item, placeDataList, true);
        //리스트뷰에 어댑터 적용
        bannerListView.setAdapter(adapter);

        // Region Place 에서 클릭한 지역 정보 받아오기
        bundle = getIntent().getExtras();
        typeRegion = PlaceIntent.intentRegionType;
        //typeRegion = bundle.getString("regionType"); // 클릭한 지역 정보
        //Toast.makeText(this.getApplicationContext(),typeRegion, Toast.LENGTH_SHORT).show();

        //SelectedPlanner로 데이터를 보내기위해 인텐트 선언
        PlaceIntent.placeIntent.setClass(PlacePlanner.this, SelectedPlanner.class);

        // 좋아요 pid 수집
        userDataRef.child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayLikesID.clear();
                for(DataSnapshot likesSnapshot : snapshot.getChildren()) {
                    String result = likesSnapshot.getKey(); // 키를 읽어옴
                    //System.out.println("출력"+result);
                    arrayLikesID.add(result);
                }

                mDatabaseRef.child("Post2").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        placeDataList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            PostItem post = dataSnapshot.getValue(PostItem.class);
                            for (String pid : arrayLikesID) {
                                if (post.getPid().equals(pid))
                                    // 장소 타입 매칭하기
                                    if (Objects.equals(post.getTypeRegion(), typeRegion))
                                        placeDataList.add(post);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("getFirebaseDatabase", "loadPost:onCancelled", error.toException());
                    }
                });

                // 리스트뷰의 아이템(배너) 클릭 이벤트 : intent 로 data 전송
                bannerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        //선택한 배너의 아이템 정보 객체
                        PostItem item = (PostItem) adapter.getItem(position);

                        //아이템 정보를 번들에 묶음
                        Bundle extras = new Bundle();
                        extras.putString("pid", item.getPid());
                        extras.putString("img", item.getThumbnail());
                        extras.putString("title", item.getTitle());
                        extras.putString("type", item.getTypePlace());
                        // 이후 수정 사항 : 이렇게 따로 보내지 말고 PostItem 객체를 인텐트로 넘기는 방법 찾기

                        //번들을 보냄
                        PlaceIntent.placeIntent.putExtras(extras);
                        startActivity(PlaceIntent.placeIntent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", error.toException());
            }
        });

        filterClickListener();

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // selected, place 넘어서 뒤로가면 Map 플랜데이터 초기화
                PlaceIntent.placeIntent = new Intent();
                PlaceIntent.daySelectedPlace.clear();
                //PlaceIntent.savedPlacesMap.clear();
                finish();
            }
        });

        // 1일차, 2일차, ...
        Integer day = PlaceIntent.savedDateMap.get("startDay");
        textView.setText(day + "일차에 방문할 장소를 선택하세요");
    }

    public void filterClickListener() {
        //명소 필터 버튼 클릭
        btnAttraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnAttraction.isSelected()){
                    btnAttraction.setBackgroundTintList(ColorStateList.valueOf(colBG1));
                    adapter.removeFilterType(bannerPostAdapter.ATT); // (type:att)를 리스트에서 제거
                }
                else {
                    btnAttraction.setBackgroundTintList(ColorStateList.valueOf(colBlue));
                    adapter.addFilterType(bannerPostAdapter.ATT); // (type:att)를 리스트에 띄움
                }
                btnAttraction.setSelected(!btnAttraction.isSelected());
            }
        });
        //맛집 필터 버튼 클릭
        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnRestaurant.isSelected()){
                    btnRestaurant.setBackgroundTintList(ColorStateList.valueOf(colBG1));
                    adapter.removeFilterType(bannerPostAdapter.REST); // (type:rest)를 리스트에서 제거
                }
                else {
                    btnRestaurant.setBackgroundTintList(ColorStateList.valueOf(colBlue));
                    adapter.addFilterType(bannerPostAdapter.REST); // (type:rest)를 리스트에 띄움
                }
                btnRestaurant.setSelected(!btnRestaurant.isSelected());
            }
        });
        //카페 필터 버튼 클릭
        btnCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnCafe.isSelected()){
                    btnCafe.setBackgroundTintList(ColorStateList.valueOf(colBG1));
                    adapter.removeFilterType(bannerPostAdapter.CAFE); // (type:cafe)를 리스트에서 제거
                }
                else {
                    btnCafe.setBackgroundTintList(ColorStateList.valueOf(colBlue));
                    adapter.addFilterType(bannerPostAdapter.CAFE); // (type:cafe)를 리스트에 띄움
                }
                btnCafe.setSelected(!btnCafe.isSelected());
            }
        });
    }
}