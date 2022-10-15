package com.seoultech.triplanner;

import android.content.Intent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlaceIntent {

    // DatePlanner.java 에서 시작 날짜와 종료 날짜를 입력받기 위해 구현
    static Map<String, Integer> savedDateMap = new LinkedHashMap<>();

    // static 변수로 지정해서, 맨 처음에 생성되도록
    static Intent placeIntent = new Intent();

    // 1일차, 2일차마다 활동 담긴 부분을 초기화해주기 위해 사용
    static Map<String, Boolean> placeSavedMap;

    //일차에 선택한 장소배너 아이템 리스트
    static ArrayList<PlaceBannerItem> daySelectedPlace = new ArrayList<PlaceBannerItem>();

    // 1일차에 해당하는 장소를 담는 Map(= Python의 Dict)
    // Key 값은 중복이 될 수 없으므로, 장소를 String[] 배열로 받도록 선언(장소를 여러 개 선택 할 수도 있으므로)
    static Map<Integer, ArrayList<PlaceBannerItem>> savedPlacesMap = new LinkedHashMap<>();

    PlaceIntent() {

    }
}
