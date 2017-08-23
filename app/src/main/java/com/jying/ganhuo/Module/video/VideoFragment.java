package com.jying.ganhuo.Module.video;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jying.ganhuo.Adapter.VideoAdapter;
import com.jying.ganhuo.Bean.VideoBean;
import com.jying.ganhuo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jying on 2017/8/23.
 */

public class VideoFragment extends Fragment implements VideoContract.View {
    VideoContract.Presenter presenter;
    private int count;
    RecyclerView videoRecyclewView;
    private List<VideoBean>lists=new ArrayList<>();
    private VideoAdapter videoAdapter;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    lists= (List<VideoBean>) msg.obj;
                    inirRecyclewView();
                    break;
            }
        }
    };

    private void inirRecyclewView() {
        videoAdapter=new VideoAdapter(lists,getActivity());
        videoRecyclewView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        videoRecyclewView.setItemAnimator(new DefaultItemAnimator());
        videoRecyclewView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        videoRecyclewView.setAdapter(videoAdapter);
    }

    public static VideoFragment newInstance(int count) {
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("count", count);
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            count=getArguments().getInt("count");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter=new VideoPresenter(this);
        View view=inflater.inflate(R.layout.fragment_video,container,false);
        videoRecyclewView= (RecyclerView) view.findViewById(R.id.video_recyclewview);
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.getVideoData(handler);
            }
        }).start();
        return view;
    }

    @Override
    public void setPresenter(VideoContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void showToast(CharSequence msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

}
