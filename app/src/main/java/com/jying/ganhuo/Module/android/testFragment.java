package com.jying.ganhuo.Module.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jying.ganhuo.R;

/**
 * Created by Jying on 2017/8/20.
 */

public class testFragment extends Fragment {


    public static testFragment getInstance(int page) {
        testFragment testFragment = new testFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page", page);
        testFragment.setArguments(bundle);
        return testFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.item_android,container,false);

        return view;
    }
}
