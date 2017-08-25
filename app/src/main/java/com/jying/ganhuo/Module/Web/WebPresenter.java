package com.jying.ganhuo.Module.Web;

import android.content.Context;

import java.io.File;

/**
 * Created by Jying on 2017/8/22.
 */

public class WebPresenter implements WebContract.Presenter {
    WebContract.View mView;

    public WebPresenter(WebContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void clearWebViewCache(Context context) {
        //清理Webview缓存数据库
        try {
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(context.getFilesDir().getAbsolutePath() + "/webcache");

        File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath() + "/webviewCache");

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            context.deleteFile(String.valueOf(webviewCacheDir));
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            context.deleteFile(String.valueOf(appCacheDir));
        }
    }
}
