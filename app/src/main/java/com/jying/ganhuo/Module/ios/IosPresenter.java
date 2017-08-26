package com.jying.ganhuo.Module.ios;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.jying.ganhuo.Bean.IosBean;
import com.jying.ganhuo.Utils.GanhuoService;
import com.jying.ganhuo.Utils.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Jying on 2017/8/22.
 */

public class IosPresenter implements IosContract.Presenter {
    IosContract.View mView;

    public IosPresenter(IosContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void getIosData(final Handler handler, final List<IosBean> iosBeans, final int flag) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Key.RANDOM_API).build();
        GanhuoService ganhuoService = retrofit.create(GanhuoService.class);
        Call<ResponseBody> call = ganhuoService.getData("iOS", 8);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            IosBean bean = new IosBean();
                            JSONObject dataJson = jsonArray.getJSONObject(i);
                            bean.setDesc(dataJson.getString("desc"));
                            bean.setTime(dataJson.getString("createdAt"));
                            bean.setAuthor(dataJson.getString("who"));
                            bean.setUrl(dataJson.getString("url"));
                            if (dataJson.has("images")) {
                                String image_url;
                                JSONArray urljason = new JSONArray(dataJson.getString("images"));
                                image_url = (String) urljason.opt(0);
                                bean.setImage_url(image_url);
                            }
                            iosBeans.add(bean);
                        }
                        Message msg = new Message();
                        msg.obj = iosBeans;
                        msg.what = 0;
                        Bundle bundle = new Bundle();
                        bundle.putInt("ios_flag", flag);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mView.showToast("没网络了知道不?!");
            }
        });
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
