package com.seoultech.triplanner.Fragment;

import static com.seoultech.triplanner.Fragment.StorageFragmentAdapter.PAGE_POSITION;
import static com.seoultech.triplanner.MainActivity.moveFragmentMainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.seoultech.triplanner.R;

@SuppressWarnings("deprecation")
public class StorageFragment extends Fragment {
    TabLayout tabs;
    ViewPager viewPagerStorageFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage, container, false);

        tabs = (TabLayout) view.findViewById(R.id.tabs);
        viewPagerStorageFragment = (ViewPager) view.findViewById(R.id.viewPager);

        setViewPager();

        if (moveFragmentMainActivity != null) {
            if (moveFragmentMainActivity.equals("storage_plan")) {
                viewPagerStorageFragment.setCurrentItem(1); // 내플랜으로 이동
            }
            else if (moveFragmentMainActivity.equals("storage_MyPost")) {
                viewPagerStorageFragment.setCurrentItem(0); // 내가 쓴 게시글로 이동
            }
        }

        return view;
    }

    private void setViewPager() {
        //어댑터 초기화
        StorageFragmentAdapter adapter = new StorageFragmentAdapter(getChildFragmentManager(), PAGE_POSITION);
        //어댑터 연결
        viewPagerStorageFragment.setAdapter(adapter);
        //페이지 리스너 (viewPager와 TabLayout의 페이지를 맞춰줌)
        viewPagerStorageFragment.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        //탭 선택 리스너 (탭 행동 설정)
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //선택된 탭일 때
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //선택된 탭과 연결된 fragment를 가져옴
                viewPagerStorageFragment.setCurrentItem(tab.getPosition());
                //아이콘 색상을 흰색으로 설정
                //tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }
            //선택되지 않은 탭일 때
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //아이콘 색상을 #0070C0 으로 설정
                //tab.getIcon().setColorFilter(Color.parseColor("#0070C0"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}