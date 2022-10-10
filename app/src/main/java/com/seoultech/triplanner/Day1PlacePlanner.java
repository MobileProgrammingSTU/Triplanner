package com.seoultech.triplanner;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class Day1PlacePlanner extends AppCompatActivity {

    LinearLayout imgLinearLayout;

    ImageView imgBtnBack;
    RelativeLayout att1, cafe1, rest1;
    ImageButton imgBtnAddPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_place_day1);

        imgBtnBack = (ImageView) findViewById(R.id.imgBtnBack);
        imgBtnAddPlace = (ImageButton) findViewById(R.id.imgBtnAddPlace);

        final Bundle bundle = getIntent().getExtras();

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        att1 = (RelativeLayout) findViewById(R.id.att1);
        cafe1 = (RelativeLayout) findViewById(R.id.cafe1);
        rest1 = (RelativeLayout) findViewById(R.id.rest1);

        if (bundle != null) {
            Set<String> keySet = bundle.keySet();   // intent 객체로 받아온 전체 keySet

            imgLinearLayout = (LinearLayout) findViewById(R.id.imgLinearLayout);

            for (String s : keySet) {
                // s값: att1Img, cafe1Img, rest1Img

                switch (s) {
                    case "att1":
                        att1.setVisibility(View.VISIBLE);
                        break;
                    case "rest1":
                        rest1.setVisibility(View.VISIBLE);
                        break;
                    case "cafe1":
                        cafe1.setVisibility(View.VISIBLE);
                        break;
                }
            }

            imgBtnAddPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }
    }
}
