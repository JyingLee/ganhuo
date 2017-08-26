package com.jying.ganhuo.Module.welfare;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jying.ganhuo.Adapter.WelfareAdapter;
import com.jying.ganhuo.Bean.WelfareBean;
import com.jying.ganhuo.R;
import com.jying.ganhuo.Utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jying on 2017/8/23.
 */

public class WelfareFragment extends Fragment implements WelfareContract.View {
    private int count;
    WelfareContract.Presenter presenter;
    private RecyclerView welfareRecyclewview;
    private List<WelfareBean> welfareDatas = new ArrayList<>();
    private WelfareAdapter welfareAdapter;
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
                    int flag = msg.getData().getInt("welfare_flag");
                    switch (flag) {
                        case 0:
                            welfareDatas = (List<WelfareBean>) msg.obj;
                            initRecyclewView();
                            break;
                        case 1:
                            if (isInit==true) {
                                welfareDatas = (List<WelfareBean>) msg.obj;
                                if (welfareDatas != null) {
                                    welfareAdapter.notifyDataSetChanged();
                                    showToast("已更新数据");
                                }
                                isRolling = false;
                                setRecyclewViewBug();
                                refreshLayout.setRefreshing(false);
                            }else {
                                presenter.getWelareData(handler,welfareDatas,0);
                                isRolling = false;
                                setRecyclewViewBug();
                                refreshLayout.setRefreshing(false);
                            }
                            break;
                        case 2:
                            List<WelfareBean> newDatas = (List<WelfareBean>) msg.obj;
                            welfareAdapter.addMoreDatas(newDatas);
                            welfareAdapter.notifyItemRemoved(welfareAdapter.getItemCount());
                            break;
                    }
                    break;
            }
        }
    };

    private void initRecyclewView() {
        welfareAdapter = new WelfareAdapter(getContext(), welfareDatas);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        welfareRecyclewview.setLayoutManager(layoutManager);
        welfareRecyclewview.addItemDecoration(new SpacesItemDecoration(16));
        welfareRecyclewview.setAdapter(welfareAdapter);
        welfareRecyclewview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == welfareAdapter.getItemCount()) {

                    boolean isRefreshing = refreshLayout.isRefreshing();
                    if (isRefreshing) {
                        welfareAdapter.notifyItemRemoved(welfareAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                List<WelfareBean> newDatas = new ArrayList<>();
                                presenter.getWelareData(handler, newDatas, 2);
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                int columnCount = layoutManager.getColumnCountForAccessibility(null, null);//获取瀑布流的列数
                int columnCount = layoutManager.getSpanCount();//获取瀑布流的列数
                int[] positions = new int[columnCount];
                layoutManager.findLastVisibleItemPositions(positions);//获取瀑布流的每一列的最下面的条目的索引（并不是最后n个(n为瀑布流的列数)），有的条目可能会很长
                lastVisibleItemPosition = presenter.getArrayMax(positions);//返回其中最大的一个（它是乱序的，并不是按顺序保存的）
            }
        });
        isInit=true;
    }

    public static WelfareFragment newInstance(int count) {
        WelfareFragment welfareFragment = new WelfareFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("count", count);
        welfareFragment.setArguments(bundle);
        return welfareFragment;
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
        presenter = new WelfarePresenter(this);
        View view = inflater.inflate(R.layout.fragment_welfare, container, false);
        welfareRecyclewview = (RecyclerView) view.findViewById(R.id.welfare_recyclewview);
        initrefreshlayout(view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.getWelareData(handler, welfareDatas, 0);
            }
        }).start();
        return view;
    }

    private void initrefreshlayout(View view) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.welfare_refresh);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                if (presenter.isNetworkAvailable(getActivity())) {
                    isRolling = true;
                    setRecyclewViewBug();
                    welfareDatas.clear();
                    presenter.getWelareData(handler, welfareDatas, 1);
                } else {
                    refreshLayout.setRefreshing(false);
                    showToast("没网络了");
                }
            }
        });
    }

    @Override
    public void setPresenter(WelfareContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showToast(CharSequence msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void setRecyclewViewBug() {
        welfareRecyclewview.setOnTouchListener(new View.OnTouchListener() {
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
