package com.strong.googleplay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.strong.googleplay.DownloadManager;
import com.strong.googleplay.R;
import com.strong.googleplay.activity.DetailActivity;
import com.strong.googleplay.adapter.DefaultAdapter;
import com.strong.googleplay.domain.AppInfo;
import com.strong.googleplay.domain.DownloadInfo;
import com.strong.googleplay.global.GlobalContants;
import com.strong.googleplay.holder.BaseHolder;
import com.strong.googleplay.protocol.AppProtocol;
import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.utils.UiUtils;
import com.strong.googleplay.view.LoadingPage;

import java.util.ArrayList;


public class AppFragment extends BaseFragment {

    ArrayList<AppInfo> mAppInfosList;

    // 当Fragment挂载的activity创建的时候调用
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        show(); //加载数据并切换界面
    }

    @Override
    protected View createSuccessView() {
        ListView listView = new ListView(UiUtils.getContext());
        if (mAppInfosList != null) {
            listView.setAdapter(new AppAdapter(mAppInfosList,listView));
        }
        return listView;
    }


    @Override
    protected LoadingPage.LoadResult load() {

        AppProtocol appProtocol = new AppProtocol();
        mAppInfosList=appProtocol.load(0);
        return LoadResult(mAppInfosList);
    }


    private class AppAdapter extends DefaultAdapter<AppInfo> {

        public AppAdapter(ArrayList<AppInfo> appInfos, ListView listView) {
            super(appInfos, listView);
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
            //新建内容观察者
            return new AppHolder();
        }

        @Override
        protected ArrayList<AppInfo> onLoad() {
            AppProtocol appProtocol = new AppProtocol();
            ArrayList<AppInfo> newDatas = appProtocol.load(getDatas().size());
            return newDatas;
        }

        private class AppHolder extends BaseHolder<AppInfo> implements View.OnClickListener{

            ImageView item_icon;
            TextView item_title,item_size,item_bottom;
            RatingBar item_rating;
            FrameLayout flProgress;
            TextView tvProgress;
            private AppDownloadObserver observer;
            private AppInfo mAppInfo;
            private DownloadManager downloadManager;

            @Override
            public View initView() {

                View contentView = View.inflate(UiUtils.getContext(), R.layout.item_app, null);
                this.item_icon = (ImageView) contentView.findViewById(R.id.item_icon);
                this.item_title = (TextView) contentView.findViewById(R.id.item_title);
                this.item_size = (TextView) contentView.findViewById(R.id.item_size);
                this.item_bottom = (TextView) contentView.findViewById(R.id.item_bottom);
                this.item_rating = (RatingBar) contentView.findViewById(R.id.item_rating);
                flProgress= (FrameLayout) contentView.findViewById(R.id.action_progress);
                tvProgress = (TextView) contentView.findViewById(R.id.action_txt);
                flProgress.setOnClickListener(this);
                //注册内容观察者
                observer = new AppDownloadObserver(-1);
                registerObserver();
                return contentView;
            }

            @Override
            public void setData(final AppInfo data) {
                mAppInfo = data;
                item_title.setText(data.getName());
                String size = android.text.format.Formatter.formatFileSize(UiUtils.getContext(), data.getSize());
                item_size.setText(size);
                item_bottom.setText(data.getDes());
                item_rating.setRating(data.getStars());
                myBitmapUtils.display(item_icon, GlobalContants.SERVER_URL+"/image?name="+ data.getIconUrl());
                tvProgress.setText("下载");

               //设置内容观察者
                observer.setId(data.getId());

            }


            private void registerObserver() {
                DownloadManager downloadManager = DownloadManager.getInstance();
                downloadManager.registerObserver(observer);
            }
            //下载
            private void download(final AppInfo appInfo) {

                downloadManager = DownloadManager.getInstance();
                downloadManager.download(appInfo);
            }

            @Override
            public void onClick(View v) {
                DownloadInfo info = DownloadManager.getInstance().getDownloadInfo(mAppInfo.getId());
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

            private class AppDownloadObserver implements DownloadManager.DownloadObserver {


                private long id;

                public AppDownloadObserver(long id) {
                    this.id = id;
                }

                @Override
                public void onDownloadStateChanged(DownloadInfo info) {
                    if(info.getId()== id){
                        switch (info.getState()) {
                            case DownloadManager.STATE_NONE:
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
                    }
                }

                @Override
                public void onDownloadProgressed(DownloadInfo info) {
                    if (info.getId() == id) {
                        int scale= (int) (info.getCurrentSize()*1000/info.getAppSize());
                        int a=scale/10;
                        int b=scale%10;
                        tvProgress.setText(a+"."+b+"%");
//                        tvProgress.setText((int) (info.getCurrentSize()/1024));
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

}
