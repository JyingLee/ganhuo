package com.jying.ganhuo.Module.video;

import android.content.Context;
import android.os.Handler;

import com.jying.ganhuo.Base.BasePresenter;
import com.jying.ganhuo.Base.BaseView;
import com.jying.ganhuo.Bean.VideoBean;

import java.util.List;

/**
 * Created by Jying on 2017/8/23.
 */

public class VideoContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getVideoData(Handler handler, List<VideoBean> videoDatas, int flag);

        boolean isNetworkAvailable(Context context);
    }
}
