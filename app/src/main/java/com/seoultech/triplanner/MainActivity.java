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
            R.drawable.cafe_1, R.drawable.cafe_1_bw
    };

    ImageView logo;
    ImageButton searchBtn;
    RelativeLayout searchWindow;
    ImageView regionA;
    ImageView regionA_down;
    TextView regionAText;
    Boolean regionAChecked = false;
    ImageView regionB;
    TextView regionBText;

    ImageButton saveBtn;
    ImageButton bookmarkBtn;
    ImageButton FooterBtnAddPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("홈화면");

        logo = (ImageView) findViewById(R.id.Logo);
        searchBtn = (ImageButton) findViewById(R.id.HeaderBtnSearch);
        searchWindow = (RelativeLayout) findViewById(R.id.BtnSearchWindow);
        regionA = findViewById(R.id.regionA);
        regionA_down = findViewById(R.id.regionA_down);
        regionAText = findViewById(R.id.regionAText);
        regionB = findViewById(R.id.regionB);
        regionBText = findViewById(R.id.regionBText);

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

        FooterBtnAddPlan = (ImageButton) findViewById(R.id.FooterBtnAddPlan);

        //돋보기 버튼 클릭
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchBtn.isSelected()) {
                    searchWindow.setVisibility(View.GONE);
                    logo.setVisibility(android.view.View.VISIBLE);
                }
                else {
                    searchWindow.setVisibility(android.view.View.VISIBLE);
                    logo.setVisibility(View.GONE);
                }
                searchBtn.setSelected(!searchBtn.isSelected());
            }
        });

        //게시물 플랜 저장 버튼 클릭
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveBtn.isSelected()) {
                    saveBtn.setImageResource(R.drawable.save);
                }
                else {
                    saveBtn.setImageResource(R.drawable.save_selected);
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
                    bookmarkBtn.setImageResource(R.drawable.bookmark);
                }
                else {
                    bookmarkBtn.setImageResource(R.drawable.bookmark_selected);
                    Toast.makeText(getApplicationContext(),"게시물을 보관함에 저장했습니다", Toast.LENGTH_SHORT).show();
                }
                bookmarkBtn.setSelected(!bookmarkBtn.isSelected());
            }
        });

        FooterBtnAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DatePlanner.class);
                startActivity(intent);  // Activity 이동
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
                    R.drawable.bg_indicator_inactive));
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
                        R.drawable.bg_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                ));
            }
        }
    }

    //필터에서 북부 지역 선택
    public void regionAClicked(View view) {
        if(regionAChecked) {
            regionAChecked = false;

            regionA.setImageResource(R.drawable.region_a_up);
            regionA.setScaleX(1);
            regionA.setScaleY(1);
            regionA_down.setImageResource(R.drawable.region_a_down);
            regionA_down.setScaleX(1);
            regionA_down.setScaleY(1);
            regionA_down.setTranslationY(0);
            regionAText.setVisibility(View.GONE);
        }
        else {
            regionAChecked = true;

            regionA.setImageResource(R.drawable.region_a_up_selected);
            regionA.setScaleX((float) 1.2);
            regionA.setScaleY((float) 1.2);
            regionA_down.setImageResource(R.drawable.region_a_down_selected);
            regionA_down.setScaleX((float) 1.2);
            regionA_down.setScaleY((float) 1.2);
            regionA_down.setTranslationY((float) 47.35);
            regionAText.setVisibility(android.view.View.VISIBLE);
        }
    }
    //필터에서 남부 지역 선택
    public void regionBClicked(View view) {
        if(regionB.isSelected()) {
            regionB.setImageResource(R.drawable.region_b);
            regionB.setScaleX(1);
            regionB.setScaleY(1);
            regionBText.setVisibility(View.GONE);
        }
        else {
            regionB.setImageResource(R.drawable.region_b_selected);
            regionB.setScaleX((float) 1.2);
            regionB.setScaleY((float) 1.2);
            regionBText.setVisibility(android.view.View.VISIBLE);
        }
        regionB.setSelected(!regionB.isSelected());
    }

}
