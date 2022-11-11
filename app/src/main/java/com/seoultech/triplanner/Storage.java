package com.seoultech.triplanner;

import static com.seoultech.triplanner.Fragment.StorageFragmentAdapter.PAGE_POSITION;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.seoultech.triplanner.Fragment.StorageFragmentAdapter;

@SuppressWarnings("deprecation")
public class Storage extends AppCompatActivity {
    TabLayout tabs;
    ViewPager viewPager;

    ImageButton btnFooterHome, btnFooterAddPlan, btnFooterStorage, btnFooterMyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage);
        setTitle("저장소");

        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        btnFooterHome = (ImageButton) findViewById(R.id.FooterBtnHome);
        btnFooterAddPlan = (ImageButton) findViewById(R.id.FooterBtnAddPlan);
        btnFooterStorage = (ImageButton) findViewById(R.id.FooterBtnStorage);
        btnFooterMyPage = (ImageButton) findViewById(R.id.FooterBtnMyPage);

        //ViewPager 연결
        setViewPager();

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

    private void setViewPager() {
        //어댑터 초기화
        StorageFragmentAdapter adapter = new StorageFragmentAdapter(getSupportFragmentManager(), PAGE_POSITION);
        //어댑터 연결
        viewPager.setAdapter(adapter);
        //페이지 리스너 (viewPager와 TabLayout의 페이지를 맞춰줌)
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        //탭 선택 리스너 (탭 행동 설정)
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //선택된 탭일 때
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //선택된 탭과 연결된 fragment를 가져옴
                viewPager.setCurrentItem(tab.getPosition());
                //아이콘 색상을 흰색으로 설정
                //tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }
            //선택되지 않은 탭일 때
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //아이콘 색상을 #0070C0 으로 설정
                //tab.getIcon().setColorFilter(Color.parseColor("#0070C0"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
