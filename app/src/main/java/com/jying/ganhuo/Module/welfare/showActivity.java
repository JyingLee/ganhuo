package com.jying.ganhuo.Module.welfare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jying.ganhuo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jying on 2017/8/23.
 */

public class showActivity extends AppCompatActivity {
    @BindView(R.id.show_image)
    ImageView showImage;
    private String imageUrl;

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
                Toast.makeText(showActivity.this,"想保存?拒绝?!",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
