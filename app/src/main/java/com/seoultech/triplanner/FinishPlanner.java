package com.seoultech.triplanner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.seoultech.triplanner.PlaceIntent;

import java.util.LinkedList;
import java.util.Set;

public class FinishPlanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish);

        Set<Integer> keySet = PlaceIntent.savedPlacesMap.keySet();
        for (int i : keySet) {
            LinkedList<String> list = PlaceIntent.savedPlacesMap.get(i);
            for (int j = 0; j < list.size(); j++) {
                System.out.println(i + "일차에 당신이 선택한 장소는 " + list.get(j) + " 입니다!");
            }
        }
    }
}
