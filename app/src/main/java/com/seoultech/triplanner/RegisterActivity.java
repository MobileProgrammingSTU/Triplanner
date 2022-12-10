package com.seoultech.triplanner;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.seoultech.triplanner.Model.UserAccount;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스(서버 연동 객체)

    EditText et_reg_Username, et_reg_Email, et_reg_PW;       // 회원가입 입력필드
    Button btn_register;                    // 회원가입 버튼

    TextView txt_login;

    boolean isUnameFilled, isEmailFilled, isPwFilled;

    int colBlue, colFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Triplanner");

        et_reg_Username = (EditText) findViewById(R.id.et_reg_Username);
        et_reg_Email = (EditText) findViewById(R.id.et_reg_Email);
        et_reg_PW = (EditText) findViewById(R.id.et_reg_PW);
        btn_register = (Button) findViewById(R.id.btn_register);

        txt_login = (TextView) findViewById(R.id.txt_login);

        colBlue = ContextCompat.getColor(getApplicationContext(), R.color.colorBrandBlue);
        colFont = ContextCompat.getColor(getApplicationContext(), R.color.colorFontEmphasis);

        // 오늘 날짜를 입력받도록
        String strRegDate = new SimpleDateFormat("yyyy년 MM월 dd일").format(new Date());

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        // editText 채워졌는지 확인
        et_reg_Username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                isUnameFilled = editable.length() > 0;
                if(isUnameFilled && isEmailFilled && isPwFilled) {
                    btn_register.setBackgroundTintList(ColorStateList.valueOf(colBlue));
                    btn_register.setEnabled(true);
                    btn_register.setTextColor(colFont);
                }
            }
        });
        et_reg_PW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                isPwFilled = editable.length() > 5;
                if(isUnameFilled && isEmailFilled && isPwFilled) {
                    btn_register.setBackgroundTintList(ColorStateList.valueOf(colBlue));
                    btn_register.setEnabled(true);
                    btn_register.setTextColor(colFont);
                }
            }
        });
        et_reg_Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                isEmailFilled = editable.length() > 0;
                if(isUnameFilled && isEmailFilled && isPwFilled) {
                    btn_register.setBackgroundTintList(ColorStateList.valueOf(colBlue));
                    btn_register.setEnabled(true);
                    btn_register.setTextColor(colFont);
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 회원가입 처리 시작
                String strUsername = et_reg_Username.getText().toString();
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
                            userAccount.setFbName(strUsername);
                            userAccount.setFbRegDate(strRegDate);
                            userAccount.setFbIdToken(firebaseUser.getUid());  // 고유값

                            //userAccount.setImageurl("https://firebasestorage.googleapis.com/v0/b/instagram-72e36.appspot.com/o/toolbar_8.jpg?alt=media&token=83118f66-19ca-4c28-975a-b276093be5dc");


                            // setValue: database 에 insert 하는 행위
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(userAccount);

                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
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