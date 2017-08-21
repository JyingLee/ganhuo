package com.jying.ganhuo.Module.android;

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

import com.jying.ganhuo.Adapter.AndroidAdapter;
import com.jying.ganhuo.Bean.AndroidBean;
import com.jying.ganhuo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jying on 2017/8/19.
 */

public class AndroidFragment extends Fragment implements AndroidContract.View {
    AndroidContract.Presenter presenter;
    private int page;
    @BindView(R.id.android_recyclewview)
    RecyclerView recyclerView;
    AndroidAdapter androidAdapter;
    List<AndroidBean> androidData=new ArrayList<>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    androidData= (List<AndroidBean>) msg.obj;
                    initRecyclewView();
                    break;
            }
        }
    };

    public static AndroidFragment getInstance(int page) {
        AndroidFragment androidFragment = new AndroidFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page", page);
        androidFragment.setArguments(bundle);
        return androidFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page=getArguments().getInt("page");
        }
    }

    private void initRecyclewView() {
        androidAdapter=new AndroidAdapter(getActivity(),androidData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(androidAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(getActivity());
        presenter = new AndroidPresenter(this);
        View view=inflater.inflate(R.layout.fragment_android,container,false);
        recyclerView= (RecyclerView) view.findViewById(R.id.android_recyclewview);

        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.getAndroidData(handler);
            }
        }).start();

        return view;
    }

    @Override
    public void setPresenter(AndroidContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showToast(CharSequence msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
