package com.seoultech.triplanner;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
    static HomeFragment homeFragment = new HomeFragment();
    static StorageFragment storageFragment = new StorageFragment();

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
                            selectedFragment = homeFragment;
                            break;
                        case R.id.nav_postWrite:
                            selectedFragment = new PostWriteFragment();
                            break;
                        case R.id.nav_planner:
                            selectedFragment = null;
                            startActivity(new Intent(MainActivity.this, DatePlanner.class));
                            break;
                        case R.id.nav_storage:
                            selectedFragment = storageFragment;
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

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
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
