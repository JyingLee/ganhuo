package com.jying.ganhuo.Module.android;

/**
 * Created by Jying on 2017/8/19.
 */

public class AndroidPresenter implements AndroidContract.Presenter {
    AndroidContract.View mView;


    public AndroidPresenter(AndroidContract.View mView) {
        this.mView=mView;
        mView.setPresenter(this);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }
}
