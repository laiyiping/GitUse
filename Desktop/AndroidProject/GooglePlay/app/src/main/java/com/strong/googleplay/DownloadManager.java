package com.strong.googleplay;

import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.IOUtils;
import com.strong.googleplay.domain.AppInfo;
import com.strong.googleplay.domain.DownloadInfo;
import com.strong.googleplay.global.GlobalContants;
import com.strong.googleplay.manager.ThreadManager;
import com.strong.googleplay.utils.FileUtils;
import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.utils.UiUtils;

import org.apache.http.HttpConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/6/6.
 */
public class DownloadManager {

    public static final int STATE_NONE = 0;
    public static final int STATE_PAUSE = 1;
    public static final int STATE_ERR = 2;
    public static final int STATE_WAITING = 3;
    public static final int STATE_DOWNLOADING = 4;
    public static final int STATE_DOWNLOADED = 5;
    public static DownloadManager instance;
    private ArrayList<DownloadObserver> mObservers = new ArrayList<>();
    /*用于记录下载信息*/
    private Map<Long, DownloadInfo> mDownloadMap = new ConcurrentHashMap<>();
    /*用于记录所有的下载任务，方便取消下载时，通过id找到任务进行删除*/
    private Map<Long, DownloadTask> mTaskMap = new ConcurrentHashMap<>();

    //私有构造方法 单例
    private DownloadManager(){
    }

    //    获得单例
    public static DownloadManager getInstance() {
        if (instance == null) {
            instance = new DownloadManager();
        }
        return instance;
    }



//    开始下载
    public synchronized void download(AppInfo appInfo) {
        DownloadInfo info = mDownloadMap.get(appInfo.getId());
        if (info == null) { //任务不在列表中 添加进去
            info=new DownloadInfo();
            info.setAppSize(appInfo.getSize());
            info.setCurrentSize(0);
            info.setDownloadUrl(appInfo.getDownloadUrl());
            info.setId(appInfo.getId());
            info.setPackageName(appInfo.getPackageName());
            info.setPath(FileUtils.getFileDir().toString()+"/"+info.getId()+".apk");
            info.setState(STATE_NONE);
            mDownloadMap.put(info.getId(), info);//保存到下载集合中
        }
        if (info.getState() == STATE_NONE || info.getState() == STATE_PAUSE
                || info.getState() == STATE_ERR) {//
            info.setState(STATE_WAITING);
            notifyDownloadStateChanged(info);
            //创建下载任务
            DownloadTask downloadTask=new DownloadTask(info,appInfo);
            mTaskMap.put(info.getId(), downloadTask);
            ThreadManager threadManager=ThreadManager.getInstance();
            threadManager.createLongPool().execute(downloadTask);
        }
    }

    //启动安装程序
    public synchronized void install(AppInfo appInfo) {
        stopDownload(appInfo);
        DownloadInfo info = mDownloadMap.get(appInfo.getId());
        if (info != null&&info.getState()==STATE_DOWNLOADED) {
            LogUtils.e("启动应用安装程序");
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.setDataAndType(Uri.parse("file://" + info.getPath()),
                    "application/vnd.android.package-archive");
            UiUtils.getContext().startActivity(installIntent);
        }
    }

    //暂停下载
    public  synchronized void pause(AppInfo appInfo) {
        stopDownload(appInfo);
        DownloadInfo info = mDownloadMap.get(appInfo.getId());
        if (info != null) {
            info.setState(STATE_PAUSE);
            notifyDownloadStateChanged(info);
        }
    }


    //取消下载
    public synchronized void cancel(AppInfo appInfo) {
        stopDownload(appInfo);
        DownloadInfo info = mDownloadMap.get(appInfo.getId());
        if (info != null) {
            info.setState(STATE_NONE);
            notifyDownloadStateChanged(info);
            File file = new File(info.getPath());
            file.delete();
            mDownloadMap.remove(info.getId());
        }
    }

