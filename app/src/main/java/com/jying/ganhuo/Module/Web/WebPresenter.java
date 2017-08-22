package com.jying.ganhuo.Module.Web;

/**
 * Created by Jying on 2017/8/22.
 */

public class WebPresenter implements WebContract.Presenter {
    WebContract.View mView;

    public WebPresenter(WebContract.View mView){
        this.mView=mView;
    }
    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }
}
