package com.seoultech.triplanner;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PlannerThirdActivity extends AppCompatActivity {

    Toolbar toolBar;
    Button button, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_third);

        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar (toolBar);                  //액티비티의 앱바(App Bar)로 지정
        ActionBar actionBar = getSupportActionBar ();   //앱바 제어를 위해 툴바 액세스
        actionBar.setDisplayHomeAsUpEnabled (true);     // 앱바에 뒤로가기 버튼 만들기
        actionBar.setTitle("플랜 추가");
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home: //  툴바 뒤로가기버튼 눌렸을 때 동작.
                finish ();          // 현재는 종료되도록 설정
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }
    }
}