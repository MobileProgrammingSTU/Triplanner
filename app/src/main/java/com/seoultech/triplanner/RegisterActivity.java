package com.seoultech.triplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.seoultech.triplanner.Model.UserAccount;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스(서버 연동 객체)

    EditText et_reg_Email, et_reg_PW;       // 회원가입 입력필드
    Button btn_register;                    // 회원가입 버튼

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Triplanner");

        et_reg_Email = (EditText) findViewById(R.id.et_reg_Email);
        et_reg_PW = (EditText)findViewById(R.id.et_reg_PW);
        btn_register = (Button) findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // 회원가입 처리 시작
                String strEmail = et_reg_Email.getText().toString();
                String strPW = et_reg_PW.getText().toString();

                // Firebase 의 경우 비밀번호를 6자리 이상 등록해야 오류가 나지 않음.
                if (strPW.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "비밀번호는 6글자 이상 입력해 주세요!",
                          Toast.LENGTH_SHORT).show();
                    return;
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
                            userAccount.setEmailId(firebaseUser.getEmail());
                            userAccount.setPasswd(strPW);
                            userAccount.setIdToken(firebaseUser.getUid());  // 고유값

                            // setValue: database 에 insert 하는 행위
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid())
                                    .setValue(userAccount);

                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }
}