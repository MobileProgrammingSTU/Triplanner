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
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import com.seoultech.triplanner.Model.PlaceIntent;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.Calendar;
import java.util.List;

public class DatePlanner extends AppCompatActivity {
    ImageButton btnBack;

    MaterialCalendarView calendarView;
    OtherDaysDecorator otherMonthDateDec;

    TextView textViewSelectedDate;
    ImageButton btnReset;
    Button btnNext;

    int startDay, endDay;
    int datePeriod; // 날짜(date) 차이, 총 일수

    // 날짜(연월일) 정보, 오로지 데이터 전송용
    LocalDate dateStart, dateEnd;

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
        otherMonthDateDec = new OtherDaysDecorator(getApplicationContext(), calendarView);
        calendarView.addDecorators(otherMonthDateDec, new DayDecorator(this), new MinMaxDecorator(startDate, endDate));


        // 월이 바뀌면
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                calendarView.addDecorator(otherMonthDateDec);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.clearSelection();

                dateStart = LocalDate.now(); // 현재 날짜로 초기화
                dateEnd = LocalDate.now();

                textViewSelectedDate.setText("-");
                // 데코레이션 초기화
                calendarView.removeDecorators();
                calendarView.addDecorators(otherMonthDateDec,
                        new DayDecorator(getApplicationContext()), new MinMaxDecorator(startDate, endDate));
            }
        });
        // 하루 선택 or 시작 날짜 선택
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                dateStart = date.getDate();
                dateEnd = date.getDate();

                textViewSelectedDate.setText(dateStart.toString().replace("-", "."));

                // 여행 기간 날짜 수 제한
                LocalDate StartDate = dateStart.minusDays(selectDaysLimit-1);
                LocalDate EndDate = dateEnd.plusDays(selectDaysLimit-1);
                calendarView.addDecorators(new MinMaxDecorator(CalendarDay.from(StartDate), CalendarDay.from(EndDate)));

                // Toast.makeText(getApplicationContext(),"여행 기간은 최대 7일 입니다!", Toast.LENGTH_SHORT).show();
            }
        });
        // 종료 날짜 선택
        calendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                dateStart = dates.get(0).getDate();
                dateEnd = dates.get(dates.size() - 1).getDate();

                textViewSelectedDate.setText(dateStart.toString().replace("-", ".") +
                        "   ~   " + dateEnd.toString().replace("-", "."));

                // 데코레이션 초기화
                calendarView.removeDecorators();
                calendarView.addDecorators(otherMonthDateDec, new DayDecorator(getApplicationContext()),
                        new MinMaxDecorator(startDate, endDate));
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
                    // 오류 발생 (해결) : 단순히 day로 값을 설정하면 월을 넘기면서 날짜를 지정 못함
                    datePeriod = (int) ChronoUnit.DAYS.between(dateStart, dateEnd);
                    startDay = 1; // 시작 날수는 1로 설정 (1일차)
                    endDay = datePeriod + 1;

                    PlaceIntent.savedDateMap.put("startDay", startDay);
                    PlaceIntent.savedDateMap.put("endDay", endDay);

                    // 이후에 DB 업로드할 날짜 정보 저장
                    PlaceIntent.savedDates.put("dateStart", dateStart);
                    PlaceIntent.savedDates.put("dateEnd", dateEnd);

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

    //달(월)이 다른날은 날짜 색 연하게
    public class OtherDaysDecorator implements DayViewDecorator {
        private Context context;
        private MaterialCalendarView calendarView;

        public OtherDaysDecorator(Context context, MaterialCalendarView calendarView) {
            this.context = context;
            this.calendarView = calendarView;
        }


        @Override
        public boolean shouldDecorate(CalendarDay day) {
            LocalDate otherDate = day.getDate();
            LocalDate selectedDate = calendarView.getCurrentDate().getDate();

            return otherDate.getYear() != selectedDate.getYear() ||
                    (otherDate.getYear() == selectedDate.getYear() &&
                    otherDate.getMonth() != selectedDate.getMonth());
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.parseColor("#4B4B4B")));
        }
    }
}
