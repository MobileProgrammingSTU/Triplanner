package com.seoultech.triplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class SelectedPlanner extends AppCompatActivity {

    ListView bannerListView;
    SelectedBannerAdapter adapter;
    ArrayList<PlaceBannerItem> placeDataList; // 장소 data 리스트

    ImageView imgBtnBack;

    TextView textView;

    RelativeLayout att1, cafe1, rest1;
    ImageButton cafeDelete1, attDelete1, restDelete1;
    ImageButton imgBtnAddPlace;
    Button btnNext;

    Bundle bundle;
    int imgData;
    String titleData;
    String typeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_selected);

        bannerListView = (ListView) findViewById(R.id.bannerList);
        placeDataList = new ArrayList<>();
        adapter = new SelectedBannerAdapter(this, R.layout.place_banner_item, placeDataList);
        bannerListView.setAdapter(adapter);

        imgBtnBack = (ImageView) findViewById(R.id.imgBtnBack);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textView = (TextView) findViewById(R.id.textView);

        imgBtnAddPlace = (ImageButton) findViewById(R.id.imgBtnAddPlace);
        btnNext = (Button) findViewById(R.id.btnNext);

        // PlacePlanner.java 에서 putExtra 로 담은 내용을 bundle 에 담는다.
        bundle = getIntent().getExtras();

        // 1일차, 2일차, ...
        Integer day = PlaceIntent.savedDateMap.get("startDay");
        textView.setText(Integer.toString(day) + "일차 장소 선택 내역");

        //PlacePlanner 에서 클릭으로 보낸 data를 받는다
        Intent intent = getIntent();
        imgData = Integer.parseInt(intent.getStringExtra("img")); // String으로 보낸 경로 정보를 다시 Int로 바꿈
        titleData = intent.getStringExtra("title");
        typeData = intent.getStringExtra("type");

        //static ArrayList에 인텐트로 받아온 데이터 누적하기
        PlaceIntent.daySelectedPlace.add(new PlaceBannerItem(imgData, titleData, typeData));
        placeDataList.addAll(PlaceIntent.daySelectedPlace); // 리스트뷰의 데이터 리스트에 모두 추가
        adapter.notifyDataSetChanged(); // 어댑터에 데이터 변경사항 적용, 리스트뷰에 나타남

        bannerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.removeItem(position); // 리스트 뷰에서 지우기
                PlaceIntent.daySelectedPlace.remove(position); // static 리스트 지우기
            }
        });

        PlaceIntent.placeSavedMap = new LinkedHashMap<>();

        if (bundle != null) {
            final Set<String> keySet = bundle.keySet();   // intent 객체로 받아온 전체 keySet

            //for (String s : keySet) {
            //    PlaceIntent.placeSavedMap.put(s, true);
            //}


/*
            // Delete button 을 누르면 data 제거
            cafeDelete1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   cafe1.setVisibility(View.GONE);

                   // 여기서 bundle 객체의 데이터를 제거한다.
                    PlaceIntent.placeSavedMap.put("cafe1_key", false);
                }
            });
            attDelete1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    att1.setVisibility(View.GONE);

                    // 여기서 bundle 객체의 데이터를 제거한다.
                    PlaceIntent.placeSavedMap.put("att1_key", false);
                }
            });
            restDelete1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rest1.setVisibility(View.GONE);

                    // 여기서 bundle 객체의 데이터를 제거한다.
                    PlaceIntent.placeSavedMap.put("rest1_key", false);
                }
            });
*/
            imgBtnAddPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /* test 코드 작성
                    Set<String> test = PlaceIntent.placeSavedMap.keySet();
                    System.out.println("====여기는 SelectedPlanner.java 위치 입니다====");
                    for (String s : test) {
                        boolean value = PlaceIntent.placeSavedMap.get(s);
                        System.out.println(s + "에 담긴 값은 " + value + " 입니다.");
                    }*/

                    //Intent imgBtnAddIntent = new Intent(SelectedPlanner.this, PlacePlanner.class);
                    //startActivity(imgBtnAddIntent);
                    finish();
                }
            });

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int startDay = PlaceIntent.savedDateMap.get("startDay");
                    int endDay = PlaceIntent.savedDateMap.get("endDay");

                    /* 각 일차마다 저장된 값을 Place.savedPlacesMap 에 넣어준다.
                    LinkedList<String> list = new LinkedList<>();
                    for (String s : keySet) {
                        list.add(s);
                    }
                    PlaceIntent.savedPlacesMap.put(startDay, list);

                    if (startDay < endDay) {
                        // 여기서 PlacePlanner 의 날짜 값을 +1 증가시킴
                        PlaceIntent.savedDateMap.put("startDay", startDay + 1);

                        // 날짜가 변경될 때 마다 해당 날짜에 맞는 새로운 Intent 객체를 생성한다.
                        PlaceIntent.placeIntent = new Intent();

                        Intent intentBack = new Intent(SelectedPlanner.this, PlacePlanner.class);
                        startActivity(intentBack);
                    }
                    else {
                        Intent intentFinish = new Intent(SelectedPlanner.this, FinishPlanner.class);
                        startActivity(intentFinish);
                    }
                     */
                    ArrayList<PlaceBannerItem> list = new ArrayList<PlaceBannerItem>();
                    list.addAll(PlaceIntent.daySelectedPlace);

                    PlaceIntent.savedPlacesMap.put(startDay, list);

                    if (startDay < endDay) {
                        // 여기서 PlacePlanner 의 날짜 값을 +1 증가시킴
                        PlaceIntent.savedDateMap.put("startDay", startDay + 1);

                        // 날짜가 변경될 때 마다 해당 날짜에 맞는 새로운 Intent 객체를 생성한다.
                        PlaceIntent.placeIntent = new Intent();
                        // 일차별 사용하는 리스트도 새롭게 비워준다
                        PlaceIntent.daySelectedPlace.clear();

                        Intent intentBack = new Intent(SelectedPlanner.this, PlacePlanner.class);
                        startActivity(intentBack);
                    }
                    else {
                        // 모두 초기화
                        PlaceIntent.placeIntent = new Intent();
                        PlaceIntent.daySelectedPlace.clear();

                        Intent intentFinish = new Intent(SelectedPlanner.this, FinishPlanner.class);
                        startActivity(intentFinish);
                    }
                }
            });

        }
    }
}
