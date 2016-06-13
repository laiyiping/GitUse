package com.strong.googleplay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.strong.googleplay.R;
import com.strong.googleplay.domain.AppInfo;
import com.strong.googleplay.holder.DetailBottomHolder;
import com.strong.googleplay.holder.DetailDesHolder;
import com.strong.googleplay.holder.DetailInfoHolder;
import com.strong.googleplay.holder.DetailSafeHolder;
import com.strong.googleplay.holder.DetailScreenHolder;
import com.strong.googleplay.protocol.DetailProtocol;
import com.strong.googleplay.utils.UiUtils;
import com.strong.googleplay.view.LoadingPage;

import java.util.ArrayList;

public class DetailActivity extends BaseActivity {

    private FrameLayout frameLayout;
    private LoadingPage loadingPage;
    private String mPackageName;
    private AppInfo mAppInfo;
    private DetailBottomHolder detailBottomHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mPackageName = intent.getStringExtra("packageName");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册内容观察者
        detailBottomHolder.unRegisterObserver();

    }

    @Override
    void initView() {
        setContentView(R.layout.activity_detail);
        frameLayout = (FrameLayout) findViewById(R.id.fl_detail_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingPage =new LoadingPage(this) {
            @Override
            protected LoadResult load() {
                return DetailActivity.this.load();
            }

            @Override
            protected View createSuccessView() {
                return DetailActivity.this.createSuccessView();
            }
        };
        frameLayout.addView(loadingPage);
        show();
    }


    //请求加载界面
    private void show() {
        loadingPage.show();
    }

    /*
    请求网络数据
     */
    public LoadingPage.LoadResult load() {
        DetailProtocol detailProtocol=new DetailProtocol(mPackageName);
        mAppInfo= detailProtocol.load(0);

        return LoadResult(mAppInfo);
    }

    public LoadingPage.LoadResult LoadResult(AppInfo info) {
        if (info == null) {
            return LoadingPage.LoadResult.error;
        } else {
            return LoadingPage.LoadResult.success;
        }
    }

    /**
     * 加载成功后界面
     * @return 成功界面
     */
    FrameLayout flBotton;
    FrameLayout flDetailInfo;
    FrameLayout flDetailSafe;
    FrameLayout flDetailDes;
    HorizontalScrollView hsvDetailScreen;


    private DetailInfoHolder mDetailInfoHolder;
    private DetailScreenHolder mDetailScreenHolder;
    private DetailSafeHolder mDetailSafeHolder;

    public View createSuccessView() {


        View view = View.inflate(UiUtils.getContext(), R.layout.content_detail, null);

        //应用详情页面
        flDetailInfo = (FrameLayout) view.findViewById(R.id.detail_info);
        mDetailInfoHolder = new DetailInfoHolder();
        mDetailInfoHolder.setData(mAppInfo);
        flDetailInfo.addView(mDetailInfoHolder.getContentView());
        //图片详情
        hsvDetailScreen = (HorizontalScrollView) view.findViewById(R.id.detail_screen);
        mDetailScreenHolder = new DetailScreenHolder();
        mDetailScreenHolder.setData(mAppInfo);
        hsvDetailScreen.addView(mDetailScreenHolder.getContentView());
        //安全信息
        flDetailSafe = (FrameLayout) view.findViewById(R.id.detail_safe);
        mDetailSafeHolder = new DetailSafeHolder();
        mDetailSafeHolder.setData(mAppInfo);
        flDetailSafe.addView(mDetailSafeHolder.getContentView());

        //应用简介
        flDetailDes = (FrameLayout) view.findViewById(R.id.detail_des);
        DetailDesHolder mDetailDesHolder=new DetailDesHolder();
        mDetailDesHolder.setData(mAppInfo);
        flDetailDes.addView(mDetailDesHolder.getContentView());

        //下载分享
        flBotton= (FrameLayout) view.findViewById(R.id.bottom_layout);
        detailBottomHolder =new DetailBottomHolder();
        detailBottomHolder.setData(mAppInfo);
        //注册内容观察者
        detailBottomHolder.registerObserver();
        flBotton.addView(detailBottomHolder.getContentView());
        return view;
    }


}
