package com.ajou.ourvillage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;

    public TabPagerAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Tab의 position에 따라 화면 변환해주기
        switch (position) {
            case 0:
                MainFragment mainFragment = new MainFragment();
                return mainFragment;
            case 1:
                ApartFragment apartFragment = new ApartFragment();
                return apartFragment;
            case 2:
                PostFragment postFragment = new PostFragment();
                return postFragment;
            case 3:
                TastyFragment tastyFragment = new TastyFragment();
                return tastyFragment;
            case 4:
                MypageFragment mypageFragment = new MypageFragment();
                return mypageFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
