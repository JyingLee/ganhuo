package com.jying.ganhuo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jying.ganhuo.Bean.AndroidBean;
import com.jying.ganhuo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jying on 2017/8/20.
 */

public class AndroidAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ANDROID = 1;
    List<AndroidBean> beans = new ArrayList<>();
    Context context;
    public AndroidAdapter(Context context,List<AndroidBean> beans) {
        this.beans = beans;
        this.context=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ANDROID) {
            return new vh_android(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_android, parent, false));
        }
        else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof vh_android) {
            final vh_android vh = (vh_android) holder;
            vh.desc.setText(beans.get(position).getDesc());
            vh.author.setText(beans.get(position).getAuthor());
            vh.time.setText(beans.get(position).getTime());
        }
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ANDROID;
    }

    private class vh_android extends RecyclerView.ViewHolder {
        ImageView image;
        TextView desc;
        TextView time;
        TextView author;

        public vh_android(View inflate) {
            super(inflate);
            image = (ImageView) inflate.findViewById(R.id.item_android_image);
            desc = (TextView) inflate.findViewById(R.id.item_android_desc);
            time = (TextView) inflate.findViewById(R.id.item_android_time);
            author = (TextView) inflate.findViewById(R.id.item_android_author);
        }
    }
}
