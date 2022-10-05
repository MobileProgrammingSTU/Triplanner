package com.seoultech.triplanner;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PlannerThirdActivity extends AppCompatActivity {

    ImageView cafeView1;
    TextView cafeText1, cafeTag1;
    ImageView btnAttraction, btnRestaurant, btnCafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_third);

        btnAttraction = (ImageView) findViewById(R.id.imageAtt);
        btnRestaurant = (ImageView) findViewById(R.id.imageRes);
        btnCafe = (ImageView) findViewById(R.id.imageCafe);
        cafeView1 = (ImageView) findViewById(R.id.cafeView1);
        cafeText1 = (TextView) findViewById(R.id.cafeText1);
        cafeTag1 = (TextView) findViewById(R.id.cafeTag1);

        btnAttraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnAttraction.isSelected()){
                    btnAttraction.clearColorFilter();
                }
                else {
                    btnAttraction.setColorFilter(Color.parseColor("#FFD37A"), PorterDuff.Mode.SRC_IN);

                    cafeView1.setVisibility(View.GONE);
                    cafeText1.setVisibility(View.GONE);
                    cafeTag1.setVisibility(View.GONE);
                }
                btnAttraction.setSelected(!btnAttraction.isSelected());
            }
        });
        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnRestaurant.isSelected()){
                    btnRestaurant.clearColorFilter();
                }
                else {
                    btnRestaurant.setColorFilter(Color.parseColor("#F26C73"), PorterDuff.Mode.SRC_IN);

                    cafeView1.setVisibility(View.GONE);
                    cafeText1.setVisibility(View.GONE);
                    cafeTag1.setVisibility(View.GONE);
                }
                btnRestaurant.setSelected(!btnRestaurant.isSelected());
            }
        });
        btnCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnCafe.isSelected()){
                    btnCafe.clearColorFilter();

                    cafeView1.setVisibility(View.GONE);
                    cafeText1.setVisibility(View.GONE);
                    cafeTag1.setVisibility(View.GONE);
                }
                else {
                    btnCafe.setColorFilter(Color.parseColor("#6CE4B4"), PorterDuff.Mode.SRC_IN);

                    cafeView1.setVisibility(View.VISIBLE);
                    cafeText1.setVisibility(View.VISIBLE);
                    cafeTag1.setVisibility(View.VISIBLE);
                }
                btnCafe.setSelected(!btnCafe.isSelected());
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home: //  툴바 뒤로가기버튼 눌렸을 때 동작.
                finish ();          // 현재는 종료되도록 설정
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }
    }

}