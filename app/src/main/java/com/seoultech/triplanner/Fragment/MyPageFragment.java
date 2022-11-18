package com.seoultech.triplanner.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seoultech.triplanner.LoginActivity;
import com.seoultech.triplanner.Model.UserAccount;
import com.seoultech.triplanner.R;

import java.util.Objects;

public class MyPageFragment extends Fragment {

    ImageView myPage_profile;
    TextView myPage_userName, myPage_regDate;
    Button btn_logout, btn_quit;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final String fbCurrentUserUID = mFirebaseAuth.getUid();
    private DatabaseReference mDatabaseRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        myPage_profile = (ImageView) view.findViewById(R.id.myPage_profile);
        myPage_userName = (TextView) view.findViewById(R.id.myPage_userName);
        myPage_regDate = (TextView) view.findViewById(R.id.et_reg_Email);

        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        btn_quit = (Button) view.findViewById(R.id.btn_quit);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Triplanner");

        setViewPager();

        return view;
    }

    private void setViewPager() {
        setProfile();

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();    // Logout

                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                        "정상적으로 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuitDialog();
            }
        });
    }

    /*
    Realtime db 에서 user 정보를 가져와서 View 에 보여주는 역할
     */

    private void setProfile() {

        assert fbCurrentUserUID != null;
        mDatabaseRef.child("UserAccount").child(fbCurrentUserUID).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserAccount userAccount = snapshot.getValue(UserAccount.class);
                        myPage_userName.setText("닉네임: " + userAccount.getFbName());
                        myPage_regDate.setText("가입 일자: " + userAccount.getFbRegDate());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }

    /*
    회원탈퇴 전 AlertDialog 창을 띄워서 다시 한 번 묻는다.
     */
    private void showQuitDialog() {

        AlertDialog.Builder dlg = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        dlg.setTitle("회원 탈퇴");
        dlg.setMessage("정말로 탈퇴하시겠습니까?");
        dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Firebase Authentication 에서는 제거되나, Realtime db 에서는 제거되지 않음.
                Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).delete();

                // Realtime db 에서도 제거
                assert fbCurrentUserUID != null;
                mDatabaseRef.child("UserAccount").child(fbCurrentUserUID).removeValue();

                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                        "정상적으로 탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        dlg.setNegativeButton("아니오", null);
        dlg.show();
    }

}