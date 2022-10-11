package com.seoultech.triplanner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Day1PlacePlanner extends AppCompatActivity {

    LinearLayout imgLinearLayout;

    ImageView imgBtnBack;
    RelativeLayout att1, cafe1, rest1;
    Button cafeDelete1, attDelete1, restDelete1;
    ImageButton imgBtnAddPlace;

    Bundle bundle;

    static Map<String, Boolean> removedList = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_place_day1);

        imgBtnBack = (ImageView) findViewById(R.id.imgBtnBack);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bundle = getIntent().getExtras();
        System.out.println("bundle size is : " + bundle.size());

        att1 = (RelativeLayout) findViewById(R.id.att1);
        cafe1 = (RelativeLayout) findViewById(R.id.cafe1);
        rest1 = (RelativeLayout) findViewById(R.id.rest1);

        cafeDelete1 = (Button) findViewById(R.id.cafeDelete1);
        attDelete1 = (Button) findViewById(R.id.attDelete1);
        restDelete1 =(Button) findViewById(R.id.restDelete1);

        imgBtnAddPlace = (ImageButton) findViewById(R.id.imgBtnAddPlace);

        if (bundle != null) {
            final Set<String> keySet = bundle.keySet();   // intent 객체로 받아온 전체 keySet

            for (String s : keySet) {
                removedList.put(s, true);
            }

            imgLinearLayout = (LinearLayout) findViewById(R.id.imgLinearLayout);

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
                    removedList.put("cafe1_key", false);
//                    getIntent().removeExtra("cafe1");
//                    bundle.remove("cafe1");
                }
            });
            attDelete1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    att1.setVisibility(View.GONE);

                    // 여기서 bundle 객체의 데이터를 제거한다.
                    removedList.put("att1_key", false);
                }
            });
            restDelete1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rest1.setVisibility(View.GONE);

                    // 여기서 bundle 객체의 데이터를 제거한다.
                    removedList.put("rest1_key", false);
                }
            });

            imgBtnAddPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }
    }
}
