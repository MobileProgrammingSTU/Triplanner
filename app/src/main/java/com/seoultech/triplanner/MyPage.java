package com.seoultech.triplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MyPage extends AppCompatActivity {
    ImageButton btnFooterHome, btnFooterAddPlan, btnFooterStorage, btnFooterMyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);
        setTitle("마이페이지");

        btnFooterHome = (ImageButton) findViewById(R.id.FooterBtnHome);
        btnFooterAddPlan = (ImageButton) findViewById(R.id.FooterBtnAddPlan);
        btnFooterStorage = (ImageButton) findViewById(R.id.FooterBtnStorage);
        btnFooterMyPage = (ImageButton) findViewById(R.id.FooterBtnMyPage);

        //홈버튼
        btnFooterHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPage.this, MainActivity.class);
                startActivity(intent);  // Activity 이동
            }
        });

        //플랜버튼
        btnFooterAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPage.this, RegionPlanner.class);
                startActivity(intent);  // Activity 이동
            }
        });

        //저장소버튼
        btnFooterStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPage.this, Storage.class);
                startActivity(intent);  // Activity 이동
            }
        });

        //마이페이지버튼
        btnFooterMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }
}
