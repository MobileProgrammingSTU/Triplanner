package com.seoultech.triplanner.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.HashMap;

public class PostWriteFragment extends Fragment {

    ImageView img_camera;
    EditText edt_Title, edt_subTitle, edt_content;
    Spinner spinner_Region, spinner_Place;
    Button btn_write;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final String fbCurrentUserUID = mFirebaseAuth.getUid();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef;

    final String [] Region = {"북부", "남부"};
    final String [] Place = {"카페", "명소", "맛집"};
    final String imgStr = "imgurl";
    PostItem postItem;

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

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, Region);
        spinner_Region.setAdapter(regionAdapter);

        ArrayAdapter<String> PlaceAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, Place);
        spinner_Place.setAdapter(PlaceAdapter);

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

        spinner_Region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                postItem.setTypeRegion(Region[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_Place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                postItem.setTypePlace(Place[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        postItem.setPid("post7");

        HashMap<String, String> hashmap = new HashMap<>();
        hashmap.put("imgurl1",
                "https://firebasestorage.googleapis.com/v0/b/triplanner-c5df2.appspot.com/o/img_planner_place_restaurant_1.jpg?alt=media&token=6a6ead11-30f7-4f76-b181-2fd62421d95e");

        postItem.setImages(hashmap);
        postItem.setThumbnail("testThumbNail");

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 이 내역들을 여기서 받는 이유는, btn_write.setOnClickListener 밖에서 받을 시 db 에 값이 저장되지 않는다.
                // 아마 "Fragment 생명 주기" 개념과 연관이 있지 않을까 추측.
                postItem.setTitle(edt_Title.getText().toString());
                postItem.setSubtitle(edt_subTitle.getText().toString());
                postItem.setContent(edt_content.getText().toString());

                mDatabaseRef.child("Post2").child("p7").setValue(postItem);

                Toast.makeText(getActivity(), "글 작성이 완료되었습니다", Toast.LENGTH_SHORT).show();

                // 글 작성이 완료되면, MainActivity 로 화면 이동
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });
    }

}
