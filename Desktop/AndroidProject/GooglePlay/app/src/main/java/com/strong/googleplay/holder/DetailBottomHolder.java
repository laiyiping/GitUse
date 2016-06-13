package com.strong.googleplay.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.strong.googleplay.DownloadManager;
import com.strong.googleplay.R;
import com.strong.googleplay.domain.AppInfo;
import com.strong.googleplay.domain.DownloadInfo;
import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.utils.UiUtils;

public class DetailBottomHolder extends BaseHolder<AppInfo> implements OnClickListener {
	@ViewInject(R.id.bottom_favorites)
	Button bottom_favorites;
	@ViewInject(R.id.bottom_share)
	Button bottom_share;
	@ViewInject(R.id.progress_btn)
	Button progress_btn;
	@ViewInject(R.id.progress_layout)
	FrameLayout progress_layout;
	@ViewInject(R.id.pb_load_process)
	ProgressBar pbLoading;
	@ViewInject(R.id.tv_load_process)
	TextView tvLoading;
	private AppInfo appInfo;
	private DetailDownloadObserver observer=new DetailDownloadObserver();

	@Override
	public View initView() {

		View view = UiUtils.inflate(R.layout.detail_bottom);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void setData(AppInfo data) {
		appInfo=data;
		bottom_favorites.setOnClickListener(this);
		bottom_share.setOnClickListener(this);
		progress_btn.setOnClickListener(this);
		//判断是否正在下载
		DownloadInfo info=DownloadManager.getInstance().getDownloadInfo(data.getId());
		if ( info!= null) {
			progress_layout.setVisibility(View.VISIBLE);
			progress_btn.setVisibility(View.GONE);
			pbLoading.setMax((int) (appInfo.getSize() / 1024));
			observer.onDownloadStateChanged(info);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bottom_favorites:
				Toast.makeText(UiUtils.getContext(), "收藏", Toast.LENGTH_SHORT).show();
				break;
			case R.id.bottom_share:
				Toast.makeText(UiUtils.getContext(), "分享", Toast.LENGTH_SHORT).show();
				break;
			case R.id.progress_btn:
				Toast.makeText(UiUtils.getContext(), "下载", Toast.LENGTH_SHORT).show();

				download();
				break;
		}
	}

	private void download() {

		progress_layout.setVisibility(View.VISIBLE);
		progress_btn.setVisibility(View.GONE);
		pbLoading.setMax((int) (appInfo.getSize()/1024));
		pbLoading.setProgress(0);

		DownloadManager downloadManager = DownloadManager.getInstance();
		downloadManager.download(appInfo);
	}

	public void registerObserver() {
		DownloadManager downloadManager = DownloadManager.getInstance();
		downloadManager.registerObserver(observer);
	}

	public void unRegisterObserver() {
		DownloadManager downloadManager = DownloadManager.getInstance();
		downloadManager.unRegisterObserver(observer);
	}

	private class DetailDownloadObserver implements DownloadManager.DownloadObserver {
		@Override
		public void onDownloadStateChanged(DownloadInfo info) {
			if(info.getId()==appInfo.getId()){
				switch (info.getState()) {
					case DownloadManager.STATE_NONE:
					case DownloadManager.STATE_WAITING:
						tvLoading.setText("准备中");
						break;
					case DownloadManager.STATE_DOWNLOADING:
						tvLoading.setText("下载中："+(info.getCurrentSize()*1000/info.getAppSize())/10f+"%");
						break;
					case DownloadManager.STATE_PAUSE:
						tvLoading.setText("暂停中");
						break;
					case DownloadManager.STATE_DOWNLOADED:
						tvLoading.setText("点击安装");
						break;
					case DownloadManager.STATE_ERR:
						tvLoading.setText("下载失败");
						break;
				}
			}
		}

		@Override
		public void onDownloadProgressed(DownloadInfo info) {
			if (info.getId() == appInfo.getId()) {
				int scale= (int) (info.getCurrentSize()*1000/info.getAppSize());
				int a=scale/10;
				int b=scale%10;
				tvLoading.setText("下载中："+a+"."+b+"%");
				pbLoading.setProgress((int) (info.getCurrentSize()/1024));
				LogUtils.e(""+info.getCurrentSize());
			}
		}

	}

}
