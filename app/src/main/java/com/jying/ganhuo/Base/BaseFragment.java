package com.jying.ganhuo.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by Jying on 2017/8/18.
 */

public class BaseFragment extends Fragment {
    private String s1;

    public static BaseFragment newInstance(String s1){
        BaseFragment baseFragment=new BaseFragment();
        Bundle bundle=new Bundle();
        bundle.putString("s1",s1);
        baseFragment.setArguments(bundle);
        return baseFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            s1=getArguments().getString("s1");
        }
    }
}
