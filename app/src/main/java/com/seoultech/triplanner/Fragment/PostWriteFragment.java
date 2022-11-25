package com.seoultech.triplanner.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seoultech.triplanner.MainActivity;
import com.seoultech.triplanner.Model.PostItem;
import com.seoultech.triplanner.Model.UserAccount;
import com.seoultech.triplanner.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostWriteFragment extends Fragment {

    ImageView img_camera;
    EditText edt_Title, edt_subTitle, edt_content;
    Spinner spinner_Region, spinner_Place;
    Button btn_write;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_postwrite, container, false);

        img_camera = (ImageView) view.findViewById(R.id.img_camera);
        edt_Title = (EditText) view.findViewById(R.id.edt_Title);
        edt_subTitle = (EditText) view.findViewById(R.id.edt_subTitle);
        edt_content = (EditText) view.findViewById(R.id.edt_content);
        btn_write = (Button) view.findViewById(R.id.btn_write);

        spinner_Region = (Spinner) view.findViewById(R.id.spinner_Region);
        spinner_Place = (Spinner) view.findViewById(R.id.spinner_Place);

        inputTitle = false;
        inputSubtitle = false;
        inputRegion = false;inputPlace = false;
        inputContent = false;

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.writepost_spinner_item, Region) {
            @Override // 0번째 아이템 색 바꾸기
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;

                if(position == 0)
                    textView.setTextColor(Color.parseColor("#4B4B4B"));

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
                    mTextView.setTextColor(Color.parseColor("#4B4B4B"));
                }

                return mView;
            }
        };
        spinner_Region.setAdapter(regionAdapter);

        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.writepost_spinner_item, Place) {
            @Override // 0번째 아이템 색 바꾸기
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;

                if(position == 0)
                    textView.setTextColor(Color.parseColor("#4B4B4B"));

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
                    mTextView.setTextColor(Color.parseColor("#4B4B4B"));
                }

                return mView;
            }
        };
        spinner_Place.setAdapter(placeAdapter);

        // 여기서 먼저 mDatabaseRef 선언
        mDatabaseRef = mDatabase.getReference("Triplanner");

        return view;
    }

    /*
    아래 메소드에서, postItem 에 담길 data 들을 넣는다.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
                inputTitle = true;
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
                inputSubtitle = true;
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
                inputContent = true;
                isAllInputComplete();
            }
        });

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                for (String s : list) {
//                    System.out.println(s);
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

                Toast.makeText(getActivity(), "글 작성이 완료되었습니다", Toast.LENGTH_SHORT).show();

                // 글 작성이 완료되면, MainActivity 로 화면 이동
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });
    }

    // 모두 입력하였는지 확인하기 : 모두 입력해야 동작
    public void isAllInputComplete() {
        if (inputTitle && inputSubtitle && inputRegion && inputPlace && inputContent ) {
            btn_write.setBackgroundColor(Color.parseColor("#5B4FBB"));
            btn_write.setEnabled(true);
            btn_write.setTextColor(Color.parseColor("#e1e1e1"));
        }
    }

}
