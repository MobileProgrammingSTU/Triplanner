package com.seoultech.triplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;

    private Integer[] images = {
            R.drawable.img_activity_main_cafe_1, R.drawable.img_activity_main_cafe_1_bw
    };

    ImageView logo;
    ImageButton searchBtn;
    TextView HeaderText;
    RelativeLayout searchWindow;
    ImageButton regionA, regionA_down, regionB;
    TextView regionAText, regionBText;

    ImageButton saveBtn, bookmarkBtn;

    LinearLayout MainPost;

    ImageButton FooterBtnAddPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("홈화면");

        logo = (ImageView) findViewById(R.id.Logo);
        searchBtn = (ImageButton) findViewById(R.id.HeaderBtnSearch);
        searchWindow = (RelativeLayout) findViewById(R.id.BtnSearchWindow);
        HeaderText = (TextView) findViewById(R.id.HeaderText);
        regionA = findViewById(R.id.regionA);
        regionA_down = findViewById(R.id.regionA_down);
        regionB = findViewById(R.id.regionB);
        regionAText = (TextView) findViewById(R.id.regionAText);
        regionBText = (TextView) findViewById(R.id.regionBText);

        saveBtn = findViewById(R.id.MainBtnSave);
        bookmarkBtn = findViewById(R.id.MainBtnBookmark);

        sliderViewPager = findViewById(R.id.sliderViewPager);
        layoutIndicator = findViewById(R.id.layoutIndicators);
        sliderViewPager.setOffscreenPageLimit(3);
        sliderViewPager.setAdapter(new ImageSliderAdapter(this, images));
        sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
        setupIndicators(images.length);

        MainPost = (LinearLayout) findViewById(R.id.MainTextPost);

        FooterBtnAddPlan = (ImageButton) findViewById(R.id.FooterBtnAddPlan);

        //돋보기 버튼 클릭
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchBtn.isSelected()) {
                    searchWindow.setVisibility(View.GONE);
                    logo.setVisibility(android.view.View.VISIBLE);
                    HeaderText.setVisibility(android.view.View.GONE);
                }
                else {
                    searchWindow.setVisibility(android.view.View.VISIBLE);
                    logo.setVisibility(View.GONE);
                    HeaderText.setVisibility(android.view.View.VISIBLE);
                }
                searchBtn.setSelected(!searchBtn.isSelected());
            }
        });

        //게시물 클릭
        MainPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostMain.class);
                startActivity(intent);  // main_post(게시물글) 이동
            }
        });

        //게시물 플랜 저장 버튼 클릭
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveBtn.isSelected()) {
                    saveBtn.setImageResource(R.drawable.ic_save);
                }
                else {
                    saveBtn.setImageResource(R.drawable.ic_save_selected);
                    Toast.makeText(getApplicationContext(),"플랜을 보관함에 저장했습니다", Toast.LENGTH_SHORT).show();
                }
                saveBtn.setSelected(!saveBtn.isSelected());
            }
        });
        //북마크 버튼 클릭
        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookmarkBtn.isSelected()) {
                    bookmarkBtn.setImageResource(R.drawable.ic_bookmark);
                }
                else {
                    bookmarkBtn.setImageResource(R.drawable.ic_bookmark_selected);
                    Toast.makeText(getApplicationContext(),"게시물을 보관함에 저장했습니다", Toast.LENGTH_SHORT).show();
                }
                bookmarkBtn.setSelected(!bookmarkBtn.isSelected());
            }
        });

        //플러스 버튼 클릭
        FooterBtnAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegionPlanner.class);
                startActivity(intent);  // Activity 이동
            }
        });

        //필터에서 북부 지역 선택
        regionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regionA.isSelected()) {
                    regionA.setImageResource(R.drawable.img_region_a_up);
                    regionA.setScaleX(1);
                    regionA.setScaleY(1);
                    regionA_down.setImageResource(R.drawable.img_region_a_down);
                    regionA_down.setScaleX(1);
                    regionA_down.setScaleY(1);
                    regionA_down.setTranslationY(0);
                    regionAText.setVisibility(View.GONE);
                }
                else {
                    regionA.setImageResource(R.drawable.img_region_a_up_selected);
                    regionA.setScaleX((float) 1.2);
                    regionA.setScaleY((float) 1.2);
                    regionA_down.setImageResource(R.drawable.img_region_a_down_selected);
                    regionA_down.setScaleX((float) 1.2);
                    regionA_down.setScaleY((float) 1.2);
                    regionA_down.setTranslationY((float) 47.35);
                    regionAText.setVisibility(android.view.View.VISIBLE);
                }
                regionA.setSelected(!regionA.isSelected());
            }
        });
        regionA_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regionA_down.isSelected()) {
                    regionA.callOnClick();
                }
                else {
                    regionA.callOnClick();
                }
                regionA_down.setSelected(!regionA_down.isSelected());
            }
        });
        //필터에서 남부 지역 선택
        regionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regionB.isSelected()) {
                    regionB.setImageResource(R.drawable.img_region_b);
                    regionB.setScaleX(1);
                    regionB.setScaleY(1);
                    regionBText.setVisibility(View.GONE);
                }
                else {
                    regionB.setImageResource(R.drawable.img_region_b_selected);
                    regionB.setScaleX((float) 1.2);
                    regionB.setScaleY((float) 1.2);
                    regionBText.setVisibility(android.view.View.VISIBLE);
                }
                regionB.setSelected(!regionB.isSelected());
            }
        });
    }

    //게시물 슬라이드 인디케이터(점) 생성
    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.activity_main_layoutindicators_inactive));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }
    //현재 게시물 슬라이드 인디케이터(점)
    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.activity_main_layoutindicators_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.activity_main_layoutindicators_inactive
                ));
            }
        }
    }

}
