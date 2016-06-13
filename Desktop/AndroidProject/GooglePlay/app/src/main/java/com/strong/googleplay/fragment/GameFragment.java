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
import com.strong.googleplay.protocol.GameProtocol;
import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.utils.UiUtils;
import com.strong.googleplay.view.LoadingPage;

import java.util.ArrayList;


public class GameFragment extends BaseFragment {

    private ArrayList<AppInfo> mAppInfoList;

    // 当Fragment挂载的activity创建的时候调用
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        show(); //加载数据并切换界面
    }

    @Override
    protected View createSuccessView() {
        ListView listView = new ListView(UiUtils.getContext());
        if (mAppInfoList != null) {
            listView.setAdapter(new GameAdapter(mAppInfoList,listView));
        }
        return listView;
    }


    @Override
    protected LoadingPage.LoadResult load() {

        GameProtocol gameProtocol = new GameProtocol();
        mAppInfoList =gameProtocol.load(0);
        return LoadResult(mAppInfoList);
    }

    private class GameAdapter extends DefaultAdapter<AppInfo> {

        public GameAdapter(ArrayList arrayList, ListView listView) {
            super(arrayList, listView);
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
            return new GameHolder();
        }

        @Override
        protected ArrayList<AppInfo> onLoad() {
            GameProtocol gameProtocol = new GameProtocol();
            ArrayList<AppInfo> newDatas = gameProtocol.load(getDatas().size());
            return newDatas;
        }
    }

    private class GameHolder extends BaseHolder<AppInfo> {

        ImageView item_icon;
        TextView item_title,item_size,item_bottom;
        RatingBar item_rating;
        FrameLayout flProgress;
        TextView tvProgress;
        private GameDownloadObserver observer;

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

            //注册内容观察者
            observer = new GameDownloadObserver(-1);
            registerObserver();
            return contentView;
        }

        @Override
        public void setData(final AppInfo data) {
            this.item_title.setText(data.getName());
            String size = android.text.format.Formatter.formatFileSize(UiUtils.getContext(), data.getSize());
            this.item_size.setText(size);
            this.item_bottom.setText(data.getDes());
            this.item_rating.setRating(data.getStars());
            myBitmapUtils.display(this.item_icon, GlobalContants.SERVER_URL+"/image?name="+ data.getIconUrl());
            tvProgress.setText("下载");
            flProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    download(data);
                }
            });
            //设置内容观察者
            observer.setId(data.getId());

        }
        private void registerObserver() {
            DownloadManager downloadManager = DownloadManager.getInstance();
            downloadManager.registerObserver(observer);
        }
        //下载
        private void download(final AppInfo appInfo) {

//            progress_layout.setVisibility(View.VISIBLE);
//            progress_btn.setVisibility(View.GONE);
//            pbLoading.setMax((int) (appInfo.getSize()/1024));
//            pbLoading.setProgress(0);

            DownloadManager downloadManager = DownloadManager.getInstance();
            downloadManager.download(appInfo);
        }

        private class GameDownloadObserver implements DownloadManager.DownloadObserver {


            private long id;

            public GameDownloadObserver(long id) {
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
                    LogUtils.e(""+info.getCurrentSize());
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
