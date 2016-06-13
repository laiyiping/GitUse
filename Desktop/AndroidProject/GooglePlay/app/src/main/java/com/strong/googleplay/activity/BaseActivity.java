package com.strong.googleplay.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pools;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.strong.googleplay.R;

import java.util.LinkedList;
import java.util.List;

abstract   class BaseActivity extends AppCompatActivity {

    static List<BaseActivity> mActivities = new LinkedList<>();

    KillAllReceiver receiver;

    private class KillAllReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        receiver = new KillAllReceiver();
        IntentFilter filter = new IntentFilter("com.strong.googleplay.kellall");
        registerReceiver(receiver, filter);

        synchronized (mActivities) {
            mActivities.add(this);
        }
        init();
        initView();
        initActitonBar();

    }

    protected void init() {
    }

    abstract void initView();

    protected void initActitonBar() {

    }

    public void killAll() {
        List<BaseActivity> copy = null;
        synchronized (mActivities) {
            copy=new LinkedList<>(mActivities);
        }
        for (BaseActivity activity : copy) {
            activity.finish();
        }
//        杀死所有进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
        receiver = null;
        synchronized (mActivities) {
            mActivities.remove(this);
        }

    }


}
