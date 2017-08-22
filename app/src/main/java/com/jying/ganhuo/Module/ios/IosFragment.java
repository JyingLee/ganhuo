package com.jying.ganhuo.Module.ios;

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
    private List<IosBean>lists=new ArrayList<>();
    IosAdapter iosAdapter;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    lists= (List<IosBean>) msg.obj;
                    initRecyclewView();
                    break;
            }
        }
    };

    private void initRecyclewView() {
        iosAdapter=new IosAdapter(getActivity(),lists);
        iosRecyclewView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        iosRecyclewView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        iosRecyclewView.setItemAnimator(new DefaultItemAnimator());
        iosRecyclewView.setAdapter(iosAdapter);
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
            count=getArguments().getInt("count");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(getActivity());
        presenter=new IosPresenter(this);
        View view=inflater.inflate(R.layout.fragment_ios,container,false);
        iosRecyclewView= (RecyclerView) view.findViewById(R.id.ios_recyclewview);
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.getIosData(handler);
            }
        }).start();
        return view;
    }

    @Override
    public void setPresenter(IosContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showToast(CharSequence msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
