package com.seoultech.triplanner;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.seoultech.triplanner.Model.CustomTimePickerDialog;
import com.seoultech.triplanner.Model.PlaceIntent;
import com.seoultech.triplanner.Model.PlanItem;
import com.seoultech.triplanner.Model.PostItem;
import com.seoultech.triplanner.Model.TimePickerDialogClickListener;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;

public class SelectedPlanner extends AppCompatActivity {

    ListView bannerListView;
    bannerPostAdapter adapter;
    ArrayList<PostItem> placeDataList; // 리스트뷰의 data 리스트

    ImageView imgBtnBack;
    TextView textView;
    Button btnAdd, btnNext;

    Bundle bundle;
    String pidData, imgData, titleData, typeData;
    int startDay, endDay;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final String fbCurrentUserUID = mFirebaseAuth.getUid();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef;

    PlanItem fbPlanItem; // Firebase 업로드할 데이터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planner_selected);

        bannerListView = (ListView) findViewById(R.id.selectedList);
        placeDataList = new ArrayList<>();
        adapter = new bannerPostAdapter(this, R.layout.place_selected_banner_item, placeDataList, false);
        adapter.useBtnDelete(true);
        adapter.isDeleteList(true);
        bannerListView.setAdapter(adapter);

        textView = (TextView) findViewById(R.id.textView); // 일차수 출력 text
        imgBtnBack = (ImageView) findViewById(R.id.imgBtnBack); // 뒤로가기 버튼
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAdd = (Button) findViewById(R.id.btnAdd); // 장소 추가 버튼
        btnNext = (Button) findViewById(R.id.btnNext); // 다음 버튼

        mDatabaseRef = mDatabase.getReference("Triplanner");
        DatabaseReference dbRefPlans = mDatabaseRef.child("UserAccount").child(fbCurrentUserUID).child("Plans");
        fbPlanItem = new PlanItem(); // 업로드할 PlanItem 정보

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

        // DB 업로드할 시작, 종료 날짜 정보
        LocalDate dateStart = PlaceIntent.savedDates.get("dateStart");
        LocalDate dateEnd = PlaceIntent.savedDates.get("dateEnd");
        int days = (int) ChronoUnit.DAYS.between(dateStart, dateEnd);
        fbPlanItem.setFbDateStart(dateStart.toString().replace("-",".")); // 20XX-MM-dd -> 20XX.MM.dd
        fbPlanItem.setFbDateEnd(dateEnd.toString().replace("-","."));
        if (days != 0)
            fbPlanItem.setFbPlanTitle(days+"박" + (days+1)+"일" + " 여행"); // 플랜 제목 설정
        else
            fbPlanItem.setFbPlanTitle(" 당일치기 여행");

        //PlacePlanner 에서 클릭으로 보낸 data를 받는다
        Intent intent = getIntent();
        PostItem addItem = new PostItem();

        pidData = intent.getStringExtra("pid");
        imgData = intent.getStringExtra("img");
        titleData = intent.getStringExtra("title");
        typeData = intent.getStringExtra("type");
        addItem.setThumbnail(imgData);
        addItem.setTitle(titleData);
        addItem.setTypePlace(typeData);
        addItem.setPid(pidData);

        //static ArrayList에 인텐트로 받아온 데이터 누적하기
        PlaceIntent.daySelectedPlace.add(addItem);

        placeDataList.addAll(PlaceIntent.daySelectedPlace); // 리스트뷰의 리스트에 누적 정보 모두 추가
        adapter.notifyDataSetChanged(); // 어댑터에 데이터 변경사항 적용, 리스트뷰에 나타남

        //배너를 클릭
        bannerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomTimePickerDialog timePicker = new CustomTimePickerDialog(SelectedPlanner.this, new TimePickerDialogClickListener() {
                    @Override
                    public void onPositiveClick(int h, int m) {
                        String pickedTime;
                        if (m != 0) // 0분이 아니면
                            pickedTime = h + "시 " + m + "분";
                        else
                            pickedTime = h + "시";
                        // 장소 아이템(PostItem)에 시간값 저장(플랜 아이템 아님)
                        PlaceIntent.daySelectedPlace.get(position).setPlanTime(pickedTime);
                    }

                    @Override
                    public void onNegativeClick() {

                    }
                });
                timePicker.setHour(0);
                timePicker.setMinute(0);
                timePicker.setCanceledOnTouchOutside(true);
                timePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePicker.show();
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
                    if (!placeDataList.isEmpty() && checkAllTimeSet()) {
                        ArrayList<PostItem> list = new ArrayList<PostItem>();
                        list.addAll(PlaceIntent.daySelectedPlace); // 리스트에 나타난 장소를 모두 담기

                        PlaceIntent.savedPlacesMap.put("day" + String.valueOf(startDay), list); // 총 저장소에 담기

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

                            // PlanItem에 모든 정보 적용(set)
                            String newRandomKey = dbRefPlans.push().getKey(); // 랜덤 키 생성
                            fbPlanItem.setFbPlanID(newRandomKey + "_" + fbPlanItem.getFbPlacesByDay().size()+"d_" +
                                    fbPlanItem.getFbDateStart()); // planID : 랜덤키+여행일수+최초여행시작날
                            fbPlanItem.setFbPlacesByDay(PlaceIntent.savedPlacesMap);
                            fbPlanItem.setFbPlanType(PlaceIntent.intentRegionType); // 플랜타입 : 지역타입
                            fbPlanItem.setFbThumbnail(fbPlanItem.getFbPlacesByDay().get("day1").get(0).getThumbnail());

                            // fbPlanItem 데이터베이스에 업로드
                            dbRefPlans.child(newRandomKey).setValue(fbPlanItem);

                            // finish
                            //Intent intentFinish = new Intent(SelectedPlanner.this, FinishPlanner.class);
                            // 스토리지의 내플랜으로 이동할 intent 정보
                            Intent intentHome = new Intent(SelectedPlanner.this, MainActivity.class);
                            intentHome.putExtra("moveFragment", "storage_plan");
                            startActivity(intentHome);
                        }
                    }
                    else if (!checkAllTimeSet()) {
                        Toast.makeText(getApplicationContext(),
                                "장소를 클릭해 시간을 설정해주세요!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "방문할 장소를 추가해주세요!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    // 모든 장소에 시간 설정하면 true, 하나라도 아니면 flase
    private Boolean checkAllTimeSet() {
        for (PostItem item : PlaceIntent.daySelectedPlace) {
            if (item.getPlanTime() == null)
                return false;
        }
        return true;
    }


}
