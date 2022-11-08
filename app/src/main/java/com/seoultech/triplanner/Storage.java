package com.seoultech.triplanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

@SuppressWarnings("deprecation")
public class Storage extends AppCompatActivity {
    ImageButton btnFooterHome, btnFooterAddPlan, btnFooterStorage, btnFooterMyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage);
        setTitle("저장소");

        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        btnFooterHome = (ImageButton) findViewById(R.id.FooterBtnHome);
        btnFooterAddPlan = (ImageButton) findViewById(R.id.FooterBtnAddPlan);
        btnFooterStorage = (ImageButton) findViewById(R.id.FooterBtnStorage);
        btnFooterMyPage = (ImageButton) findViewById(R.id.FooterBtnMyPage);

        TabHost.TabSpec tabSpecMypost = tabHost.newTabSpec("MYPOST").setIndicator("내 게시물");
        tabSpecMypost.setContent(R.id.tabMypost);
        tabHost.addTab(tabSpecMypost);

        TabHost.TabSpec tabSpecMyplan = tabHost.newTabSpec("MYPLAN").setIndicator("내 플랜");
        tabSpecMyplan.setContent(R.id.tabMyplan);
        tabHost.addTab(tabSpecMyplan);

        TabHost.TabSpec tabSpecLikes = tabHost.newTabSpec("LIKES").setIndicator("좋아요한 게시물");
        tabSpecLikes.setContent(R.id.tabLikes);
        tabHost.addTab(tabSpecLikes);

        tabHost.setCurrentTab(0);

        //홈버튼
        btnFooterHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Storage.this, MainActivity.class);
                startActivity(intent);  // Activity 이동
            }
        });

        //플랜버튼
        btnFooterAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Storage.this, RegionPlanner.class);
                startActivity(intent);  // Activity 이동
            }
        });

        //저장소버튼
        btnFooterStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //마이페이지버튼
        btnFooterMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Storage.this, MyPage.class);
                startActivity(intent);  // Activity 이동
            }
        });

    }
}
