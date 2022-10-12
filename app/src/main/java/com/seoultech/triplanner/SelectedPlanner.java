package com.seoultech.triplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

public class SelectedPlanner extends AppCompatActivity {

    ImageView imgBtnBack;

    TextView textView;
    RelativeLayout att1, cafe1, rest1;
    Button cafeDelete1, attDelete1, restDelete1;
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

        cafeDelete1 = (Button) findViewById(R.id.cafeDelete1);
        attDelete1 = (Button) findViewById(R.id.attDelete1);
        restDelete1 =(Button) findViewById(R.id.restDelete1);

        imgBtnAddPlace = (ImageButton) findViewById(R.id.imgBtnAddPlace);
        btnNext = (Button) findViewById(R.id.btnNext);

        // PlacePlanner.java 에서 putExtra 로 담은 내용을 bundle 에 담는다.
        bundle = getIntent().getExtras();
        System.out.println("bundle size is : " + bundle.size());

        // 1일차, 2일차, ...
        Integer day = PlaceIntent.savedDateMap.get("startDay");
        textView.setText(Integer.toString(day) + "일차 활동 선택 내역");

        PlaceIntent.removedPlaceList = new LinkedHashMap<>();

        if (bundle != null) {
            final Set<String> keySet = bundle.keySet();   // intent 객체로 받아온 전체 keySet

            for (String s : keySet) {
                PlaceIntent.removedPlaceList.put(s, true);
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
                    PlaceIntent.removedPlaceList.put("cafe1_key", false);
                }
            });
            attDelete1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    att1.setVisibility(View.GONE);

                    // 여기서 bundle 객체의 데이터를 제거한다.
                    PlaceIntent.removedPlaceList.put("att1_key", false);
                }
            });
            restDelete1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rest1.setVisibility(View.GONE);

                    // 여기서 bundle 객체의 데이터를 제거한다.
                    PlaceIntent.removedPlaceList.put("rest1_key", false);
                }
            });

            imgBtnAddPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int startDay = PlaceIntent.savedDateMap.get("startDay");
                    int endDay = PlaceIntent.savedDateMap.get("endDay");

                    // 각 일차마다 저장된 값을 Place.savedPlacesMap 에 넣어준다.
                    LinkedList<String> list = new LinkedList<>();
                    for (String s : keySet) {
                        list.add(s);
                    }
                    PlaceIntent.savedPlacesMap.put(startDay, list);

                    if (startDay < endDay) {
                        // 여기서 PlacePlanner 의 날짜 값을 +1 증가시킴
                        PlaceIntent.savedDateMap.put("startDay", startDay + 1);

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
