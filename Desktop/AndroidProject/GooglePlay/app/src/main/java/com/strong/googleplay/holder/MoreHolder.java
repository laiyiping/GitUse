package com.strong.googleplay.holder;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.strong.googleplay.R;
import com.strong.googleplay.adapter.DefaultAdapter;
import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.utils.UiUtils;

/**
 * Created by Administrator on 2016/5/31.
 */
public class MoreHolder extends BaseHolder<Integer> {

    public static final int HAS_NO_MORE=0;//没有更多数据了
    public static final int LOAD_ERR=1;//加载失败
    public static final int HAS_MORE=2;//有额外数据
    public static final int NONE=3;//页面为空

    RelativeLayout rlMoreLoading;
    RelativeLayout rlMoreErr;
    RelativeLayout rlNoMore;

    DefaultAdapter adapter;
    boolean hasMore;


    public MoreHolder(DefaultAdapter adapter, boolean hasMore) {
        super();
        this.adapter = adapter;
        this.hasMore = hasMore;
    }

    @Override
    public View initView() {
        View view = View.inflate(UiUtils.getContext(), R.layout.load_more, null);
        rlMoreErr = (RelativeLayout) view.findViewById(R.id.rl_more_error);
        rlMoreLoading = (RelativeLayout) view.findViewById(R.id.rl_more_loading);
        rlNoMore = (RelativeLayout) view.findViewById(R.id.rl_no_more);
        rlMoreErr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMore();
                setData(HAS_MORE);
            }
        });
        rlNoMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UiUtils.getContext(), "没有更多数据啦，逛逛别的吧！", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public View getContentView() {
        if (hasMore) {
            loadMore();
        } else {
            setData(NONE);
        }
        return super.getContentView();
    }

    private void loadMore() {
        adapter.loadMore();
    }


    @Override
    public void setData(Integer state) {//根据不同状态刷新界面
        rlMoreErr.setVisibility(state==LOAD_ERR?View.VISIBLE:View.GONE);
        rlMoreLoading.setVisibility(state==HAS_MORE?View.VISIBLE:View.GONE);
        rlNoMore.setVisibility(state==HAS_NO_MORE?View.VISIBLE:View.GONE);
    }
}
