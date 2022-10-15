package com.seoultech.triplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class FinishPlanner extends AppCompatActivity {

    ImageButton FooterBtnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish);

        FooterBtnHome = (ImageButton) findViewById(R.id.FooterBtnHome);
        FooterBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 메인 화면으로 가기 전, 아래 내역들 초기화 필요
                PlaceIntent.savedDateMap = new LinkedHashMap<>();
                PlaceIntent.placeIntent = new Intent();
                PlaceIntent.savedPlacesMap = new LinkedHashMap<>();

                Intent intentHome = new Intent(FinishPlanner.this, MainActivity.class);
                startActivity(intentHome);
            }
        });

        Set<Integer> keySet = PlaceIntent.savedPlacesMap.keySet();
        for (int i : keySet) {
            ArrayList<PlaceBannerItem> list = PlaceIntent.savedPlacesMap.get(i);
            for (int j = 0; j < list.size(); j++) {
                System.out.println(i + "일차에 당신이 선택한 장소는 " + list.get(j).getTitle() + " 입니다!");
            }
        }
    }
}
