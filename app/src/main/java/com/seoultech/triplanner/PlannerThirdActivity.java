package com.seoultech.triplanner;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PlannerThirdActivity extends AppCompatActivity {

    LinearLayout linearAttraction, linearRestaurant, linearCafe;

    Toolbar toolBar;

    ImageView cafeView1;
    TextView cafeText1, cafeTag1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_third);

        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar (toolBar);                  //액티비티의 앱바(App Bar)로 지정
        ActionBar actionBar = getSupportActionBar ();   //앱바 제어를 위해 툴바 액세스
        actionBar.setDisplayHomeAsUpEnabled (true);     // 앱바에 뒤로가기 버튼 만들기
        actionBar.setTitle("플랜 추가");

        linearAttraction = (LinearLayout) findViewById(R.id.linearAttraction);
        linearRestaurant = (LinearLayout) findViewById(R.id.linearRestaurant);
        linearCafe = (LinearLayout) findViewById(R.id.linearCafe);
        cafeView1 = (ImageView) findViewById(R.id.cafeView1);
        cafeText1 = (TextView) findViewById(R.id.cafeText1);
        cafeTag1 = (TextView) findViewById(R.id.cafeTag1);

        linearAttraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cafeView1.setVisibility(View.INVISIBLE);
                cafeText1.setVisibility(View.INVISIBLE);
                cafeTag1.setVisibility(View.INVISIBLE);
            }
        });
        linearRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cafeView1.setVisibility(View.INVISIBLE);
                cafeText1.setVisibility(View.INVISIBLE);
                cafeTag1.setVisibility(View.INVISIBLE);
            }
        });
        linearCafe.setOnClickListener(new View.OnClickListener() {
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