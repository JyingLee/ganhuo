package com.jying.ganhuo.Module.ios;

import android.os.Handler;

import com.jying.ganhuo.Base.BasePresenter;
import com.jying.ganhuo.Base.BaseView;

/**
 * Created by Jying on 2017/8/22.
 */

public class IosContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getIosData(Handler handler);
    }
}
