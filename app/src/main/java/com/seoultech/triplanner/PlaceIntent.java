package com.seoultech.triplanner;

import android.content.Intent;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlaceIntent {

    // static 변수로 지정해서, 맨 처음에 생성되도록
    static Intent placeIntent = new Intent();

    // 첫 번째 String 에는 Place 가, 두 번째 String 에는 day1, day2, ... 등과 같이 날짜 정보를 담는다.
    static Map<String, String> savedPlacesMap = new LinkedHashMap<>();

    PlaceIntent() {

    }
}
