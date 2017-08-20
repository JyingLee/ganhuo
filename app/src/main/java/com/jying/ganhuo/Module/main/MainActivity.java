package com.jying.ganhuo.Module.main;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jying.ganhuo.R;

public class MainActivity extends AppCompatActivity implements MainContract.View{
    MainContract.presenter mPresenter;
    Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        con=this;
        mPresenter=new MainPresenter(this);
    }

    @Override
    public void setPresenter(MainContract.presenter presenter) {

    }

    @Override
    public void showToast(CharSequence msg) {
        Toast.makeText(con.getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
