package com.seoultech.triplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

public class SelectedPlanner extends AppCompatActivity {

    ImageView imgBtnBack;

    TextView textView;
    RelativeLayout att1, cafe1, rest1;
    ImageButton cafeDelete1, attDelete1, restDelete1;
    ImageButton imgBtnAddPlace;
    Button btnNext;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_selected);

        imgBtnBack = (ImageView) findViewById(R.id.imgBtnBack);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textView = (TextView) findViewById(R.id.textView);

        att1 = (RelativeLayout) findViewById(R.id.att1);
        cafe1 = (RelativeLayout) findViewById(R.id.cafe1);
        rest1 = (RelativeLayout) findViewById(R.id.rest1);

        cafeDelete1 = (ImageButton) findViewById(R.id.cafeDelete1);
        attDelete1 = (ImageButton) findViewById(R.id.attDelete1);
        restDelete1 =(ImageButton) findViewById(R.id.restDelete1);

        imgBtnAddPlace = (ImageButton) findViewById(R.id.imgBtnAddPlace);
        btnNext = (Button) findViewById(R.id.btnNext);

        // PlacePlanner.java 에서 putExtra 로 담은 내용을 bundle 에 담는다.
        bundle = getIntent().getExtras();
        System.out.println("bundle size is : " + bundle.size());

        // 1일차, 2일차, ...
        Integer day = PlaceIntent.savedDateMap.get("startDay");
        textView.setText(Integer.toString(day) + "일차 활동 선택 내역");

        PlaceIntent.placeSavedMap = new LinkedHashMap<>();

        if (bundle != null) {
            final Set<String> keySet = bundle.keySet();   // intent 객체로 받아온 전체 keySet

            for (String s : keySet) {
                PlaceIntent.placeSavedMap.put(s, true);
            }

            for (String s : keySet) {
                // s값: att1_key, cafe1_key, rest1_key

                switch (s) {
                    case "att1_key":
                        att1.setVisibility(View.VISIBLE);
                        attDelete1.setVisibility(View.VISIBLE);
                        break;
                    case "rest1_key":
                        rest1.setVisibility(View.VISIBLE);
                        restDelete1.setVisibility(View.VISIBLE);
                        break;
                    case "cafe1_key":
                        cafe1.setVisibility(View.VISIBLE);
                        cafeDelete1.setVisibility(View.VISIBLE);
                        break;
                }
            }

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

            imgBtnAddPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /* test 코드 작성
                    Set<String> test = PlaceIntent.placeSavedMap.keySet();
                    System.out.println("====여기는 SelectedPlanner.java 위치 입니다====");
                    for (String s : test) {
                        boolean value = PlaceIntent.placeSavedMap.get(s);
                        System.out.println(s + "에 담긴 값은 " + value + " 입니다.");
                    } */

                    Intent imgBtnAddIntent = new Intent(SelectedPlanner.this, PlacePlanner.class);
                    startActivity(imgBtnAddIntent);
                    // finish();
                }
            });

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int startDay = PlaceIntent.savedDateMap.get("startDay");
                    int endDay = PlaceIntent.savedDateMap.get("endDay");

                    // 사용자가 게시물을 삭제한 뒤 '다음' 버튼을 눌렀을 때, 삭제된 내역이 반영되지 않는 오류를 수정
                    Set<String> checkRemovedSet = PlaceIntent.placeSavedMap.keySet();
                    for (String s : checkRemovedSet) {
                        if (keySet.contains(s) && !PlaceIntent.placeSavedMap.get(s)) {
                            keySet.remove(s);
                        }
                    }

                    // 사용자가 선택한 게시물이 하나도 없는 경우, 다음 화면으로 넘어가지 못하도록 방지
                    if (keySet.size() == 0) {
                        Toast.makeText(getApplicationContext(), "활동을 하나 이상 선택하세요!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 각 일차마다 저장된 값을 Place.savedPlacesMap 에 넣어준다.
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
                }
            });

        }
    }
}
