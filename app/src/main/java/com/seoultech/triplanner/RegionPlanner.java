package com.seoultech.triplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegionPlanner extends AppCompatActivity {

    ImageButton btnBack;

    ImageButton regionA, regionA_down, regionB;
    TextView regionAText, regionBText;

    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_region);

        btnBack = (ImageButton) findViewById(R.id.imgBtnBack);

        regionA = findViewById(R.id.regionA);
        regionA_down = findViewById(R.id.regionA_down);
        regionAText = findViewById(R.id.regionAText);
        regionB = findViewById(R.id.regionB);
        regionBText = findViewById(R.id.regionBText);

        btnNext = (Button) findViewById(R.id.btnNext);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        regionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regionA.isSelected()) {
                    regionA.setImageResource(R.drawable.img_region_a_up);
                    regionA.setScaleX(1);
                    regionA.setScaleY(1);
                    regionA_down.setImageResource(R.drawable.img_region_a_down);
                    regionA_down.setScaleX(1);
                    regionA_down.setScaleY(1);
                    regionA_down.setTranslationY(0);
                    regionAText.setVisibility(View.GONE);

                }
                else {
                    regionA.setImageResource(R.drawable.img_region_a_up_selected);
                    regionA.setScaleX((float) 1.2);
                    regionA.setScaleY((float) 1.2);
                    regionA_down.setImageResource(R.drawable.img_region_a_down_selected);
                    regionA_down.setScaleX((float) 1.2);
                    regionA_down.setScaleY((float) 1.2);
                    regionA_down.setTranslationY((float) 61);
                    regionAText.setVisibility(android.view.View.VISIBLE);

                    regionB.setSelected(true);
                    regionB.callOnClick();
                }
                regionA.setSelected(!regionA.isSelected());
            }
        });
        regionA_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regionA_down.isSelected()) {
                    regionA.callOnClick();
                }
                else {
                    regionA.callOnClick();
                }
                regionA_down.setSelected(!regionA_down.isSelected());
            }
        });
        regionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regionB.isSelected()) {
                    regionB.setImageResource(R.drawable.img_region_b);
                    regionB.setScaleX(1);
                    regionB.setScaleY(1);
                    regionBText.setVisibility(View.GONE);
                }
                else {
                    regionB.setImageResource(R.drawable.img_region_b_selected);
                    regionB.setScaleX((float) 1.2);
                    regionB.setScaleY((float) 1.2);
                    regionBText.setVisibility(android.view.View.VISIBLE);

                    regionA.setSelected(true);
                    regionA.callOnClick();
                }
                regionB.setSelected(!regionB.isSelected());
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegionPlanner.this, PlacePlanner.class);
                if (regionA.isSelected()) {
                    intent.putExtra("regionType", "N");
                    startActivity(intent);
                }
                else if (regionB.isSelected()) {
                    intent.putExtra("regionType", "S");
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "지역을 선택해주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
