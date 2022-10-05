package com.seoultech.triplanner;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PlannerThirdActivity extends AppCompatActivity {

    Toolbar toolBar;

    ImageView cafeView1;
    TextView cafeText1, cafeTag1;
    ImageButton btnAttraction;
    ImageButton btnRestaurant;
    ImageButton btnCafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_third);

        btnAttraction = (ImageButton) findViewById(R.id.imageAtt);
        btnRestaurant = (ImageButton) findViewById(R.id.imageRes);
        btnCafe = (ImageButton) findViewById(R.id.imageCafe);
        cafeView1 = (ImageView) findViewById(R.id.cafeView1);
        cafeText1 = (TextView) findViewById(R.id.cafeText1);
        cafeTag1 = (TextView) findViewById(R.id.cafeTag1);

        btnAttraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cafeView1.setVisibility(View.INVISIBLE);
                cafeText1.setVisibility(View.INVISIBLE);
                cafeTag1.setVisibility(View.INVISIBLE);
            }
        });
        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cafeView1.setVisibility(View.INVISIBLE);
                cafeText1.setVisibility(View.INVISIBLE);
                cafeTag1.setVisibility(View.INVISIBLE);
            }
        });
        btnCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cafeView1.setVisibility(View.VISIBLE);
                cafeText1.setVisibility(View.VISIBLE);
                cafeTag1.setVisibility(View.VISIBLE);
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