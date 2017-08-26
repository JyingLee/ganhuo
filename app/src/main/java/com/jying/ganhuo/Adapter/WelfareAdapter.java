package com.jying.ganhuo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jying.ganhuo.Bean.WelfareBean;
import com.jying.ganhuo.Module.welfare.showActivity;
import com.jying.ganhuo.R;

import java.util.List;

/**
 * Created by Jying on 2017/8/23.
 */

public class WelfareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<WelfareBean> lists;
    private static final int WELFARE = 1;
    private static final int FOOTVIEW=2;

    public WelfareAdapter(Context context, List<WelfareBean> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == WELFARE) {
            return new vh_welfare(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_welfare, parent, false));
        } else if (viewType == FOOTVIEW) {
            return new vh_footview(LayoutInflater.from(parent.getContext()).inflate(R.layout.footview,parent,false));
        }

        return null;
    }

    /**
     * DiskCacheStrategy.NONE 什么都不缓存
     * DiskCacheStrategy.SOURCE 只缓存最高解析图的image
     * DiskCacheStrategy.RESULT 缓存最后一次那个image,比如有可能你对image做了转化
     * DiskCacheStrategy.ALL image的所有版本都会缓存
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof vh_welfare) {
            vh_welfare vh = (vh_welfare) holder;
            vh.author.setText(lists.get(position).getImage_who());
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.shithub)//预加载图片
                    .error(R.mipmap.shithub)//加载失败显示图片
                    .priority(Priority.HIGH)//优先级
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);//缓存策略
            Glide.with(context)
                    .load(lists.get(position).getImage_url())
                    .thumbnail(0.1f)//表示为原图的十分之一
                    .apply(options).into(vh.image);

            vh.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, showActivity.class);
                    intent.putExtra("url", lists.get(position).getImage_url());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lists.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOTVIEW;
        }else {
            return WELFARE;
        }
    }

    public void addMoreDatas(List<WelfareBean> newDatas) {
        lists.addAll(newDatas);
        notifyDataSetChanged();
    }

    private class vh_welfare extends RecyclerView.ViewHolder {
        ImageView image;
        TextView author;

        public vh_welfare(View inflate) {
            super(inflate);
            image = (ImageView) inflate.findViewById(R.id.item_welfare_image);
            author = (TextView) inflate.findViewById(R.id.item_welfare_author);
        }
    }

    private class vh_footview extends RecyclerView.ViewHolder {
        public vh_footview(View inflate) {
            super(inflate);
        }
    }
}
