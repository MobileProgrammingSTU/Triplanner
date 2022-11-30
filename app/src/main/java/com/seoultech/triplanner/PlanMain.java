package com.seoultech.triplanner;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.seoultech.triplanner.Model.PlanIntent;
import com.seoultech.triplanner.Model.PlanItem;
import com.seoultech.triplanner.Model.PostItem;

import java.util.ArrayList;
import java.util.HashMap;

public class PlanMain extends AppCompatActivity {

    ListView bannerListView;
    bannerPlanInfoAdapter adapter;
    ArrayList<ArrayList<PostItem>> planItemList;

    ImageButton btnBack;
    TextView tvTitle;

    private PlanItem staticPlanItem;
    private String planTitle;
    private String planType;
    private String planStart;
    private String planEnd;
    private HashMap<String, ArrayList<PostItem>> planPlaces;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_plan);

        staticPlanItem = PlanIntent.intentPlanItem;
        planTitle = staticPlanItem.getFbPlanTitle();
        planType = staticPlanItem.getFbPlanType();
        planStart = staticPlanItem.getFbDateStart();
        planEnd = staticPlanItem.getFbDateEnd();
        planPlaces = staticPlanItem.getFbPlacesByDay();

        btnBack = (ImageButton) findViewById(R.id.imgBtnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlanIntent.intentPlanItem = new PlanItem(); // 초기화
                finish();
            }
        });

        tvTitle = (TextView) findViewById(R.id.planTitle);
        tvTitle.setText(planTitle);

        // ListView 기본 설정
        bannerListView = (ListView) findViewById(R.id.listViewPlan);
        planItemList = new ArrayList<>();
        adapter = new bannerPlanInfoAdapter(this, R.layout.myplan_banner_item, planItemList);
        bannerListView.setAdapter(adapter);

        // day index (day1 == 0)
        for (String key : planPlaces.keySet()) {
            ArrayList<PostItem> placesByDay = planPlaces.get(key);
            planItemList.add(placesByDay);
        }
        adapter.notifyDataSetChanged();



    }
}
