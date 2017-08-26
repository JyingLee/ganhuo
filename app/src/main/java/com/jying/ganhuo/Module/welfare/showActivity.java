package com.jying.ganhuo.Module.welfare;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jying.ganhuo.R;
import com.jying.ganhuo.Utils.FileUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jying on 2017/8/23.
 */

public class showActivity extends AppCompatActivity {
    @BindView(R.id.show_image)
    ImageView showImage;
    private String imageUrl;
    @BindView(R.id.show_down)
    ImageButton down;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimage);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        imageUrl = bundle.getString("url");



        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.shithub)//预加载图片
                .error(R.mipmap.shithub)//加载失败显示图片
                .priority(Priority.HIGH)//优先级
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);//缓存策略
        Glide.with(this)
                .load(imageUrl)
                .thumbnail(0.1f)//表示为原图的十分之一
                .apply(options)
                .into(showImage);
        showImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });
        showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download(imageUrl);
                down.setEnabled(false);
            }
        });
    }

    // 保存图片到手机
    public void download(final String url) {
        new AsyncTask<Void, Integer, File>() {
            @Override
            protected File doInBackground(Void... params) {
                File file = null;
                try {
                    FutureTarget<File> future = Glide
                            .with(showActivity.this)
                            .load(url)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

                    file = future.get();

                    // 首先保存图片
                    File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();

                    File appDir = new File(pictureFolder, "Ganhuo");
                    if (!appDir.exists()) {
                        appDir.mkdirs();
                    }
                    String fileName = System.currentTimeMillis() + ".jpg";
                    File destFile = new File(appDir, fileName);

                    FileUtil.copy(file, destFile);

                    // 最后通知图库更新
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(new File(destFile.getPath()))));
                } catch (Exception e) {
                }
                return file;
            }

            @Override
            protected void onPostExecute(File file) {
                Toast.makeText(showActivity.this, "已保存图片", Toast.LENGTH_SHORT).show();
                down.setVisibility(View.GONE);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }
        }.execute();
    }

}
