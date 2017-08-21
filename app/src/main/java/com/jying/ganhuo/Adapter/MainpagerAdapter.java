package com.jying.ganhuo.Adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jying.ganhuo.Module.main.MainActivity;
import com.jying.ganhuo.R;

import java.util.List;

/**
 * Created by Jying on 2017/8/18.
 */

public class MainpagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragments;

    public MainpagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Resources res= MainActivity.instance.getResources();
        String tabs[]=res.getStringArray(R.array.tab_name);
        return tabs[position];
    }
}
