package com.seoultech.triplanner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StorageFragmentAdapter extends FragmentPagerAdapter {
    public static int PAGE_POSITION = 3;

    public StorageFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            //1번째 탭
            case 0:
                return StorageFragmentMypost.newInstance();
            //2번째 탭
            case 1:
                return StorageFragmentMyplan.newInstance();
            //3번째 탭
            case 2:
                return StorageFragmentLikes.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
