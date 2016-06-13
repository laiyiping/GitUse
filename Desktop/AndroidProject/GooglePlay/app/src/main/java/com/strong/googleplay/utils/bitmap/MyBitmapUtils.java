package com.strong.googleplay.utils.bitmap;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.strong.googleplay.R;
import com.strong.googleplay.utils.LogUtils;


/**
 * 自定义图片加载工具
 * 
 * @author Kevin
 * 
 */
public class MyBitmapUtils {

	NetCacheUtils mNetCacheUtils;
	LocalCacheUtils mLocalCacheUtils;
	MemoryCacheUtils mMemoryCacheUtils;
	public static MyBitmapUtils instance;

	private MyBitmapUtils() {
		mMemoryCacheUtils = new MemoryCacheUtils();
		mLocalCacheUtils = new LocalCacheUtils();
		mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils, mMemoryCacheUtils);
	}

	public static MyBitmapUtils getMyBitmapUtils() {
		if (instance == null) {
			instance = new MyBitmapUtils();
		}
		return instance;
	}

	public void display(ImageView ivPic, String url) {
//		ivPic.setImageResource(R.drawable.news_pic_default);// 设置默认加载图片

		Bitmap bitmap = null;
		// 从内存读
		bitmap = mMemoryCacheUtils.getBitmapFromMemory(url);
		if (bitmap != null) {
			ivPic.setImageBitmap(bitmap);
//			LogUtils.e("从内存读取图片啦...");
			return;
		}

		// 从本地读
		bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
		if (bitmap != null) {
			ivPic.setImageBitmap(bitmap);
//			LogUtils.e("从本地读取图片啦...");
			mMemoryCacheUtils.setBitmapToMemory(url, bitmap);// 将图片保存在内存
			return;
		}

		// 从网络读
		mNetCacheUtils.getBitmapFromNet(ivPic, url);
	}

}
