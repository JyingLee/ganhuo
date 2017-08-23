package com.jying.ganhuo.Module.welfare;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
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
    private List<WelfareBean> lists = new ArrayList<>();
    private WelfareAdapter welfareAdapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    lists = (List<WelfareBean>) msg.obj;
                    initRecyclewView();
                    break;
            }
        }
    };

    private void initRecyclewView() {
        welfareAdapter = new WelfareAdapter(getContext(), lists);
        welfareRecyclewview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        welfareRecyclewview.addItemDecoration(new SpacesItemDecoration(16));
        welfareRecyclewview.setAdapter(welfareAdapter);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.getWelareData(handler);
            }
        }).start();
        return view;
    }

    @Override
    public void setPresenter(WelfareContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showToast(CharSequence msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
