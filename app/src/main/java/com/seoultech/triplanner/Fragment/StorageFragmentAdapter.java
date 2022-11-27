package com.seoultech.triplanner.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

public class StorageFragmentAdapter extends FragmentPagerAdapter {
    public static int PAGE_POSITION = 3;

    public StorageFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            //2번째 탭
            case 1:
                return new StorageFragmentMyplan();
            //3번째 탭
            case 2:
                return new StorageFragmentLikes();

            //1번째 탭
            default:
                return new StorageFragmentMyPost();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
