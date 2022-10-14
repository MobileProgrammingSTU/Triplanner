package com.seoultech.triplanner;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class PlacePlanner extends AppCompatActivity {

    ImageButton imgBtnBack;

    TextView textView;
    RelativeLayout att1, rest1, cafe1;
    Button btnAttraction, btnRestaurant, btnCafe;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_place);

        imgBtnBack = (ImageButton) findViewById(R.id.imgBtnBack);

        textView = (TextView) findViewById(R.id.textView);
        btnAttraction = (Button) findViewById(R.id.btnAtt);
        btnRestaurant = (Button) findViewById(R.id.btnRes);
        btnCafe = (Button) findViewById(R.id.btnCafe);

        att1 = (RelativeLayout) findViewById(R.id.att1);
        rest1 = (RelativeLayout) findViewById(R.id.rest1);
        cafe1 = (RelativeLayout) findViewById(R.id.cafe1);

        btnNext = (Button) findViewById(R.id.btnNext);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAttraction.setSelected(false);
        btnRestaurant.setSelected(false);
        btnCafe.setSelected(false);

        btnAttraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnAttraction.isSelected()){
                    btnAttraction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#404040")));
                    att1.setVisibility(View.GONE);
                }
                else {
                    btnAttraction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFD37A")));
                    att1.setVisibility(View.VISIBLE);
                }
                btnAttraction.setSelected(!btnAttraction.isSelected());
            }
        });
        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnRestaurant.isSelected()){
                    btnRestaurant.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#404040")));
                    rest1.setVisibility(View.GONE);
                }
                else {
                    btnRestaurant.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F26C73")));
                    rest1.setVisibility(View.VISIBLE);
                }
                btnRestaurant.setSelected(!btnRestaurant.isSelected());
            }
        });
        btnCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnCafe.isSelected()){
                    btnCafe.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#404040")));
                    cafe1.setVisibility(View.GONE);
                }
                else {
                    btnCafe.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6CE4B4")));
                    cafe1.setVisibility(View.VISIBLE);
                }
                btnCafe.setSelected(!btnCafe.isSelected());
            }
        });

        Set<String> removedListKeySet = null;
        if (PlaceIntent.placeSavedMap != null) {
            removedListKeySet = PlaceIntent.placeSavedMap.keySet();
        }

        // 이 부분은 SelectedPlanner class 의 imgBtnAddPlace 가 finish() 가 아닌 intent 객체로 이동하기 때문에 실행된다.
        // finish() 를 사용하게 되면 아래 해당되는 부분이 읽어지지 않아서 삭제한 활동이 반영되지 않는다.
        if (removedListKeySet != null) {
            for (String s : removedListKeySet) {
                if (!PlaceIntent.placeSavedMap.get(s)) {
                    PlaceIntent.placeIntent.removeExtra(s);
                }
            }
        }

        // RelativeLayout 을 클릭 시 이미지들을 저장하도록 하는 Intent 객체를 불러온다.
        PlaceIntent.placeIntent.setClass(PlacePlanner.this, SelectedPlanner.class);

        // 1일차, 2일차, ...
        Integer day = PlaceIntent.savedDateMap.get("startDay");
        textView.setText(Integer.toString(day) + "일차 활동을 선택하세요");

        // 여기서, att1, rest1, cafe1 의 RelativeLayout 을 클릭할 때, intent 로 data 를 저장한다.
        // putExtra 에서 첫 번째 변수는 임의로 지정한 문자열, 두 번째 변수는 RelativeLayout 의 id와 동일한 문자열.
        att1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceIntent.placeIntent.putExtra("att1_key", "att1");
                startActivity(PlaceIntent.placeIntent);
            }
        });
        rest1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PlaceIntent.placeIntent.putExtra("rest1_key", "rest1");
                startActivity(PlaceIntent.placeIntent);
            }
        });
        cafe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceIntent.placeIntent.putExtra("cafe1_key", "cafe1");
                startActivity(PlaceIntent.placeIntent);
            }
        });
    }

}