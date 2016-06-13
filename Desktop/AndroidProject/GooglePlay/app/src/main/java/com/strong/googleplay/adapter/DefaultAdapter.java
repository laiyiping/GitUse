package com.strong.googleplay.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.strong.googleplay.DownloadManager;
import com.strong.googleplay.domain.AppInfo;
import com.strong.googleplay.domain.DownloadInfo;
import com.strong.googleplay.holder.BaseHolder;
import com.strong.googleplay.holder.MoreHolder;
import com.strong.googleplay.manager.ThreadManager;
import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/31.
 */
public abstract class DefaultAdapter<Data> extends BaseAdapter {

    public static final int DEFAULT_TYPE=0;
    public static final int MORE_TYPE=1;


    protected ArrayList<Data> datas = new ArrayList<>();
    private ListView listView;

    public ArrayList<Data> getDatas() {
        return datas;
    }

    public DefaultAdapter(ArrayList<Data> datas, final ListView listView) {
        this.datas.addAll(datas);
        this.listView = listView;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onInnerItemClick(position - listView.getHeaderViewsCount()); //条目位置修正后
            }
        });
    }

    public void onInnerItemClick(int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (position == datas.size()) {
            return MORE_TYPE;
        } else {
            return getDefaultType(position);
        }
//        return super.getItemViewType(position);
    }

    //返回默认条目类型
    public int getDefaultType(int position) {
        return DEFAULT_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount()+1;
    }

    @Override
    public int getCount() {
        return datas.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BaseHolder holder=null;

        switch (getItemViewType(position)) {
            case MORE_TYPE:
                if (convertView == null) {
                    holder = getMoreHolder();
                } else {
                    holder = (BaseHolder) convertView.getTag();
                }
                break;
             default:
                if (convertView == null) {
                    holder = getHolder();
                } else {
                    holder= (BaseHolder) convertView.getTag();
                }
                 Data data=datas.get(position);
                holder.setData(data);

                break;
        }

        return holder.getContentView();
    }






    MoreHolder moreHolder;
    private BaseHolder getMoreHolder() {
        if (moreHolder == null) {
            moreHolder=new MoreHolder(this,hasMore());
        }
        return moreHolder;
    }

    //判断有没更多数据
    protected boolean hasMore() {
        return true;
    }

    public abstract BaseHolder getHolder();


//    从网络获取数据
    public void loadMore() {
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Data> newDatas = onLoad();
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (newDatas == null) {
                            //TODO 加载失败
                            moreHolder.setData(MoreHolder.LOAD_ERR);
                        } else if (newDatas.size() == 0) {
                            //TODO 没有更多数据
                            moreHolder.setData(MoreHolder.HAS_NO_MORE);
                        } else {
                            //成功了
                            moreHolder.setData(MoreHolder.HAS_MORE);
                            datas.addAll(newDatas);
                            notifyDataSetChanged();//刷新界面
                        }
                    }
                });
            }
        });
    }

    protected abstract ArrayList<Data> onLoad();
}
