package com.jying.ganhuo.Module.android;

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

import com.jying.ganhuo.Adapter.AndroidAdapter;
import com.jying.ganhuo.Bean.AndroidBean;
import com.jying.ganhuo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Jying on 2017/8/19.
 */

public class AndroidFragment extends Fragment implements AndroidContract.View {
    AndroidContract.Presenter presenter;
    private int count;
    RecyclerView recyclerView;
    AndroidAdapter androidAdapter;
    List<AndroidBean> androidData = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private boolean isLoading = false;
    private int lastVisibleItemPosition;//最后一个可见的item
    private boolean isRolling=false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    int flag = msg.getData().getInt("flag");  //0：刚进界面显示的数据，1：下拉刷新的数据，2上拉加载更多的数据
                    switch (flag) {
                        case 0:
                            androidData = (List<AndroidBean>) msg.obj;
                            initRecyclewView();
                            break;
                        case 1:
                            androidData = (List<AndroidBean>) msg.obj;
                            if (androidData != null) {
                                androidAdapter.notifyDataSetChanged();
                                showToast("已更新数据");
                            }
                            isRolling=false;
                            setRecyclewViewBug();
                            refreshLayout.setRefreshing(false);
                            break;
                        case 2:
                            List<AndroidBean>newDaras=(List<AndroidBean>) msg.obj;
                            androidAdapter.addMoreData(newDaras);
                            androidAdapter.notifyItemRemoved(androidAdapter.getItemCount());
                            break;
                    }

                    break;
            }
        }
    };

    public static AndroidFragment getInstance(int count) {
        AndroidFragment androidFragment = new AndroidFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("count", count);
        androidFragment.setArguments(bundle);
        return androidFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            count = getArguments().getInt("count");
        }
    }

    private void initRecyclewView() {
        androidAdapter = new AndroidAdapter(getActivity(), androidData);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(androidAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == androidAdapter.getItemCount()) {

                    boolean isRefreshing = refreshLayout.isRefreshing();
                    if (isRefreshing) {
                        androidAdapter.notifyItemRemoved(androidAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                List<AndroidBean> newDatas = new ArrayList<>();
                                presenter.getAndroidData(handler, newDatas, 2);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(getActivity());
        presenter = new AndroidPresenter(this);
        View view = inflater.inflate(R.layout.fragment_android, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.android_recyclewview);
        initRefreshLayout(view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.getAndroidData(handler, androidData, 0);
            }
        }).start();

        return view;
    }

    private void initRefreshLayout(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.android_refresh);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                if (presenter.isNetworkAvailable(getActivity())) {
                    isRolling=true;
                    setRecyclewViewBug();
                    androidData.clear();
                    presenter.getAndroidData(handler, androidData, 1);
                } else {
                    refreshLayout.setRefreshing(false);
                    showToast("没网络了");
                }
            }
        });
    }

    @Override
    public void setPresenter(AndroidContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showToast(CharSequence msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
    public void setRecyclewViewBug() {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
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
