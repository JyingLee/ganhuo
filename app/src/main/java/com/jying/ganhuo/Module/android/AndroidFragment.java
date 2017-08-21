package com.jying.ganhuo.Module.android;

import android.os.Bundle;
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
        List<AndroidBean>beans=new ArrayList<>();
        for (int i=0;i<10;i++){
            AndroidBean test=new AndroidBean();
            test.setAuthor("Jying");
            test.setTime("2017/8/20");
            test.setDesc("这是个测试");
            beans.add(test);
        }
        androidAdapter=new AndroidAdapter(getActivity(),beans);
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
        initRecyclewView();
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
