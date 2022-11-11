package com.seoultech.triplanner;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.seoultech.triplanner.Fragment.HomeFragment;
import com.seoultech.triplanner.Fragment.MypageFragment;
import com.seoultech.triplanner.Fragment.StorageFragment;
import com.seoultech.triplanner.Model.PlaceBannerItem;
import com.seoultech.triplanner.Model.PostItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("홈화면");

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new navigationItemSelectedListener());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new HomeFragment());
        transaction.commit();

    }

    class  navigationItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_planner:
                            selectedFragment = null;
                            startActivity(new Intent(MainActivity.this, RegionPlanner.class));
                            break;
                        case R.id.nav_storage:
                            selectedFragment = new StorageFragment();
                            break;
                        case R.id.nav_mypage:
                            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            //editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            //editor.apply();
                            selectedFragment = new MypageFragment();
                            break;
                    }

                    if (selectedFragment != null){
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.fragment_container, selectedFragment).commit();
                    }

                    return true;
                }
            };

}
