package com.strong.googleplay.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strong.googleplay.domain.AppInfo;
import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.view.LoadingPage;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/29.
 */
public abstract class BaseFragment extends Fragment {

    public LoadingPage loadingPage;


    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (loadingPage == null) {
            loadingPage = new LoadingPage(getActivity()) {
                @Override
                protected LoadResult load() {
                    return BaseFragment.this.load();
                }

                @Override
                protected View createSuccessView() {
                    return BaseFragment.this.createSuccessView();
                }
            };
            LogUtils.e("新建LoadPager");
            show();
        }

        return loadingPage;
    }

    public void show() {
        if(loadingPage!=null)
            loadingPage.show();
    }


    public LoadingPage.LoadResult LoadResult(ArrayList list) {
        if (list == null) {
            return LoadingPage.LoadResult.error;
        } else if (list.size() == 0) {
            return LoadingPage.LoadResult.empty;
        } else {
            return LoadingPage.LoadResult.success;
        }

    }
    /**
     * 从网络获取数据
     * @return 获取结果的状态
     */
    protected abstract LoadingPage.LoadResult load();
    protected abstract View createSuccessView();



}
