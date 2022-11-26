package com.seoultech.triplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.seoultech.triplanner.Fragment.HomeFragment;
import com.seoultech.triplanner.Fragment.MyPageFragment;
import com.seoultech.triplanner.Fragment.PostWriteFragment;
import com.seoultech.triplanner.Fragment.StorageFragment;

public class MainActivity extends AppCompatActivity {
    NavigationBarView navigationBarView;
    Fragment selectedFragment = null;

    public static String moveFragmentMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("홈화면");

        navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setOnItemSelectedListener(new navigationItemSelectedListener());

        moveFragmentMainActivity = getIntent().getStringExtra("moveFragment");
        if (moveFragmentMainActivity != null) { // 다른 액티비티에서 날라온 프래그먼트 이동 정보가 있을 경우
            if (moveFragmentMainActivity.contains("storage")) { // 인텐트 정보가 "storage_XXXX"
                navigationBarView.setSelectedItemId(R.id.nav_storage);
            }
        }
        else
            loadFragment(new HomeFragment());

    }

    class navigationItemSelectedListener implements NavigationBarView.OnItemSelectedListener {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_postWrite:
                            selectedFragment = new PostWriteFragment();
                            break;
                        case R.id.nav_planner:
                            selectedFragment = null;
                            startActivity(new Intent(MainActivity.this, DatePlanner.class));
                            break;
                        case R.id.nav_storage:
                            selectedFragment = new StorageFragment();
                            break;
                        case R.id.nav_mypage:
                            selectedFragment = new MyPageFragment();
                            break;
                    }

                    if (selectedFragment != null){
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.fragment_container, selectedFragment).commit();
                        loadFragment(selectedFragment);
                    }

                    return true;
                }
            };

    private void loadFragment(Fragment fragment) {
        // load Fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
