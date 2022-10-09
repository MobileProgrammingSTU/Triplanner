package com.seoultech.triplanner;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;


public class PlacePlanner extends AppCompatActivity {

    ImageButton imgBtnBack;

    RelativeLayout att1, rest1, cafe1;
    Button btnAttraction, btnRestaurant, btnCafe;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_place);

        imgBtnBack = (ImageButton) findViewById(R.id.imgBtnBack);

        btnAttraction = (Button) findViewById(R.id.btnAtt);
        btnRestaurant = (Button) findViewById(R.id.btnRes);
        btnCafe = (Button) findViewById(R.id.btnCafe);

        att1 = (RelativeLayout) findViewById(R.id.att1);
        rest1 = (RelativeLayout) findViewById(R.id.rest1);
        cafe1 = (RelativeLayout) findViewById(R.id.cafe1);

        btnNext = (Button) findViewById(R.id.btnNext);

        // RelativeLayout 을 클릭 시 이미지들을 저장하도록 하는 Intent 객체를 생성
        // 여기서 final keyword 를 사용하지 않으면, 아래에서 오류 발생
        final Intent intent = new Intent(PlacePlanner.this, Day1PlacePlanner.class);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAttraction.setSelected(false);
        btnRestaurant.setSelected(false); btnCafe.setSelected(false);

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

        // 여기서, att1, rest1, cafe1 의 RelativeLayout 을 클릭할 때, intent 로 data 를 저장한다.
        att1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent.putExtra("att1", R.drawable.img_planner_place_attraction_1);
                //relativeLayoutList.add(att));

                intent.putExtra("att1Img", R.drawable.img_planner_place_attraction_1);
              //  intent.putExtra("att1Text", "명소 이름");
                startActivity(intent);
            }
        });
        rest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("rest1Img", R.drawable.img_planner_place_restaurant_1);
                //intent.putExtra("res1Text", "맛집 이름");
                startActivity(intent);

                // intent.putExtra("rest1", (Parcelable) relativeLayoutList);
            }
        });
        cafe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("cafe1Img", R.drawable.img_planner_place_cafe_1);
              //  intent.putExtra("cafe1Text", "카페 이름");
                startActivity(intent);
            }
        });

        /*
        one by one 으로 활동이 선택되기 때문에, 이 버튼은 굳이 필요가 없다 -> 추후 제거 예정
        // 클릭 시 다음 화면으로 이동
       btnNext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               System.out.println("main intent size: " + intent.toString());

               startActivity(intent);
           }
       });

         */
    }

}