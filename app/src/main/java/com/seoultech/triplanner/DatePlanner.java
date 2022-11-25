package com.seoultech.triplanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import com.seoultech.triplanner.Model.PlaceIntent;

import org.threeten.bp.LocalDate;

import java.util.Calendar;
import java.util.List;

public class DatePlanner extends AppCompatActivity {
    ImageButton btnBack;

    MaterialCalendarView calendarView;

    TextView textViewSelectedDate;
    ImageButton btnReset;
    Button btnNext;

    int startYear, startMonth, startDay;
    int endYear, endMonth, endDay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_date);

        btnBack = (ImageButton) findViewById(R.id.imgBtnBack);

        //new calendarView
        calendarView = findViewById(R.id.calendar);

        textViewSelectedDate = (TextView) findViewById(R.id.textViewSelectedDate);
        btnReset = (ImageButton) findViewById(R.id.btnReset);

        btnNext = (Button) findViewById(R.id.btnNext);

        //뒤로가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatePlanner.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //새 캘린더뷰---------------------------------------------------------------------------------
        //현재 날짜 구하기
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        int curMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        int curDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        final int selectDaysLimit = 7; // 여행 기간 최대 날짜 수 제한
        CalendarDay startDate = CalendarDay.from(curYear, curMonth, curDay);
        CalendarDay endDate = CalendarDay.from(curYear+1, curMonth, curDay);

        calendarView.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(curYear, curMonth, curDay)) // 최소날짜
                .setMaximumDate(CalendarDay.from(curYear+1, curMonth, curDay)) // 최대날짜
                .setCalendarDisplayMode(CalendarMode.MONTHS).commit();

        // 데코레이션 적용 : 날짜선택 스타일, 최대최소 날짜 제한
        calendarView.addDecorators(new DayDecorator(this), new MinMaxDecorator(startDate, endDate));

        // 하루 선택 or 시작 날짜 선택
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                startYear = date.getYear();
                startMonth = date.getMonth();
                startDay = date.getDay();

                endYear = date.getYear();
                endMonth = date.getMonth();
                endDay = date.getDay();
                textViewSelectedDate.setText(startYear+"."+startMonth+"."+startDay);

                // 여행 기간 날짜 수 제한
                LocalDate StartDate = date.getDate().minusDays(selectDaysLimit-1);
                LocalDate EndDate = date.getDate().plusDays(selectDaysLimit-1);
                calendarView.addDecorators(new MinMaxDecorator(CalendarDay.from(StartDate), CalendarDay.from(EndDate)));

                Toast.makeText(getApplicationContext(),"여행 기간은 최대 7일 입니다!", Toast.LENGTH_SHORT).show();
            }
        });
        // 종료 날짜 선택
        calendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                startYear = dates.get(0).getYear();
                startMonth = dates.get(0).getMonth();
                startDay = dates.get(0).getDay();
                textViewSelectedDate.setText(startYear+"."+startMonth+"."+startDay);

                endYear = dates.get(dates.size() - 1).getYear();
                endMonth = dates.get(dates.size() - 1).getMonth();
                endDay = dates.get(dates.size() - 1).getDay();

                textViewSelectedDate.setText(startYear+"."+startMonth+"."+startDay + "   ~   "
                        + endYear+"."+endMonth+"."+endDay);

                // 데코레이션 초기화
                calendarView.removeDecorators();
                calendarView.addDecorators(new DayDecorator(getApplicationContext()),
                        new MinMaxDecorator(startDate, endDate));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.clearSelection();

                startYear = 0;
                startMonth = 0;
                startDay = 0;
                endYear = 0;
                endMonth = 0;
                endDay = 0;

                textViewSelectedDate.setText("-");
                // 데코레이션 초기화
                calendarView.removeDecorators();
                calendarView.addDecorators(new DayDecorator(getApplicationContext()), new MinMaxDecorator(startDate, endDate));
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calendarView.getSelectedDates().isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "여행 기간을 설정해 주세요!", Toast.LENGTH_SHORT).show();
                    textViewSelectedDate.setText("-");
                }
                else {
                    // 여기서 시작 일자와 끝 일자를 저장한다.
                    PlaceIntent.savedDateMap.put("startDay", startDay - (startDay - 1));
                    PlaceIntent.savedDateMap.put("endDay", endDay - (startDay - 1));

                    Intent intent = new Intent(DatePlanner.this, RegionPlanner.class);
                    startActivity(intent);  // Activity 이동
                }
            }
        });
    }

    /* 선택된 요일의 background를 설정하는 Decorator 클래스 */
    static class DayDecorator implements DayViewDecorator {
        private final Drawable drawable;

        public DayDecorator(Context context) {
            drawable = ContextCompat.getDrawable(context, R.drawable.planner_date_calendar_view_selector);
        }

        // true를 리턴 시 모든 요일에 내가 설정한 드로어블이 적용된다
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return true;
        }

        // 일자 선택 시 내가 정의한 드로어블이 적용되도록 한다
        @Override
        public void decorate(DayViewFacade view) {
            view.setSelectionDrawable(drawable);
        }
    }

    static class MinMaxDecorator implements DayViewDecorator {
        CalendarDay dayMin, dayMax;
        public MinMaxDecorator(CalendarDay min, CalendarDay max) {
            dayMin = min;
            dayMax = max;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return (day.getDate().isAfter(dayMax.getDate()))
                    || (day.getDate().isBefore(dayMin.getDate()));
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.parseColor("#000000")));
            view.setDaysDisabled(true);
        }
    }
}