package com.seoultech.triplanner;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seoultech.triplanner.Model.PostItem;
import com.seoultech.triplanner.Model.UserAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostWriteActivity extends AppCompatActivity {

    ImageView img_camera;
    EditText edt_Title, edt_subTitle, edt_content;
    Spinner spinner_Region, spinner_Place;
    Button btn_write;
    ImageButton btnBack;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final String fbCurrentUserUID = mFirebaseAuth.getUid();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef;

    final String [] Region = {"지역을 선택하세요", "북부", "남부"}; // 0번째 추가
    final String [] Place = {"장소 타입을 선택하세요", "카페", "명소", "맛집"}; // 0번째 추가
    final String imgStr = "imgurl";
    PostItem postItem;

    Boolean inputTitle, inputSubtitle, inputRegion,
            inputPlace, inputContent;

    int colFontLight, colFontEmp, colBlue, colBG2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postwrite);

        colFontLight = ContextCompat.getColor(this, R.color.colorFontLight);
        colFontEmp = ContextCompat.getColor(this, R.color.colorFontEmphasis);
        colBlue = ContextCompat.getColor(this, R.color.colorBrandBlue);
        colBG2 = ContextCompat.getColor(this, R.color.colorGrayBG2);

        img_camera = (ImageView) findViewById(R.id.img_camera);
        edt_Title = (EditText) findViewById(R.id.edt_Title);
        edt_subTitle = (EditText)findViewById(R.id.edt_subTitle);
        edt_content = (EditText) findViewById(R.id.edt_content);
        btn_write = (Button) findViewById(R.id.btn_write);
        btnBack = (ImageButton) findViewById(R.id.imgBtnBack); // 뒤로가기 버튼

        spinner_Region = (Spinner) findViewById(R.id.spinner_Region);
        spinner_Place = (Spinner) findViewById(R.id.spinner_Place);

        inputTitle = false;
        inputSubtitle = false;
        inputRegion = false;inputPlace = false;
        inputContent = false;

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(this,
                R.layout.writepost_spinner_item, Region) {
            @Override // 0번째 아이템 색 바꾸기
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;

                if(position == 0)
                    textView.setTextColor(colFontLight);

                return view;
            }

            @Override // 0번째 아이템 선택 불가
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                }
                return true;
            }

            @Override// 드롭다운에서 0번째 아이템 색 바꾸기
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View mView = super.getDropDownView(position, convertView, parent);
                TextView mTextView = (TextView) mView;
                if (position == 0) {
                    mTextView.setTextColor(colFontLight);
                }

                return mView;
            }
        };
        spinner_Region.setAdapter(regionAdapter);

        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(this,
                R.layout.writepost_spinner_item, Place) {
            @Override // 0번째 아이템 색 바꾸기
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;

                if(position == 0)
                    textView.setTextColor(colFontLight);

                return view;
            }

            @Override // 0번째 아이템 선택 불가
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                }
                return true;
            }

            @Override// 드롭다운에서 0번째 아이템 색 바꾸기
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View mView = super.getDropDownView(position, convertView, parent);
                TextView mTextView = (TextView) mView;
                if (position == 0) {
                    mTextView.setTextColor(colFontLight);
                }

                return mView;
            }
        };
        spinner_Place.setAdapter(placeAdapter);

        // 여기서 먼저 mDatabaseRef 선언
        mDatabaseRef = mDatabase.getReference("Triplanner");

        postItem = new PostItem();

        List<String> list = new ArrayList<>();

        spinner_Region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                }
                else if (position == 1) {
                    postItem.setTypeRegion("N");
                    inputRegion = true;
                }
                else {
                    postItem.setTypeRegion("S");
                    inputRegion = true;
                }
                isAllInputComplete();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_Place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                if (position == 0) {

                }
                else if (position == 1){
                    postItem.setTypePlace("cafe");
                    inputPlace = true;
                }
                else if (position == 2){
                    postItem.setTypePlace("att");
                    inputPlace = true;
                }
                else {
                    postItem.setTypePlace("rest");
                    inputPlace = true;
                }
                isAllInputComplete();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // p2, p3, ... 처럼 db 의 table 값을 받아오기 위해 사용
        // 내림차순으로 정렬한 뒤, 마지막 값에서 +1 추가 필요
        mDatabaseRef.child("Post2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    list.add(dataSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 현재 접속 중인 user 의 FbName 을 db 에서 읽어오기 위해 작성(글 작성자의 fbName)
        mDatabaseRef.child("UserAccount").child(fbCurrentUserUID).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserAccount userAccount = snapshot.getValue(UserAccount.class);
                        postItem.setPublisher(userAccount.getFbName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("imgurl1",
                "https://firebasestorage.googleapis.com/v0/b/triplanner-c5df2.appspot.com/o/img_planner_place_restaurant_1.jpg?alt=media&token=6a6ead11-30f7-4f76-b181-2fd62421d95e");

        postItem.setImages(hashmap);
        postItem.setThumbnail("https://firebasestorage.googleapis.com/v0/b/triplanner-c5df2.appspot.com/o/img_planner_place_restaurant_1.jpg?alt=media&token=6a6ead11-30f7-4f76-b181-2fd62421d95e");

        edt_Title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputTitle = editable.length() > 0;
                isAllInputComplete();
            }
        });
        edt_subTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputSubtitle = editable.length() > 0;
                isAllInputComplete();
            }
        });
        edt_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputContent = editable.length() > 0;
                isAllInputComplete();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (edt_content.getText().length() == 0 || edt_Title.getText().length() == 0 ||
//                        edt_subTitle.getText().length() == 0) {
//
//                    btn_write.setBackgroundTintList(ColorStateList.valueOf(colBG2));
//                    btn_write.setEnabled(false);
//                    btn_write.setTextColor(colFontLight);
//
//                    Toast.makeText(getApplicationContext(), "입력되지 않는 부분이 있습니다!",
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }

                // 데이터 처리 후 pid column 값 설정
                String s = list.get(list.size() - 1);
                int num = Character.getNumericValue(s.charAt(1));
                postItem.setPid("post" + Integer.toString(num + 1));

                // 이 내역들을 여기서 받는 이유는, btn_write.setOnClickListener 밖에서 받을 시 db 에 값이 저장되지 않는다.
                // 아마 "Fragment 생명 주기" 개념과 연관이 있지 않을까 추측.
                postItem.setTitle(edt_Title.getText().toString());
                postItem.setSubtitle(edt_subTitle.getText().toString());
                postItem.setContent(edt_content.getText().toString());

                // db table 이름 설정
                String fbTableName = s.charAt(0) + Integer.toString(num + 1);
                mDatabaseRef.child("Post2").child(fbTableName).setValue(postItem);

                Toast.makeText(getApplicationContext(), "글 작성이 완료되었습니다", Toast.LENGTH_SHORT).show();

                // 글 작성이 완료되면, 우선 MainActivity 로 화면 이동
                Intent intentHome = new Intent(PostWriteActivity.this, MainActivity.class);
                intentHome.putExtra("moveFragment", "storage_MyPost");
                startActivity(intentHome);

            }
        });
    }

    // 모두 입력하였는지 확인하기 : 모두 입력해야 동작
    public void isAllInputComplete() {
        if (inputTitle && inputSubtitle && inputRegion && inputPlace && inputContent ) {
            btn_write.setBackgroundTintList(ColorStateList.valueOf(colBlue));
            btn_write.setEnabled(true);
            btn_write.setTextColor(colFontEmp);
        }
        else {
            btn_write.setBackgroundTintList(ColorStateList.valueOf(colBG2));
            btn_write.setEnabled(false);
            btn_write.setTextColor(colFontLight);
        }
    }

    // editText 눌렀을 때 밖에 다른곳 누르면 해제되고 키보드 내림
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
