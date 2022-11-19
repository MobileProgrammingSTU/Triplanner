package com.seoultech.triplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.seoultech.triplanner.Model.PlaceIntent;
import com.seoultech.triplanner.Model.PostItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class FinishPlanner extends AppCompatActivity {

    ImageButton FooterBtnHome;

    TextView textResult;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish);

        textResult = (TextView) findViewById(R.id.result);

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
            ArrayList<PostItem> list = PlaceIntent.savedPlacesMap.get(i);
            result += "\n\n" + i + "일차에 당신이 선택한 장소는 ";
            for (int j = 0; j < list.size(); j++) {
                System.out.println(i + "일차에 당신이 선택한 장소는 " + list.get(j).getTitle() + " 입니다!");
                result += "\n" + list.get(j).getTitle() + " 입니다!";
            }
        }
        textResult.setText(result);
    }
}
