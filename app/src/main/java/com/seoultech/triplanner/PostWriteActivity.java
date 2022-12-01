package com.seoultech.triplanner;

import android.content.ClipData;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.seoultech.triplanner.Model.PostItem;
import com.seoultech.triplanner.Model.UserAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostWriteActivity extends AppCompatActivity {

    private final int GALLERY_CODE = 10;    // 갤러리에 접근하기 위해 선언한 임의의 코드 번호
    private final String TAG = "PostWriteActivity";

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
    PostItem postItem;

    Boolean inputTitle, inputSubtitle, inputRegion,
            inputPlace, inputContent;

    int colFontLight, colFontEmp, colBlue, colBG2;

    // 이미지 부분
    ArrayList<Uri> imgUriList = new ArrayList<>();   // 이미지의 uri를 담을 ArrayList 객체
    RecyclerView pw_recyclerView;
    MultiImageAdapter multiImageAdapter;        // RecyclerView 에 적용시킬 Adapter
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    final String imgStr = "imgurl";
    private static int imgCount = 1;
    public ArrayList<String> fbImgList = new ArrayList<>();   // fb Storage 에 업로드된 이미지 파일명을 담을 배열
    public static String staticPid;

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
        inputRegion = false;
        inputPlace = false;
        inputContent = false;

        // 갤러리로 이동
        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgGetIntent = new Intent(Intent.ACTION_PICK);
                imgGetIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                imgGetIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);   // 다중 이미지를 가져오도록 설정
                imgGetIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(imgGetIntent, GALLERY_CODE);
            }
        });
        pw_recyclerView = findViewById(R.id.pw_RecyclerView);

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(this,
                R.layout.writepost_spinner_item, Region) {
            @Override // 0번째 아이템 색 바꾸기
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;

                if (position == 0)
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

        // database 불러옴
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

                // 데이터 처리 후 pid column 값 설정
                String s = list.get(list.size() - 1);
                int num = Character.getNumericValue(s.charAt(1));
                staticPid = "post" + Integer.toString(num + 1);
                postItem.setPid(staticPid);

                // 이 내역들을 여기서 받는 이유는, btn_write.setOnClickListener 밖에서 받을 시 db 에 값이 저장되지 않는다.
                // 아마 "Fragment 생명 주기" 개념과 연관이 있지 않을까 추측.
                postItem.setTitle(edt_Title.getText().toString());
                postItem.setSubtitle(edt_subTitle.getText().toString());
                postItem.setContent(edt_content.getText().toString());

                uploadToFirebase(imgUriList);

//                for (int i = 0; i < fbImgList.size(); i++) {
//                    System.out.println("test : " + fbImgList.get(i));
//                }

                ArrayList<String> arrayList  = getFbStorageURL(fbImgList);
                fbImgList = null;   // static 변수이므로 null로 초기화 시켜줘야 함

                // 이거를 Intent로 MainActivity로 넘겨서, 거기서 처리하자
                postItem.setThumbnail(arrayList.get(0));   // 첫 번째 이미지는 썸네일
                HashMap<String, String> imgListMap = new HashMap<>();
                for (int i = 0; i < arrayList.size(); i++) {
                    imgListMap.put(imgStr + (i + 1), arrayList.get(i).toString());
                }
                postItem.setImages(imgListMap); // 이미지 배열 담기

                // db table 이름 설정
                String fbTableName = s.charAt(0) + Integer.toString(num + 1);

                // db에 값 넣기
                mDatabaseRef.child("Post2").child(fbTableName).setValue(postItem);

                // 글 작성이 완료되면, Storage > MyPost 로 이동
                Toast.makeText(getApplicationContext(), "글 작성이 완료되었습니다", Toast.LENGTH_SHORT).show();

                Intent intentHome = new Intent(PostWriteActivity.this, MainActivity.class);
                intentHome.putExtra("moveFragment", "storage_MyPost");
                startActivity(intentHome);

            }
        });
    }

    // 이미지 처리는 여기서 해야함
    // 앨범에서 액티비티로 돌아온 후 실행되는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode != Activity.RESULT_OK) {
//            return;
//        }

        if (data == null) {     // 이미지를 하나도 선택하지 않은 경우
            Toast.makeText(getApplicationContext(), "이미지를 하나 이상 선택해 주세요!",
                            Toast.LENGTH_SHORT).show();
        }
        else {  // 이미지를 최소 하나 이상 선택한 경우
            if (data.getClipData() == null) {   // 이미지를 하나만 선택한 경우
                Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                imgUriList.add(imageUri);

                multiImageAdapter = new MultiImageAdapter(imgUriList, getApplicationContext());
                pw_recyclerView.setAdapter(multiImageAdapter);
                pw_recyclerView.setLayoutManager(new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL, false));

            }
            else {  // 이미지를 여러 장 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if (clipData.getItemCount() >= 5) {  // 선택한 이미지가 5장 이상인 경우
                    Toast.makeText(getApplicationContext(), "사진은 4장까지 선택 가능합니다.",
                            Toast.LENGTH_LONG).show();
                }
                else {  // 선택한 이미지가 4장 이하 인 경우
                    Log.e("MultiImageActivity", "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri 를 가져온다.
                        try {
                            imgUriList.add(imageUri);
                        } catch (Exception e) {
                            Log.e("MultiImageActivity", "File Select Error!", e);
                        }
                    }

                    multiImageAdapter = new MultiImageAdapter(imgUriList, getApplicationContext());
                    pw_recyclerView.setAdapter(multiImageAdapter);
                    pw_recyclerView.setLayoutManager(new LinearLayoutManager(this,
                            LinearLayoutManager.HORIZONTAL, false));
                }

            }
        }

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

    // Firebase Storage 에 이미지 파일 업로드
    public void uploadToFirebase(ArrayList<Uri> arrayList) {
        UploadTask uploadTask;
        final Boolean[] imgUploadSucceed = {true};  // 아래 익명 클래스에서 사용하기 위해 final 배열로 선언

        for (int i = 0; i < arrayList.size(); i++) {

            String fileName = imgStr + imgCount + ".jpeg";
            StorageReference imgRef = mStorageRef.child(fileName);

            uploadTask = imgRef.putFile(arrayList.get(i));
            if (imgUploadSucceed[0]) {
                imgCount += 1;
            }

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Image Upload ", "Failed");
                    imgUploadSucceed[0] = false;
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e("Image Upload ", "Succeed");
                    imgUploadSucceed[0] = true;
                }
            });

            fbImgList.add(fileName);    // 여기서 파일 이름을 담는다.
        }
    }

    // 이미지 처리: Firebase 에 접근해서 Storage URL 정보 가지고 온다.
    // 문제: 이미지 업로드보다 이미지 url을 가져오는 것이 더 빨리 실행된다.
    public ArrayList<String> getFbStorageURL(ArrayList<String> arrayList) {

        ArrayList<String> urlList = new ArrayList<>();

        /* test 코드. 잘 작동함
        mStorageRef.child("att_2-2.jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println(uri+ ": uri");
                System.out.println(uri+ ": uri");
                System.out.println(uri+ ": uri");
            }
        }); */
        for (int i = 0; i < arrayList.size(); i++) {
            try {
                mStorageRef.child(arrayList.get(i)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        urlList.add(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            } catch(Exception e) {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
        }
        return urlList;
    }

}
