package com.jying.ganhuo.Module.android;

import android.content.Context;
import android.os.Handler;

import com.jying.ganhuo.Base.BasePresenter;
import com.jying.ganhuo.Base.BaseView;
import com.jying.ganhuo.Bean.AndroidBean;

import java.util.List;

/**
 * Created by Jying on 2017/8/19.
 */

public class AndroidContract {
    interface View extends BaseView<Presenter>{

    }
    interface Presenter extends BasePresenter{
        void getAndroidData(Handler handler, List<AndroidBean> datas,int flag);
        boolean isNetworkAvailable(Context context);
    }
}
