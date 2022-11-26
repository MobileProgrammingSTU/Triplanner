package com.seoultech.triplanner.Model;

import android.content.Intent;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlaceIntent {

    // DatePlanner.java 에서 시작 날짜와 종료 날짜를 입력받기 위해 구현
    public static Map<String, Integer> savedDateMap = new LinkedHashMap<>();
    // 날(int : day)말고 날짜(LocalDate) 정보
    public static Map<String, LocalDate> savedDates = new LinkedHashMap<String, org.threeten.bp.LocalDate>();

    // static 변수로 지정해서, 맨 처음에 생성되도록
    public static Intent placeIntent = new Intent();

    //새로 추가됨 : 일차에 선택한 장소배너 아이템 리스트
    public static ArrayList<PostItem> daySelectedPlace = new ArrayList<PostItem>();

    // 1일차에 해당하는 장소를 담는 Map(= Python의 Dict)
    // Key 값은 중복이 될 수 없으므로, 장소를 String[] 배열로 받도록 선언(장소를 여러 개 선택 할 수도 있으므로)
    //List<String>에서 ArrayList<PlaceBannerItem>으로 변경
    // 22.11.26 변경 사항 : HashMap<Integer, ArrayList<PostItem>> -> HashMap<String, ArrayList<PostItem>> 변경
    // 이유 : DB의 키로 String 만 가능하기 때문, 따라서 finish 페이지 출력 불가
    public static HashMap<String, ArrayList<PostItem>> savedPlacesMap = new LinkedHashMap<>();

    // 모든 PlaceBannerItem 데이터 타입은 --> PostItem 으로 변경되었습니다

    // 장소 데이터
    public static String intentRegionType;

    PlaceIntent() {

    }
}
