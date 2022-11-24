package com.seoultech.triplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.seoultech.triplanner.Model.PlaceIntent;
import com.seoultech.triplanner.Model.PostItem;

import java.util.ArrayList;

public class SelectedPlanner extends AppCompatActivity {

    ListView bannerListView;
    bannerPostAdapter adapter;
    ArrayList<PostItem> placeDataList; // 리스트뷰의 data 리스트

    ImageView imgBtnBack;

    TextView textView;

    Button btnAdd, btnNext;

    Bundle bundle;
    String imgData, titleData, typeData;
    int startDay, endDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_selected);

        bannerListView = (ListView) findViewById(R.id.selectedList);
        placeDataList = new ArrayList<>();
        adapter = new bannerPostAdapter(this, R.layout.place_selected_banner_item, placeDataList, false);
        adapter.useBtnDelete(true);
        bannerListView.setAdapter(adapter);

        imgBtnBack = (ImageView) findViewById(R.id.imgBtnBack);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        textView = (TextView) findViewById(R.id.textView);

        btnAdd = (Button) findViewById(R.id.btnAdd); //장소 추가 버튼 수정
        btnNext = (Button) findViewById(R.id.btnNext);

        // PlacePlanner.java 에서 putExtra 로 담은 내용을 bundle 에 담는다.
        bundle = getIntent().getExtras();

        // 1일차, 2일차, ...
        Integer day = PlaceIntent.savedDateMap.get("startDay");
        textView.setText(Integer.toString(day) + "일차 장소 선택 내역");

        //날짜 정보
        startDay = PlaceIntent.savedDateMap.get("startDay");
        endDay = PlaceIntent.savedDateMap.get("endDay");
        // 마지막 일차에는 다음버튼이 완료로 텍스트가 나타남
        if (endDay - startDay < 1) {
            btnNext.setText("완료");
        }

        //PlacePlanner 에서 클릭으로 보낸 data를 받는다
        Intent intent = getIntent();
        PostItem addItem = new PostItem();
        imgData = intent.getStringExtra("img");
        titleData = intent.getStringExtra("title");
        typeData = intent.getStringExtra("type");
        addItem.setThumbnail(imgData);
        addItem.setTitle(titleData);
        addItem.setTypePlace(typeData);

        //static ArrayList에 인텐트로 받아온 데이터 누적하기
        PlaceIntent.daySelectedPlace.add(addItem);

        placeDataList.addAll(PlaceIntent.daySelectedPlace); // 리스트뷰의 리스트에 누적 정보 모두 추가
        adapter.notifyDataSetChanged(); // 어댑터에 데이터 변경사항 적용, 리스트뷰에 나타남

        //배너를 클릭하면 지워짐(이후에 delete 버튼 만들어서 역할 이전시킬 예정)
        bannerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.removeItem(position); // 리스트 뷰에서 지우기
                PlaceIntent.daySelectedPlace.remove(position); // static 리스트 지우기
            }
        });

        if (bundle != null) {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!placeDataList.isEmpty()) {
                        ArrayList<PostItem> list = new ArrayList<PostItem>();
                        list.addAll(PlaceIntent.daySelectedPlace); // 리스트에 나타난 장소를 모두 담기

                        PlaceIntent.savedPlacesMap.put(startDay, list); // 총 저장소에 담기

                        if (startDay < endDay) {
                            // 여기서 PlacePlanner 의 날짜 값을 +1 증가시킴
                            PlaceIntent.savedDateMap.put("startDay", startDay + 1);

                            // 날짜가 변경될 때 마다 해당 날짜에 맞는 새로운 Intent 객체를 생성한다.
                            PlaceIntent.placeIntent = new Intent();
                            // 일차별 사용하는 리스트도 새롭게 비워준다
                            PlaceIntent.daySelectedPlace.clear();

                            // 다음날
                            Intent intentBack = new Intent(SelectedPlanner.this, PlacePlanner.class);
                            startActivity(intentBack);
                        }
                        else {
                            // 모두 초기화
                            PlaceIntent.placeIntent = new Intent();
                            PlaceIntent.daySelectedPlace.clear();

                            // finish
                            Intent intentFinish = new Intent(SelectedPlanner.this, FinishPlanner.class);
                            startActivity(intentFinish);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "방문할 장소를 추가해주세요!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
