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
import com.jying.ganhuo.Bean.IosBean;
import com.jying.ganhuo.Module.Web.WebActivity;
import com.jying.ganhuo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jying on 2017/8/22.
 */

public class IosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<IosBean> beans=new ArrayList<>();
    private static final int IOSDATA = 1;
    private static final int FOOTVIEW = 2;

    public IosAdapter(Context context, List<IosBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == IOSDATA) {
            return new vh_ios(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ios, parent, false));
        } else if (viewType == FOOTVIEW) {
            return new vh_footview(LayoutInflater.from(parent.getContext()).inflate(R.layout.footview, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof vh_ios) {
            final vh_ios vh = (vh_ios) holder;
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

    @Override
    public int getItemCount() {
        return beans.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOTVIEW;
        } else {
            return IOSDATA;
        }
    }

    public void addMoreDatas(List<IosBean> newDatas) {
        beans.addAll(newDatas);
        notifyDataSetChanged();
    }

    private class vh_ios extends RecyclerView.ViewHolder {
        ImageView image;
        TextView desc;
        TextView time;
        TextView author;
        LinearLayout layout;

        public vh_ios(View inflate) {
            super(inflate);
            image = (ImageView) inflate.findViewById(R.id.item_ios_image);
            desc = (TextView) inflate.findViewById(R.id.item_ios_desc);
            time = (TextView) inflate.findViewById(R.id.item_ios_time);
            author = (TextView) inflate.findViewById(R.id.item_ios_author);
            layout = (LinearLayout) inflate.findViewById(R.id.item_ios_layout);
        }
    }

    private class vh_footview extends RecyclerView.ViewHolder {
        public vh_footview(View inflate) {
            super(inflate);
        }
    }
}
