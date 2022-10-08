package com.seoultech.triplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DatePlanner extends AppCompatActivity {
    ImageButton btnBack;

    CalendarView calendarView;
    TextView textViewStartDate, textViewEndDate;
    Button btnReset, btnSelect;
    Button btnNext;

    int dateSelectCount = 0;
    int startYear, startMonth, startDay;
    int endYear, endMonth, endDay;

    boolean btnSelectIsClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_date);

        btnBack = (ImageButton) findViewById(R.id.imgBtnBack);

        calendarView = (CalendarView) findViewById(R.id.calendarView);

        textViewStartDate = (TextView) findViewById(R.id.textViewStartDate);
        textViewEndDate = (TextView) findViewById(R.id.textViewEndDate);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnSelect = (Button) findViewById(R.id.btnSelect);

        btnNext = (Button) findViewById(R.id.btnNext);

        //뒤로가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatePlanner.this, RegionPlanner.class);
                startActivity(intent);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView,
                                            int i, int i1, int i2) {
                if (dateSelectCount == 0) {
                    startYear = i;
                    startMonth = i1 + 1;
                    startDay = i2;
                    textViewStartDate.setText("여행 시작일 : " + startYear + "년 " + startMonth + "월 "
                            + startDay + "일");

                    dateSelectCount++;
                }
                else if (dateSelectCount == 1) {
                    endYear = i;
                    endMonth = i1 + 1;
                    endDay = i2;
                    if (startMonth > endMonth) {
                        Toast.makeText(getApplicationContext(),
                                "시작 월이 종료 월보다 앞설 수 없습니다!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (startDay > endDay) {
                            Toast.makeText(getApplicationContext(),
                                    "시작 날짜가 종료 날짜보다 앞설 수 없습니다!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            textViewEndDate.setText("여행 종료일 : " + endYear + "년 " + endMonth + "월 "
                                    + endDay + "일");
                            dateSelectCount++;
                        }
                    }
                }
                else {
                   Toast.makeText(getApplicationContext(),
                            "이미 날짜를 모두 선택하셨습니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSelectCount = 0;
                textViewStartDate.setText("여행 시작일 : ");
                textViewEndDate.setText("여행 종료일 : ");
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 여기서 database(firebase 등)에 시작 날짜, 종료 날짜 데이터를 넣도록 한다.


                // 이 값을 true 로 설정해야, '다음' 버튼을 눌렀을 때 화면이 전환되도록 한다.
                btnSelectIsClicked = true;

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btnSelectIsClicked) {
                    Toast.makeText(getApplicationContext(),
                            "일정 선택 버튼을 클릭해서 일정을 등록해 주세요!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(DatePlanner.this, PlacePlanner.class);
                    startActivity(intent);  // Activity 이동
                }
            }
        });
    }

}
