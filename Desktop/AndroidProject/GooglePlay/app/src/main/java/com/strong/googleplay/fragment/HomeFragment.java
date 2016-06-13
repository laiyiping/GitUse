package com.strong.googleplay.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.strong.googleplay.DownloadManager;
import com.strong.googleplay.R;
import com.strong.googleplay.activity.DetailActivity;
import com.strong.googleplay.adapter.DefaultAdapter;
import com.strong.googleplay.domain.AppInfo;
import com.strong.googleplay.domain.DownloadInfo;
import com.strong.googleplay.global.GlobalContants;
import com.strong.googleplay.holder.BaseHolder;
import com.strong.googleplay.holder.HomePictureHolder;
import com.strong.googleplay.protocol.HomeProtocol;
import com.strong.googleplay.utils.BatmapHelper;
import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.utils.UiUtils;
import com.strong.googleplay.view.CircleProgress;
import com.strong.googleplay.view.LoadingPage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;


public class HomeFragment extends BaseFragment {


    private ArrayList<AppInfo> mAppInfoList;
    private BitmapUtils bitmapUtils;
    private ArrayList<String> mPictures;

    //     当Fragment挂载的activity创建的时候调用
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        if(loadingPage.mCurrentState!=LoadingPage.STATE_SUCCESS)
//            show(); //加载数据并切换界面
    }


    @Override
    protected View createSuccessView() {


        LogUtils.e("createSuccessView");
        ListView listView = new ListView(UiUtils.getContext());

        HomePictureHolder homePictureHolder =new HomePictureHolder();
        homePictureHolder.setData(mPictures);
        listView.addHeaderView(homePictureHolder.getContentView());
        //      setSelector  点击显示的颜色
//      setCacheColorHint  拖拽的颜色
//      setDivider  每个条目的间隔 的分割线
        listView.setSelector(R.drawable.nothing);  // 什么都没有
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setDivider(UiUtils.getDrawalbe(R.drawable.nothing));
        if (mAppInfoList != null) {
            HomeAdapter mHomeAdatpter = new HomeAdapter(mAppInfoList, listView);
            listView.setAdapter(mHomeAdatpter);
            bitmapUtils = BatmapHelper.getBitmapUtils();
//            listView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils, false, true));
            bitmapUtils.configDefaultLoadingImage(R.drawable.ic_default);
            bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_default);
        }

        return listView;
    }



    @Override
    protected LoadingPage.LoadResult load() {
        HomeProtocol protocol=new HomeProtocol();
        mAppInfoList = protocol.load(0);
        mPictures = protocol.getmPictures();//获得图片地址
        return LoadResult(mAppInfoList);
    }



    private class HomeAdapter extends DefaultAdapter<AppInfo> {


        public HomeAdapter(ArrayList<AppInfo> appInfos, ListView listView) {
            super(appInfos,listView);
        }

        @Override
        public void onInnerItemClick(int position) {
            Toast.makeText(UiUtils.getContext(), "点击" + position, Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(UiUtils.getContext(), DetailActivity.class);
            intent.putExtra("packageName", getDatas().get(position).getPackageName());
            startActivity(intent);
        }

        @Override
        public BaseHolder getHolder() {
            return new HomeHolder();
        }

        @Override
        protected ArrayList<AppInfo> onLoad() {//加载更多
            HomeProtocol protocol=new HomeProtocol();
            ArrayList<AppInfo> newDatas = protocol.load(getDatas().size());
            return newDatas;
        }


    }

    static class HomeHolder extends BaseHolder<AppInfo> implements View.OnClickListener{

        ImageView item_icon;
        TextView item_title,item_size,item_bottom;
        RatingBar item_rating;
        FrameLayout flProgress;
        TextView tvProgress;
        CircleProgress cpProgress;
        HomeDownloadObserver observer;
        private AppInfo mAppInfo;
        static DownloadManager downloadManager = DownloadManager.getInstance();


        @Override
        public View initView() {
            View contentView = View.inflate(UiUtils.getContext(), R.layout.item_app, null);
            item_icon = (ImageView) contentView.findViewById(R.id.item_icon);
            item_title = (TextView) contentView.findViewById(R.id.item_title);
            item_size = (TextView) contentView.findViewById(R.id.item_size);
            item_bottom = (TextView) contentView.findViewById(R.id.item_bottom);
            item_rating = (RatingBar) contentView.findViewById(R.id.item_rating);
            flProgress= (FrameLayout) contentView.findViewById(R.id.action_progress);
            tvProgress = (TextView) contentView.findViewById(R.id.action_txt);
            cpProgress = (CircleProgress) contentView.findViewById(R.id.download_progress);
            cpProgress.setOnClickListener(this);
            //注册内容观察者
            observer=new HomeDownloadObserver(-1);
            registerObserver();
            return contentView;
        }

        public void setData( AppInfo data) {

            mAppInfo =data;
            //设置内容观察者
            managerObserver(data.getId());
//            appInfo =data;
            item_title.setText(data.getName());
            String size = android.text.format.Formatter.formatFileSize(UiUtils.getContext(), data.getSize());
            item_size.setText(size);
            item_bottom.setText(data.getDes());
            item_rating.setRating(data.getStars());
            myBitmapUtils.display(item_icon, GlobalContants.SERVER_URL+"/image?name="+data.getIconUrl());

            DownloadInfo info = downloadManager.getDownloadInfo(data.getId());
            if (info != null) {
                observer.onDownloadStateChanged(info);
            } else {
                tvProgress.setText("下载");
                cpProgress.setProgress(0);
            }

        }

        private void managerObserver(long newId) {
            observer.setId(newId);
        }

        private void registerObserver() {
            downloadManager.registerObserver(observer);
        }

        //下载
        private void download(final AppInfo appInfo) {

            downloadManager.download(appInfo);
        }

        @Override
        public void onClick(View v) {
            //根据当前状态
            DownloadInfo info=downloadManager.getDownloadInfo(mAppInfo.getId());
            if (info != null) {
                switch (info.getState()) {
                    case DownloadManager.STATE_DOWNLOADING://执行暂停操作
                        downloadManager.pause(mAppInfo);
                        break;
                    case DownloadManager.STATE_DOWNLOADED://执行安装操作
                        downloadManager.install(mAppInfo);
                        break;
                    default: //执行下载操作
                        downloadManager.download(mAppInfo);
                        break;
                }
            } else {
                download(mAppInfo);
            }
        }

        private class HomeDownloadObserver implements DownloadManager.DownloadObserver {

            private long id;

            public HomeDownloadObserver(long id) {
                this.id = id;
            }

            @Override
            public void onDownloadStateChanged(DownloadInfo info) {
                if(info.getId()== id){
                    switch (info.getState()) {
                        case DownloadManager.STATE_NONE:
                            tvProgress.setText("下载");
                            break;
                        case DownloadManager.STATE_WAITING:
                            tvProgress.setText("准备中");
                            break;
                        case DownloadManager.STATE_DOWNLOADING:
                            tvProgress.setText((info.getCurrentSize()*1000/info.getAppSize())/10f+"%");
                            break;
                        case DownloadManager.STATE_PAUSE:
                            tvProgress.setText("暂停中");
                            break;
                        case DownloadManager.STATE_DOWNLOADED:
                            tvProgress.setText("点击安装");
                            break;
                        case DownloadManager.STATE_ERR:
                            tvProgress.setText("下载失败");
                            break;
                    }
                    cpProgress.setProgress(info.getCurrentSize()/1f/info.getAppSize());
                }
            }

            @Override
            public void onDownloadProgressed(DownloadInfo info) {
                if (info.getId() == id) {
                    int scale= (int) (info.getCurrentSize()*1000/info.getAppSize());
                    int a=scale/10;
                    int b=scale%10;
                    tvProgress.setText(a+"."+b+"%");
                    cpProgress.setProgress(scale/1000f);
                }
            }

            public void setId(long id) {
                this.id = id;
            }

            public long getId() {
                return id;
            }
        }
    }
}
