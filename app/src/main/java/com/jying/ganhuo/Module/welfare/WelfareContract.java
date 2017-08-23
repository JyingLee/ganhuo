package com.jying.ganhuo.Module.welfare;

import android.os.Handler;

import com.jying.ganhuo.Base.BasePresenter;
import com.jying.ganhuo.Base.BaseView;

/**
 * Created by Jying on 2017/8/23.
 */

public class WelfareContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getWelareData(Handler handler);
    }
}
