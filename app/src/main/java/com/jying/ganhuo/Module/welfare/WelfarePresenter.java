package com.jying.ganhuo.Module.welfare;

import android.os.Handler;
import android.os.Message;

import com.jying.ganhuo.Bean.WelfareBean;
import com.jying.ganhuo.Utils.GanhuoService;
import com.jying.ganhuo.Utils.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
    public void getWelareData(final Handler handler) {
        final List<WelfareBean> lists = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Key.RANDOM_API).build();
        GanhuoService ganhuoService = retrofit.create(GanhuoService.class);
        Call<ResponseBody> call = ganhuoService.getData("福利", 10);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        WelfareBean welfareBean = new WelfareBean();
                        JSONObject dealjson = jsonArray.getJSONObject(i);
                        welfareBean.setImage_url(dealjson.getString("url"));
                        welfareBean.setImage_who(dealjson.getString("who"));
                        lists.add(welfareBean);
                    }
                    Message message = new Message();
                    message.obj = lists;
                    message.what = 0;
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
}
