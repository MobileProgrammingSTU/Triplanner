package com.seoultech.triplanner;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlacePlanner extends AppCompatActivity {

    ListView bannerListView;
    PlaceBannerAdapter adapter;
    ArrayList<PlaceBannerItem> placeData; // 장소 data 리스트

    ImageButton imgBtnBack;

    TextView textView;
    RelativeLayout att1, rest1, cafe1;
    Button btnAttraction, btnRestaurant, btnCafe;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_place);

        bannerListView = (ListView) findViewById(R.id.bannerList);
        placeData = new ArrayList<>();

        //어댑터에 배너 기본 형식과 장소 data 리스트 적용
        adapter = new PlaceBannerAdapter(this, R.layout.place_banner_item, placeData);
        //리스트뷰에 어댑터 적용
        bannerListView.setAdapter(adapter);

        //placeData에 장소 data 입력
        adapter.addItem(R.drawable.img_planner_place_restaurant_1, "맛집 이름", "rest");
        adapter.addItem(R.drawable.img_planner_place_cafe_1, "카페 이름", "cafe");
        adapter.addItem(R.drawable.img_planner_place_attraction_1, "명소 이름", "att");

        imgBtnBack = (ImageButton) findViewById(R.id.imgBtnBack);

        textView = (TextView) findViewById(R.id.textView);
        btnAttraction = (Button) findViewById(R.id.btnAtt);
        btnRestaurant = (Button) findViewById(R.id.btnRes);
        btnCafe = (Button) findViewById(R.id.btnCafe);

        btnNext = (Button) findViewById(R.id.btnNext);

        PlaceIntent.placeIntent.setClass(PlacePlanner.this, SelectedPlanner.class);

        //배너 클릭
        bannerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaceBannerItem item = (PlaceBannerItem) adapter.getItem(position);

                Bundle extras = new Bundle();
                extras.putString("img", Integer.toString(item.getImg()));
                extras.putString("title", item.getTitle());
                extras.putString("type", item.getType());

                PlaceIntent.placeIntent.putExtras(extras);
                startActivity(PlaceIntent.placeIntent);
            }
        });

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAttraction.setSelected(false);
        btnRestaurant.setSelected(false);
        btnCafe.setSelected(false);

        //명소 필터 버튼 클릭
        btnAttraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnAttraction.isSelected()){
                    btnAttraction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#404040")));
                    adapter.removeFilterType(PlaceBannerAdapter.ATT);
                }
                else {
                    btnAttraction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFD37A")));
                    adapter.addFilterType(PlaceBannerAdapter.ATT);
                }
                btnAttraction.setSelected(!btnAttraction.isSelected());
            }
        });
        //맛집 필터 버튼 클릭
        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnRestaurant.isSelected()){
                    btnRestaurant.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#404040")));
                    adapter.removeFilterType(PlaceBannerAdapter.REST);
                }
                else {
                    btnRestaurant.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F26C73")));
                    adapter.addFilterType(PlaceBannerAdapter.REST);
                }
                btnRestaurant.setSelected(!btnRestaurant.isSelected());
            }
        });
        //카페 필터 버튼 클릭
        btnCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnCafe.isSelected()){
                    btnCafe.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#404040")));
                    adapter.removeFilterType(PlaceBannerAdapter.CAFE);
                }
                else {
                    btnCafe.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6CE4B4")));
                    adapter.addFilterType(PlaceBannerAdapter.CAFE);
                }
                btnCafe.setSelected(!btnCafe.isSelected());
            }
        });
/*
        Set<String> removedListKeySet = null;
        if (PlaceIntent.placeSavedMap != null) {
            removedListKeySet = PlaceIntent.placeSavedMap.keySet();
        }

        // 이 부분은 SelectedPlanner class 의 imgBtnAddPlace 가 finish() 가 아닌 intent 객체로 이동하기 때문에 실행된다.
        // finish() 를 사용하게 되면 아래 해당되는 부분이 읽어지지 않아서 삭제한 활동이 반영되지 않는다.
        if (removedListKeySet != null) {
            for (String s : removedListKeySet) {
                if (!PlaceIntent.placeSavedMap.get(s)) {
                    PlaceIntent.placeIntent.removeExtra(s);
                }
            }
        }
*/
        // RelativeLayout 을 클릭 시 이미지들을 저장하도록 하는 Intent 객체를 불러온다.
        //PlaceIntent.placeIntent.setClass(PlacePlanner.this, SelectedPlanner.class);

        // 1일차, 2일차, ...
        Integer day = PlaceIntent.savedDateMap.get("startDay");
        textView.setText(Integer.toString(day) + "일차에 방문할 장소를 선택하세요");
/*
        // 여기서, att1, rest1, cafe1 의 RelativeLayout 을 클릭할 때, intent 로 data 를 저장한다.
        // putExtra 에서 첫 번째 변수는 임의로 지정한 문자열, 두 번째 변수는 RelativeLayout 의 id와 동일한 문자열.
        att1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceIntent.placeIntent.putExtra("att1_key", "att1");
                startActivity(PlaceIntent.placeIntent);
            }
        });
        rest1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PlaceIntent.placeIntent.putExtra("rest1_key", "rest1");
                startActivity(PlaceIntent.placeIntent);
            }
        });
        cafe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceIntent.placeIntent.putExtra("cafe1_key", "cafe1");
                startActivity(PlaceIntent.placeIntent);
            }
        });*/
    }
}