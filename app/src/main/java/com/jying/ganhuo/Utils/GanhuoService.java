package com.jying.ganhuo.Utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Jying on 2017/8/21.
 */

public interface GanhuoService {
    @GET("data/{type}/{count}")
    Call<ResponseBody>getData(@Path("type") String type,@Path("count") int count);
}
