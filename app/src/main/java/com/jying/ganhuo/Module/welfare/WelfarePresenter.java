package com.jying.ganhuo.Module.welfare;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.jying.ganhuo.Bean.WelfareBean;
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
 * Created by Jying on 2017/8/23.
 */

public class WelfarePresenter implements WelfareContract.Presenter {
    WelfareContract.View mView;

    public WelfarePresenter(WelfareContract.View mView) {
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
    public void getWelareData(final Handler handler, final List<WelfareBean> welfareDatas, final int flag) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Key.RANDOM_API).build();
        GanhuoService ganhuoService = retrofit.create(GanhuoService.class);
        Call<ResponseBody> call = ganhuoService.getData("福利", 6);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response == null) return;
                    String json = response.body().string();
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        WelfareBean welfareBean = new WelfareBean();
                        JSONObject dealjson = jsonArray.getJSONObject(i);
                        welfareBean.setImage_url(dealjson.getString("url"));
                        welfareBean.setImage_who(dealjson.getString("who"));
                        welfareDatas.add(welfareBean);
                    }
                    Message message = new Message();
                    message.obj = welfareDatas;
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putInt("welfare_flag", flag);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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

    @Override
    public int getArrayMax(int[] position) {
        int max = position[0];
        for (int value : position) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
