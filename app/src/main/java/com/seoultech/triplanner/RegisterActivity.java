package com.seoultech.triplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.seoultech.triplanner.Model.UserAccount;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스(서버 연동 객체)

    EditText et_reg_username, et_reg_Email, et_reg_PW;       // 회원가입 입력필드
    Button btn_register;                    // 회원가입 버튼

    TextView txt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Triplanner");

        et_reg_username = (EditText) findViewById(R.id.et_reg_Username);
        et_reg_Email = (EditText) findViewById(R.id.et_reg_Email);
        et_reg_PW = (EditText) findViewById(R.id.et_reg_PW);
        btn_register = (Button) findViewById(R.id.btn_register);

        txt_login = (TextView) findViewById(R.id.txt_login);

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // 회원가입 처리 시작

                String strUsername = et_reg_username.getText().toString();
                String strEmail = et_reg_Email.getText().toString();
                String strPW = et_reg_PW.getText().toString();

                // Firebase 의 경우 비밀번호를 6자리 이상 등록해야 오류가 나지 않음.
                if (TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPW)) {
                    Toast.makeText(RegisterActivity.this, "모든 양식을 채워주세요!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (strPW.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "비밀번호는 최소 6자리 이상으로 해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    btn_register.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5B4FBB")));
                    btn_register.setClickable(true);
                }

                // Firebase Auth 인증 처리 절차 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPW)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                // 가입 성공한 경우
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                    UserAccount userAccount = new UserAccount();
                                    userAccount.setFbEmail(firebaseUser.getEmail());
                                    userAccount.setFbPassword(strPW);
                                    //userAccount. username 데이터 입력 필요
                                    userAccount.setImageurl("https://firebasestorage.googleapis.com/v0/b/instagram-72e36.appspot.com/o/toolbar_8.jpg?alt=media&token=83118f66-19ca-4c28-975a-b276093be5dc");
                                    userAccount.setFbIdToken(firebaseUser.getUid());  // 고유값

                                    // setValue: database 에 insert 하는 행위
                                    mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(userAccount);
                                    Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            private void register(String username, String email, String password) {
                mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                    String userid = firebaseUser.getUid();

                                    //mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("id", userid);
                                    hashMap.put("username", username.toLowerCase());
                                    hashMap.put("bio", "");
                                    hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/instagram-72e36.appspot.com/o/toolbar_8.jpg?alt=media&token=83118f66-19ca-4c28-975a-b276093be5dc");

                                    mDatabaseRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(RegisterActivity.this, "해당 이메일로 가입할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}


    