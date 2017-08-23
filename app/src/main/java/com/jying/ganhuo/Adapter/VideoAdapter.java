package com.jying.ganhuo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jying.ganhuo.Bean.VideoBean;
import com.jying.ganhuo.Module.Web.WebActivity;
import com.jying.ganhuo.R;

import java.util.List;

/**
 * Created by Jying on 2017/8/23.
 */

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<VideoBean> lists;
    Context context;
    private static final int VIDEO = 1;

    public VideoAdapter(List<VideoBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIDEO) {
            return new vh_video(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof vh_video) {
            vh_video vh = (vh_video) holder;
            vh.author.setText("提供者:"+lists.get(position).getAuthor());
            vh.time.setText(lists.get(position).getTime());
            vh.desc.setText(lists.get(position).getDesc());
            vh.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("url", lists.get(position).getUrl());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIDEO;
    }

    private class vh_video extends RecyclerView.ViewHolder {
        TextView author;
        TextView time;
        TextView desc;
        LinearLayout layout;

        public vh_video(View inflate) {
            super(inflate);
            author = (TextView) inflate.findViewById(R.id.item_video_author);
            time = (TextView) inflate.findViewById(R.id.item_video_time);
            desc = (TextView) inflate.findViewById(R.id.item_video_desc);
            layout = (LinearLayout) inflate.findViewById(R.id.item_video_layout);
        }
    }
}
