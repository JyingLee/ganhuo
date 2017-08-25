package com.jying.ganhuo.Module.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.jying.ganhuo.Adapter.MainpagerAdapter;
import com.jying.ganhuo.Module.android.AndroidFragment;
import com.jying.ganhuo.Module.ios.IosFragment;
import com.jying.ganhuo.Module.video.VideoFragment;
import com.jying.ganhuo.Module.welfare.WelfareFragment;
import com.jying.ganhuo.R;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

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
    @BindView(R.id.image_bar)
    ImageView imageBar;
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        con = this;
        instance = this;
        setUpdate();
        ButterKnife.bind(this);
        mPresenter = new MainPresenter(this);
        initViewPager();
    }

    private void setUpdate() {
        PgyUpdateManager.setIsForced(false);
        PgyUpdateManager.register(MainActivity.this, "",
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        Toast.makeText(MainActivity.this,"又更新啦!",Toast.LENGTH_SHORT).show();
                        final AppBean appBean = getAppBeanFromString(result);
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("更新")
                                .setMessage("又是一个惊喜")
                                .setNegativeButton(
                                        "确定",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                startDownloadTask(
                                                        MainActivity.this,
                                                        appBean.getDownloadURL());
                                            }
                                        }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        Toast.makeText(MainActivity.this,"已经是最新版本",Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void initViewPager() {
        Resources res = getResources();
        String tabs[] = res.getStringArray(R.array.tab_name);
        tabLayout.addTab(tabLayout.newTab().setText(tabs[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabs[1]));
        tabLayout.addTab(tabLayout.newTab().setText(tabs[2]));
        tabLayout.addTab(tabLayout.newTab().setText(tabs[3]));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        fragments = new ArrayList<>();

        AndroidFragment androidFragment = AndroidFragment.getInstance(20);
        fragments.add(androidFragment);


        IosFragment iosFragment = IosFragment.newInstance(20);
        fragments.add(iosFragment);

        WelfareFragment welfareFragment = WelfareFragment.newInstance(10);
        fragments.add(welfareFragment);

        VideoFragment videoFragment = VideoFragment.newInstance(20);
        fragments.add(videoFragment);


        pagerAdapter = new MainpagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewpager);

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (viewpager.getCurrentItem()) {
                    case 0:
                        imageBar.setBackgroundResource(R.mipmap.c1);
                        break;
                    case 1:
                        imageBar.setBackgroundResource(R.mipmap.c2);
                        break;
                    case 2:
                        imageBar.setBackgroundResource(R.mipmap.c3);
                        break;
                    case 3:
                        imageBar.setBackgroundResource(R.mipmap.c4);
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setPresenter(MainContract.presenter presenter) {

    }

    @Override
    public void showToast(CharSequence msg) {
        Toast.makeText(con.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ( secondTime - firstTime < 2000) {
                System.exit(0);
            } else {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
