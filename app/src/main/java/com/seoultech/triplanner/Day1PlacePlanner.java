package com.seoultech.triplanner;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class Day1PlacePlanner extends AppCompatActivity {

    ImageView imgBtnBack;
    ImageButton btnAdd;
    int imageValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_place_day1);

        imgBtnBack = (ImageView) findViewById(R.id.imgBtnBack);
        btnAdd = (ImageButton) findViewById(R.id.btnAddPlace);

        final Bundle bundle = getIntent().getExtras();
        //System.out.println(bundle.size());  // bundle의 사이즈에 맞도록 imageView를 생성하자.

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
               //Intent intent = new Intent(Day1PlacePlanner.this, PlacePlanner.class);
               //startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (bundle != null) {
            Set<String> keySet = bundle.keySet();   // intent 객체로 받아온 전체 keySet
           // Set<String> imgSet = new LinkedHashSet<>(); // intent 객체 중 img 객체들
           // Set<String> textViewSet = new LinkedHashSet<>(); // intent 객체 중 textView 들

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.imgLinearLayout);

            /*
            for (String s : keySet) {
                System.out.println("s is : " + s);
                if (s.contains("Img")) {
                    imgSet.add(String.valueOf(bundle.get(s)));
                    System.out.println("Img contains is " + s);
                }
                else {
                    textViewSet.add(String.valueOf(bundle.get(s)));
                    System.out.println("Text contains is " + s);
                    //System.out.println(textViewSet.toString() + " toString()");
                }
            } */

            for (String s : keySet) {
                // s값: att1Img, cafe1Img, rest1Img
                ImageView imgView = new ImageView(this);
                imageValue = bundle.getInt(s);
                imgView.setImageResource(imageValue);
                imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,300);
                layoutParams.setMargins(0, 20, 0, 20);
                imgView.setLayoutParams(layoutParams);
                linearLayout.addView(imgView);


            }
//            for (String s : textViewSet) {
//                // s값: att1Text, cafe1Text, rest1Text
//                TextView textView = new TextView(this);
//                textView.setText(bundle.getString(s));
//                textView.setTextSize(50);
//                textView.setTextColor(Color.WHITE);
//                linearLayout.addView(textView);
//            }

            /*
            Button btnPlus = new Button(this);
            btnPlus.setWidth(200);
            btnPlus.setHeight(200);
            btnPlus.setText("+");
            btnPlus.setTextSize(40);
            btnPlus.setTextColor(Color.WHITE);
            btnPlus.setBackground(getDrawable(R.drawable.btn_background_click_effect));
            linearLayout.addView(btnPlus);

            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
*/
        }
    }
}
