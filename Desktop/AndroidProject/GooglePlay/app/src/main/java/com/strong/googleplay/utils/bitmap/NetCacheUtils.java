package com.strong.googleplay.utils.bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.utils.UiUtils;

/**
 * 网络缓存
 * 
 * @author Kevin
 * 
 */
public class NetCacheUtils {

	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;

	public NetCacheUtils(LocalCacheUtils localCacheUtils,
			MemoryCacheUtils memoryCacheUtils) {
		mLocalCacheUtils = localCacheUtils;
		mMemoryCacheUtils = memoryCacheUtils;
	}

	/**
	 * 从网络下载图片
	 * 
	 * @param ivPic
	 * @param url
	 */
	public void getBitmapFromNet(ImageView ivPic, String url) {
		new BitmapTask().execute(ivPic, url);// 启动AsyncTask,
												// 参数会在doInbackground中获取
	}

	/**
	 * Handler和线程池的封装
	 * 
	 * 第一个泛型: 参数类型 第二个泛型: 更新进度的泛型, 第三个泛型是onPostExecute的返回结果
	 * 
	 * @author Kevin
	 * 
	 */
	class BitmapTask extends AsyncTask<Object, Void, Bitmap> {

		private ImageView ivPic;
		private String url;

		public BitmapTask() {
		}

		/**
		 * 后台耗时方法在此执行, 子线程
		 */
		@Override
		protected Bitmap doInBackground(Object... params) {
			ivPic = (ImageView) params[0];
			url = (String) params[1];


			UiUtils.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ivPic.setTag(url);// 将url和imageview绑定
				}
			});

			return downloadBitmap(url,ivPic);
		}

		/**
		 * 更新进度, 主线程
		 */
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		/**
		 * 耗时方法结束后,执行该方法, 主线程
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null) {
				String bindUrl = (String) ivPic.getTag();

				if (url.equals(bindUrl)) {// 确保图片设定给了正确的imageview

					ivPic.setImageBitmap(bitmap);
					mLocalCacheUtils.setBitmapToLocal(url, bitmap);// 将图片保存在本地
					mMemoryCacheUtils.setBitmapToMemory(url, bitmap);// 将图片保存在内存
//					LogUtils.e("从网络缓存读取图片啦...");
				}
			}
		}
	}

	/**
	 * 下载图片
	 * 
	 * @param
	 * @param url
	 * @param
	 *@param  @return
	 */
	private Bitmap downloadBitmap(String url,ImageView ivPic) {

		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();

			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setRequestMethod("GET");
			conn.connect();

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				final InputStream inputStream = conn.getInputStream();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len;
				try {
					while ((len = inputStream.read(buffer)) > -1 ) {
						baos.write(buffer, 0, len);
					}
					baos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

				//图片压缩处理
				BitmapFactory.Options option = new BitmapFactory.Options();

				option.inJustDecodeBounds=true;
				BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size(), option);
				int bitmapWidth=option.outWidth;//图片宽
				int bitmapHeight = option.outHeight;//图片高
				int disWidth = ivPic.getMeasuredWidth();
				int dispHeight = ivPic.getMeasuredHeight();
				float widthSize = bitmapWidth / disWidth;
				float heightSize = bitmapHeight / dispHeight;
				float isSampleSize = widthSize > heightSize ? widthSize : heightSize;
				if (isSampleSize < 1) {
					isSampleSize=1;
				}
				LogUtils.e("图片宽"+bitmapWidth+"图片高"+bitmapHeight+"显示宽"+disWidth+"显示高"+dispHeight+"压缩比例"+isSampleSize);
				option.inSampleSize = Math.round(isSampleSize);//宽高都压缩
				option.inPreferredConfig = Bitmap.Config.RGB_565;//设置图片格式
				option.inJustDecodeBounds = false;
				Bitmap bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size(), option);

				return bitmap;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(conn!=null)
				conn.disconnect();
		}

		return null;
	}

}
