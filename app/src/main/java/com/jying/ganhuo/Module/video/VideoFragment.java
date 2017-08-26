package com.jying.ganhuo.Module.video;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private List<VideoBean> videoDatas = new ArrayList<>();
    private VideoAdapter videoAdapter;
    private SwipeRefreshLayout refreshLayout;
    private boolean isRolling = false;
    private boolean isLoading = false;
    private int lastVisibleItemPosition;
    private boolean isInit = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    int flag = msg.getData().getInt("video_flag");
                    switch (flag) {
                        case 0:
                            videoDatas = (List<VideoBean>) msg.obj;
                            initRecyclewView();
                            break;
                        case 1:
                            if (isInit==true) {
                                videoDatas = (List<VideoBean>) msg.obj;
                                if (videoDatas != null) {
                                    videoAdapter.notifyDataSetChanged();
                                    showToast("已更新数据");
                                }
                                isRolling = false;
                                setRecyclewViewBug();
                                refreshLayout.setRefreshing(false);
                            }else {
                                presenter.getVideoData(handler,videoDatas,0);
                                isRolling = false;
                                setRecyclewViewBug();
                                refreshLayout.setRefreshing(false);
                            }
                            break;
                        case 2:
                            List<VideoBean> newDatas = (List<VideoBean>) msg.obj;
                            videoAdapter.addMoreDatas(newDatas);
                            videoAdapter.notifyItemRemoved(videoAdapter.getItemCount());
                            break;
                    }
                    break;
            }
        }
    };

    private void initRecyclewView() {
        videoAdapter = new VideoAdapter(videoDatas, getActivity());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        videoRecyclewView.setLayoutManager(layoutManager);
        videoRecyclewView.setItemAnimator(new DefaultItemAnimator());
        videoRecyclewView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        videoRecyclewView.setAdapter(videoAdapter);
        videoRecyclewView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == videoAdapter.getItemCount()) {

                    boolean isRefreshing = refreshLayout.isRefreshing();
                    if (isRefreshing) {
                        videoAdapter.notifyItemRemoved(videoAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                List<VideoBean> newDatas = new ArrayList<>();
                                presenter.getVideoData(handler, newDatas, 2);
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            }
        });
        isInit = true;
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
        if (getArguments() != null) {
            count = getArguments().getInt("count");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter = new VideoPresenter(this);
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        videoRecyclewView = (RecyclerView) view.findViewById(R.id.video_recyclewview);
        initrefreshlayout(view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.getVideoData(handler, videoDatas, 0);
            }
        }).start();
        return view;
    }

    private void initrefreshlayout(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.video_fresh);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                if (presenter.isNetworkAvailable(getActivity())) {
                    isRolling = true;
                    setRecyclewViewBug();
                    videoDatas.clear();
                    presenter.getVideoData(handler, videoDatas, 1);
                } else {
                    refreshLayout.setRefreshing(false);
                    showToast("没网络了");
                }
            }
        });
    }

    @Override
    public void setPresenter(VideoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showToast(CharSequence msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void setRecyclewViewBug() {
        videoRecyclewView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isRolling) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
