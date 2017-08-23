package com.jying.ganhuo.Adapter;

import android.content.Context;
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
import com.jying.ganhuo.R;

import java.util.List;

/**
 * Created by Jying on 2017/8/23.
 */

public class WelfareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<WelfareBean> lists;
    private static final int WELFARE = 1;

    public WelfareAdapter(Context context, List<WelfareBean> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == WELFARE) {
            return new vh_welfare(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_welfare, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof vh_welfare) {
            vh_welfare vh= (vh_welfare) holder;
            vh.author.setText(lists.get(position).getImage_who());
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.shithub)//预加载图片
                    .error(R.mipmap.shithub)//加载失败显示图片
                    .priority(Priority.HIGH)//优先级
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);//缓存策略
            Glide.with(context).load(lists.get(position).getImage_url()).apply(options).into(vh.image);
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return WELFARE;
    }

    private class vh_welfare extends RecyclerView.ViewHolder {
        ImageView image;
        TextView author;
        public vh_welfare(View inflate) {
            super(inflate);
            image= (ImageView) inflate.findViewById(R.id.item_welfare_image);
            author= (TextView) inflate.findViewById(R.id.item_welfare_author);
        }
    }
}
