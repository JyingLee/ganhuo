package com.jying.ganhuo.Module.main;

/**
 * Created by Jying on 2017/8/16.
 */

public class MainPresenter implements MainContract.presenter {
    MainContract.View mView;

    public MainPresenter(MainContract.View mView) {
        this.mView = mView;
    }


    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }
}
