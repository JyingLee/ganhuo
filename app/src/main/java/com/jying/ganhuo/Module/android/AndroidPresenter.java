package com.jying.ganhuo.Module.android;

import android.os.Handler;
import android.os.Message;

import com.jying.ganhuo.Bean.AndroidBean;
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
 * Created by Jying on 2017/8/19.
 */

public class AndroidPresenter implements AndroidContract.Presenter {
    AndroidContract.View mView;


    public AndroidPresenter(AndroidContract.View mView) {
        this.mView=mView;
        mView.setPresenter(this);
    }


    @Override
    public void onStart() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void getAndroidData(final Handler handler) {
        final List<AndroidBean>list=new ArrayList<>();
        Retrofit retrofit=new Retrofit.Builder().baseUrl(Key.RANDOM_API).build();
        GanhuoService ganhuoService=retrofit.create(GanhuoService.class);
        Call<ResponseBody>call=ganhuoService.getData("Android",20);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json=response.body().string();
                    JSONObject jsonObject=new JSONObject(json);
                    JSONArray jsonArray=jsonObject.getJSONArray("results");
                    for (int i=0;i<jsonArray.length();i++){
                        AndroidBean bean=new AndroidBean();
                        JSONObject dataJson=jsonArray.getJSONObject(i);
                        bean.setDesc(dataJson.getString("desc"));
                        bean.setTime(dataJson.getString("createdAt"));
                        bean.setAuthor(dataJson.getString("who"));
                        bean.setUrl(dataJson.getString("url"));
                        if (dataJson.has("images")){
                            String image_url;
                            JSONArray urljason=new JSONArray(dataJson.getString("images"));
                            image_url= (String) urljason.opt(0);
                            bean.setImage_url(image_url);
                        }
                        list.add(bean);
                    }
                    Message msg=new Message();
                    msg.obj=list;
                    msg.what=1;
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
}
