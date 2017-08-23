package com.jying.ganhuo.Module.video;

import android.os.Handler;
import android.os.Message;

import com.jying.ganhuo.Bean.VideoBean;
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

public class VideoPresenter implements VideoContract.Presenter {
    VideoContract.View mView;

    public VideoPresenter(VideoContract.View mView) {
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
    public void getVideoData(final Handler handler) {
        final List<VideoBean> lists = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Key.RANDOM_API).build();
        GanhuoService ganhuoService = retrofit.create(GanhuoService.class);
        Call<ResponseBody> call = ganhuoService.getData("休息视频", 20);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    if (json==null)return;
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject dealData = jsonArray.getJSONObject(i);
                        VideoBean data = new VideoBean();
                        data.setAuthor(dealData.getString("who"));
                        data.setDesc(dealData.getString("desc"));
                        data.setTime(dealData.getString("createdAt"));
                        data.setUrl(dealData.getString("url"));
                        lists.add(data);
                    }
                    Message message = new Message();
                    message.what = 0;
                    message.obj = lists;
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
