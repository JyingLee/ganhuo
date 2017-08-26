package com.jying.ganhuo.Module.welfare;

import android.content.Context;
import android.os.Handler;

import com.jying.ganhuo.Base.BasePresenter;
import com.jying.ganhuo.Base.BaseView;
import com.jying.ganhuo.Bean.WelfareBean;

import java.util.List;

/**
 * Created by Jying on 2017/8/23.
 */

public class WelfareContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getWelareData(Handler handler, List<WelfareBean>lists,int flag);
        boolean isNetworkAvailable(Context context);
        int getArrayMax(int []position);
    }
}