    //停止下载下载
    public void stopDownload(AppInfo appInfo) {
        DownloadTask task = mTaskMap.remove(appInfo.getId());
        if (task != null) {
            ThreadManager.getInstance().createLongPool().cancel(task);
        }
    }



    //下载接口
    private class DownloadTask implements Runnable{

        private DownloadInfo info;
        private AppInfo appInfo;
        public DownloadTask(DownloadInfo info, AppInfo appInfo) {
            this.info=info;
            this.appInfo = appInfo;
        }

        @Override
        public void run() {
            String url;
            SystemClock.sleep(1000);
            UiUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    info.setState(STATE_DOWNLOADING);
                    notifyDownloadStateChanged(info);
                }
            });

            File file = new File(info.getPath());

            if (info.getCurrentSize() == 0 || !file.exists()
                    || file.length() != info.getCurrentSize()) {
                info.setCurrentSize(0);
                file.delete();
                url= GlobalContants.SERVER_URL+"/download?name="+info.getDownloadUrl();
            } else {
                url= GlobalContants.SERVER_URL+"/download?name="+info.getDownloadUrl()
                        + "&range="+info.getCurrentSize();
                LogUtils.e("断点继续下载"+info.getCurrentSize()+"   "+file.length());
            }


            HttpURLConnection conn = null;
            FileOutputStream fos = null;
            InputStream inputStream = null;

            try {
                conn = (HttpURLConnection) new URL(url).openConnection();

                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");
                conn.connect();

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    inputStream = conn.getInputStream();

                    fos = new FileOutputStream(file, true);
                    byte[] buffer = new byte[1024];
                    int count ;
                    while ((count=inputStream.read(buffer)) != -1&&info.getState()==STATE_DOWNLOADING) {
                        fos.write(buffer, 0, count);
                        fos.flush();
                        info.setCurrentSize(info.getCurrentSize() + count);
                        notifyDownloadProgressed(info);
                        if (count < 1024) {
                            break;
                        }
                    }



                    if (info.getState() == STATE_PAUSE) {
                        conn.disconnect();
                        conn = null;
                    }
                }
            } catch (Exception e) {
                LogUtils.e("下载抛出异常");
                e.printStackTrace();
                info.setState(STATE_ERR);
                notifyDownloadStateChanged(info);
                info.setCurrentSize(0);
                file.delete();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



            if (info.getCurrentSize() == info.getAppSize()) {//下载完毕
                info.setState(STATE_DOWNLOADED);
                notifyDownloadStateChanged(info);
                install(appInfo);

            } else if (info.getState() == STATE_PAUSE) {//暂停
                notifyDownloadStateChanged(info);
            } else {//出错
                info.setState(STATE_ERR);
                notifyDownloadStateChanged(info);
                info.setCurrentSize(0);
                file.delete();
            }

            mTaskMap.remove(info.getId());
        }
    }

    public DownloadInfo getDownloadInfo(long id) {
        return mDownloadMap.get(id);
    }


    //注册观察者
    public void registerObserver(DownloadObserver observer) {
        synchronized (mObservers) {
            if (observer == null) {
                throw new RuntimeException();
            }
            if (!mObservers.contains(observer)) {
                mObservers.add(observer);
            }
        }
    }



    //反注册观察者
    public void unRegisterObserver(DownloadObserver observer) {
        synchronized (mObservers) {
            if (mObservers.contains(observer)) {
                mObservers.remove(observer);
            }
        }
    }

    // 通知数据发生变化
    public void notifyDownloadProgressed(final DownloadInfo info) {
        UiUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (DownloadObserver observer : mObservers) {
                    observer.onDownloadProgressed(info);
                }
            }
        });
    }

    // 通知状态改变
    public void notifyDownloadStateChanged(final DownloadInfo info) {
        UiUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (DownloadObserver observer : mObservers) {
                    observer.onDownloadStateChanged(info);
                }
            }
        });
    }


    public interface DownloadObserver {
        public void onDownloadStateChanged(DownloadInfo info);
        public void onDownloadProgressed(DownloadInfo info);
    }


}
