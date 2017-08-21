package com.jying.ganhuo.Module.android;

import android.os.Handler;

import com.jying.ganhuo.Base.BasePresenter;
import com.jying.ganhuo.Base.BaseView;

/**
 * Created by Jying on 2017/8/19.
 */

public class AndroidContract {
    interface View extends BaseView<Presenter>{

    }
    interface Presenter extends BasePresenter{
        void getAndroidData(Handler handler);
    }
}
