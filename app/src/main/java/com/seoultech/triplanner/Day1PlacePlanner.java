package com.seoultech.triplanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class Day1PlacePlanner extends AppCompatActivity {

    ImageView imgBtnBack;
    int imageValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_place_day1);

        imgBtnBack = (ImageView) findViewById(R.id.imgBtnBack);

        final Bundle bundle = getIntent().getExtras();
        //System.out.println(bundle.size());  // bundle의 사이즈에 맞도록 imageView를 생성하자.

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
               // Intent intent = new Intent(Day1PlacePlanner.this, PlacePlanner.class);
               // startActivity(intent);
            }
        });

        if (bundle != null) {
            Set<String> keySet = bundle.keySet();

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.imgLinearLayout);

            for (String s : keySet) {
                // s값: att1, cafe1, rest1
                ImageView imgView = new ImageView(this);
                imageValue = bundle.getInt(s);
                imgView.setImageResource(imageValue);
                imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                300);
//                            LinearLayout.LayoutParams.MATCH_PARENT /* layout_width */,
//                            height /* layout_height */);
                layoutParams.setMargins(20, 20, 20, 20);
                imgView.setLayoutParams(layoutParams);
                linearLayout.addView(imgView);
            }
        }
    }
}
