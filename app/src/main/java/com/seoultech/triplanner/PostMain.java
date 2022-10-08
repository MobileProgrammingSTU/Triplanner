package com.seoultech.triplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

public class PostMain extends AppCompatActivity {
    ImageButton btnBack;

    ImageButton btnSave, btnBookmark;

    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;

    private Integer[] images = {
            R.drawable.img_activity_main_cafe_1, R.drawable.img_activity_main_cafe_1_bw
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_post);

        btnBack = findViewById(R.id.imgBtnBack);

        btnSave = findViewById(R.id.postBtnSave);
        btnBookmark = findViewById(R.id.postBtnBookmark);

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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostMain.this, MainActivity.class);
                startActivity(intent); // 홈으로 이동
            }
        });

        //게시물 플랜 저장 버튼 클릭
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnSave.isSelected()) {
                    btnSave.setImageResource(R.drawable.ic_save);
                }
                else {
                    btnSave.setImageResource(R.drawable.ic_save_selected);
                    Toast.makeText(getApplicationContext(),"플랜을 보관함에 저장했습니다", Toast.LENGTH_SHORT).show();
                }
                btnSave.setSelected(!btnSave.isSelected());
            }
        });
        //북마크 버튼 클릭
        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnBookmark.isSelected()) {
                    btnBookmark.setImageResource(R.drawable.ic_bookmark);
                }
                else {
                    btnBookmark.setImageResource(R.drawable.ic_bookmark_selected);
                    Toast.makeText(getApplicationContext(),"게시물을 보관함에 저장했습니다", Toast.LENGTH_SHORT).show();
                }
                btnBookmark.setSelected(!btnBookmark.isSelected());
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
