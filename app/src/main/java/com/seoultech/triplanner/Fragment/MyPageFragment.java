package com.seoultech.triplanner.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seoultech.triplanner.LoginActivity;
import com.seoultech.triplanner.Model.CustomNormalDialog;
import com.seoultech.triplanner.Model.CustomNormalDialogClickListener;
import com.seoultech.triplanner.Model.UserAccount;
import com.seoultech.triplanner.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPageFragment extends Fragment {

    CircleImageView myPage_profile_img;
    TextView myPage_userName, myPage_regDate;
    Button btn_logout, btn_quit;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final String fbCurrentUserUID = mFirebaseAuth.getUid();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        myPage_profile_img = (CircleImageView) view.findViewById(R.id.myPage_profile_img);
        myPage_userName = (TextView) view.findViewById(R.id.myPage_userName);
        myPage_regDate = (TextView) view.findViewById(R.id.et_reg_Email);

        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        btn_quit = (Button) view.findViewById(R.id.btn_quit);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference("Triplanner");    // DB table connect

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
                        "??????????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();

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
    Realtime db ?????? user ????????? ???????????? View ??? ???????????? ??????
     */

    private void setProfile() {

        assert fbCurrentUserUID != null;
        mDatabaseRef.child("UserAccount").child(fbCurrentUserUID).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserAccount userAccount = snapshot.getValue(UserAccount.class);
                        myPage_userName.setText(userAccount.getFbName());
                        myPage_regDate.setText(userAccount.getFbRegDate());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }

    /*
    ???????????? ??? AlertDialog ?????? ????????? ?????? ??? ??? ?????????.
     */
    private void showQuitDialog() {
        CustomNormalDialog dlg = new CustomNormalDialog(getContext(), "?????? ??????",
                "????????? ?????????????????????????", new CustomNormalDialogClickListener() {
            @Override
            public void onPositiveClick() {
                // Firebase Authentication ????????? ????????????, Realtime db ????????? ???????????? ??????.
                Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).delete();

                // Realtime db ????????? ??????
                assert fbCurrentUserUID != null;
                mDatabaseRef.child("UserAccount").child(fbCurrentUserUID).removeValue();

                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                        "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onNegativeClick() {

            }
        });
        dlg.setCanceledOnTouchOutside(true);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.show();
    }

}