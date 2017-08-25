package com.jying.ganhuo.Module.Web;

import android.content.Context;

import com.jying.ganhuo.Base.BasePresenter;
import com.jying.ganhuo.Base.BaseView;

/**
 * Created by Jying on 2017/8/22.
 */

public interface WebContract {

    interface View extends BaseView<Presenter>{

    }
    interface Presenter extends BasePresenter{
        void clearWebViewCache(Context context);
    }
}
