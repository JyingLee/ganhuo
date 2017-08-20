package com.jying.ganhuo.Base;

/**
 * Created by Jying on 2017/8/16.
 */

public interface BaseView <P>{
    void setPresenter(P presenter);

    void showToast(CharSequence msg);
}
