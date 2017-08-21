package com.jying.ganhuo.Module.main;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jying.ganhuo.Adapter.MainpagerAdapter;
import com.jying.ganhuo.Module.android.AndroidFragment;
import com.jying.ganhuo.Module.android.testFragment;
import com.jying.ganhuo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    public static MainActivity instance = null;
    MainContract.presenter mPresenter;
    Context con;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private List<Fragment> fragments;
    MainpagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        con = this;
        instance = this;
        ButterKnife.bind(this);
        mPresenter = new MainPresenter(this);
        initViewPager();
    }

    private void initViewPager() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        Resources res = getResources();
        String tabs[] = res.getStringArray(R.array.tab_name);
        tabLayout.addTab(tabLayout.newTab().setText(tabs[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabs[1]));
        tabLayout.addTab(tabLayout.newTab().setText(tabs[2]));
        tabLayout.addTab(tabLayout.newTab().setText(tabs[3]));
        tabLayout.setTabTextColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        fragments = new ArrayList<>();
        AndroidFragment androidFragment = AndroidFragment.getInstance(1);
        fragments.add(androidFragment);

        testFragment test=testFragment.getInstance(2);
        fragments.add(test);


        pagerAdapter = new MainpagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewpager);

    }

    @Override
    public void setPresenter(MainContract.presenter presenter) {

    }

    @Override
    public void showToast(CharSequence msg) {
        Toast.makeText(con.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
