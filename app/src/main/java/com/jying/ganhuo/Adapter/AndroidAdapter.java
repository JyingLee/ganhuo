package com.jying.ganhuo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jying.ganhuo.Bean.AndroidBean;
import com.jying.ganhuo.Module.Web.WebActivity;
import com.jying.ganhuo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jying on 2017/8/20.
 */

public class AndroidAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ANDROID = 1;
    private static final int FOOTVIEW = 2;
    private static final int NOTSEE=1;
    private static final int ISLOADING=2;
    private int load_state;
    List<AndroidBean> beans = new ArrayList<>();
    Context context;

    public AndroidAdapter(Context context, List<AndroidBean> beans) {
        this.beans = beans;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ANDROID) {
            return new vh_android(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_android, parent, false));
        } else if (viewType == FOOTVIEW) {
            return new vh_footview(LayoutInflater.from(parent.getContext()).inflate(R.layout.footview, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof vh_android) {
            final vh_android vh = (vh_android) holder;
            vh.desc.setText(beans.get(position).getDesc());
            vh.author.setText(beans.get(position).getAuthor());
            vh.time.setText(beans.get(position).getTime());
            if (beans.get(position).getImage_url() != null) {
                Glide.with(context).load(beans.get(position).getImage_url()).into(vh.image);
            } else {
                vh.image.setVisibility(View.GONE);
            }
            vh.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = beans.get(position).getUrl();
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("url", url);
                    context.startActivity(intent);
                }
            });
        }
    }

    public void addMoreData(List<AndroidBean>newDatas){
        beans.addAll(newDatas);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return beans.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOTVIEW;
        } else {
            return ANDROID;
        }
    }

    private class vh_android extends RecyclerView.ViewHolder {
        ImageView image;
        TextView desc;
        TextView time;
        TextView author;
        LinearLayout layout;

        public vh_android(View inflate) {
            super(inflate);
            image = (ImageView) inflate.findViewById(R.id.item_android_image);
            desc = (TextView) inflate.findViewById(R.id.item_android_desc);
            time = (TextView) inflate.findViewById(R.id.item_android_time);
            author = (TextView) inflate.findViewById(R.id.item_android_author);
            layout = (LinearLayout) inflate.findViewById(R.id.item_android_layout);
        }
    }

    private class vh_footview extends RecyclerView.ViewHolder {
        TextView state;
        LinearLayout foot_layout;
        public vh_footview(View inflate) {
            super(inflate);
            state= (TextView) inflate.findViewById(R.id.footview_tv);
            foot_layout= (LinearLayout) inflate.findViewById(R.id.foot_layout);
        }
    }
}
