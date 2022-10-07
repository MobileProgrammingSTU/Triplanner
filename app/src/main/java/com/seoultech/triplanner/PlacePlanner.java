package com.seoultech.triplanner;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PlacePlanner extends AppCompatActivity {

    ImageButton imgBtnBack;

    RelativeLayout att1, rest1, cafe1;
    Button btnAttraction, btnRestaurant, btnCafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_place);

        imgBtnBack = (ImageButton) findViewById(R.id.imgBtnBack);

        btnAttraction = (Button) findViewById(R.id.imageAtt);
        btnRestaurant = (Button) findViewById(R.id.imageRes);
        btnCafe = (Button) findViewById(R.id.imageCafe);

        att1 = (RelativeLayout) findViewById(R.id.att1);
        rest1 = (RelativeLayout) findViewById(R.id.rest1);
        cafe1 = (RelativeLayout) findViewById(R.id.cafe1);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlacePlanner.this, DatePlanner.class);
                startActivity(intent);
            }
        });

        btnAttraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnAttraction.isSelected()){
                    btnAttraction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#404040")));
                    att1.setVisibility(View.GONE);
                }
                else {
                    btnAttraction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFD37A")));
                    att1.setVisibility(View.VISIBLE);
                }
                btnAttraction.setSelected(!btnAttraction.isSelected());
            }
        });
        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnRestaurant.isSelected()){
                    btnRestaurant.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#404040")));
                    rest1.setVisibility(View.GONE);
                }
                else {
                    btnRestaurant.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F26C73")));
                    rest1.setVisibility(View.VISIBLE);
                }
                btnRestaurant.setSelected(!btnRestaurant.isSelected());
            }
        });
        btnCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnCafe.isSelected()){
                    btnCafe.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#404040")));
                    cafe1.setVisibility(View.GONE);
                }
                else {
                    btnCafe.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6CE4B4")));
                    cafe1.setVisibility(View.VISIBLE);
                }
                btnCafe.setSelected(!btnCafe.isSelected());
            }
        });
    }

}