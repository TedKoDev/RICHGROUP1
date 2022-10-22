package com.example.RG.mainfragment;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MainViewpagerAdapter extends FragmentPagerAdapter {

    private int page_count;

    public MainViewpagerAdapter(FragmentManager fm, int pageCount, int behavior) {
        super(fm, behavior);
        this.page_count = pageCount;
    }


    @Override
    public Fragment getItem(int position) {

        Log.e("TLPAD", "1");
        switch (position) {

            case 0:
                HomeFragment homeFragment = new HomeFragment();
                Log.e("TLPAD", "2");
                return homeFragment;

            case 1:
                SearchFragment searchFragment = new SearchFragment();
                Log.e("TLPAD", "3");
                return searchFragment;

            case 2:
                ChatFragment chatFragment = new ChatFragment();
                Log.e("TLPAD", "4");
                return chatFragment;

            case 3:
                InvestFragment investFragment = new InvestFragment();
                Log.e("TLPAD", "5");
                return investFragment;
            case 4:
                LiveFragment liveFragment = new LiveFragment();
                Log.e("TLPAD", "7");
                return liveFragment;
            case 5:

                MoreFragment moreFragment = new MoreFragment();
                Log.e("TLPAD", "6");
                return moreFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        Log.e("TLPAD", "7");
        return page_count;
    }


}

