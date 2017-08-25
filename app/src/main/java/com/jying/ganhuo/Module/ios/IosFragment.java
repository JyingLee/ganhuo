package com.jying.ganhuo.Module.ios;

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

import com.jying.ganhuo.Adapter.IosAdapter;
import com.jying.ganhuo.Bean.IosBean;
import com.jying.ganhuo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Jying on 2017/8/22.
 */

public class IosFragment extends Fragment implements IosContract.View {
    IosContract.Presenter presenter;
    private int count;
    RecyclerView iosRecyclewView;
    List<IosBean> iosDatas = new ArrayList<>();
    IosAdapter iosAdapter;
    private SwipeRefreshLayout refreshLayout;
    private int lastVisibleItemPosition;
    private boolean isLoading = false;//是否在上拉加载
    private boolean isRolling = false;//是否在刷新

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    int flag = msg.getData().getInt("ios_flag");
                    switch (flag) {
                        case 0:
                            iosDatas = (List<IosBean>) msg.obj;
                            initRecyclewView();
                            break;
                        case 1:
                            iosDatas = (List<IosBean>) msg.obj;
                            if (iosDatas != null) {
                                iosAdapter.notifyDataSetChanged();
                                showToast("已更新数据");
                            }
                            isRolling = false;
                            setRecyclewViewBug();
                            refreshLayout.setRefreshing(false);
                            break;
                        case 2:
                            List<IosBean> newDatas = (List<IosBean>) msg.obj;
                            iosAdapter.addMoreDatas(newDatas);
                            iosAdapter.notifyItemRemoved(iosAdapter.getItemCount());
                            break;
                    }
                    break;
            }
        }
    };

    private void initRecyclewView() {
        iosAdapter = new IosAdapter(getActivity(), iosDatas);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        iosRecyclewView.setLayoutManager(layoutManager);
        iosRecyclewView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        iosRecyclewView.setItemAnimator(new DefaultItemAnimator());
        iosRecyclewView.setAdapter(iosAdapter);
        iosRecyclewView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == iosAdapter.getItemCount()) {

                    boolean isRefreshing = refreshLayout.isRefreshing();
                    if (isRefreshing) {
                        iosAdapter.notifyItemRemoved(iosAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                List<IosBean> newDatas = new ArrayList<>();
                                presenter.getIosData(handler, newDatas, 2);
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

    public static IosFragment newInstance(int count) {
        IosFragment iosFragment = new IosFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("count", count);
        iosFragment.setArguments(bundle);
        return iosFragment;
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
        ButterKnife.bind(getActivity());
        presenter = new IosPresenter(this);
        View view = inflater.inflate(R.layout.fragment_ios, container, false);
        iosRecyclewView = (RecyclerView) view.findViewById(R.id.ios_recyclewview);
        initRefreshLayout(view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.getIosData(handler, iosDatas, 0);
            }
        }).start();

        return view;
    }

    private void initRefreshLayout(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.ios_refresh);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                if (presenter.isNetworkAvailable(getActivity())) {
                    isRolling = true;
                    setRecyclewViewBug();
                    iosDatas.clear();
                    presenter.getIosData(handler, iosDatas, 1);
                } else {
                    refreshLayout.setRefreshing(false);
                    showToast("没网络了");
                }
            }
        });
    }

    @Override
    public void setPresenter(IosContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showToast(CharSequence msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void setRecyclewViewBug() {
        iosRecyclewView.setOnTouchListener(new View.OnTouchListener() {
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
