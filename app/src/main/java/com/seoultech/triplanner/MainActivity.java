package com.seoultech.triplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.seoultech.triplanner.Fragment.HomeFragment;
import com.seoultech.triplanner.Fragment.MyPageFragment;
import com.seoultech.triplanner.Fragment.StorageFragment;

public class MainActivity extends AppCompatActivity {
    NavigationBarView navigationBarView;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("홈화면");

        navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setOnItemSelectedListener(new navigationItemSelectedListener());

        loadFragment(new HomeFragment());

    }

    class navigationItemSelectedListener implements NavigationBarView.OnItemSelectedListener {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_planner:
                            selectedFragment = null;
                            startActivity(new Intent(MainActivity.this, DatePlanner.class));
                            break;
                        case R.id.nav_storage:
                            selectedFragment = new StorageFragment();
                            break;
                        case R.id.nav_mypage:
                            //SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            //editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            //editor.apply();
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

}
