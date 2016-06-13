package com.strong.googleplay.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.strong.googleplay.adapter.DefaultAdapter;
import com.strong.googleplay.domain.CategoryInfo;
import com.strong.googleplay.holder.BaseHolder;
import com.strong.googleplay.holder.CategoryContentHolder;
import com.strong.googleplay.holder.CategoryTitleHolder;
import com.strong.googleplay.protocol.CategoryProtocol;
import com.strong.googleplay.utils.UiUtils;
import com.strong.googleplay.view.LoadingPage;

import java.util.ArrayList;


public class CategoryFragment extends BaseFragment {

    private static  final int TITLE_TYPE=2;

    private ArrayList<CategoryInfo> infos;

    // 当Fragment挂载的activity创建的时候调用
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        show(); //加载数据并切换界面
    }

    @Override
    protected View createSuccessView() {

        ListView listView = new ListView(UiUtils.getContext());
        listView.setDividerHeight(0);
        listView.setAdapter(new CategoryAdapter(infos, listView));

        return listView;
    }


    @Override
    protected LoadingPage.LoadResult load() {
        CategoryProtocol categoryProtocol=new CategoryProtocol();
        infos = categoryProtocol.load(0);
        return LoadResult(infos);
    }

    private class CategoryAdapter extends DefaultAdapter<CategoryInfo> {

        private int currentPos;

        public CategoryAdapter(ArrayList<CategoryInfo> categoryInfos, ListView listView) {
            super(categoryInfos, listView);
        }


        //返回条目类型
        @Override
        public int getDefaultType(int position) {
            currentPos =position;
            if (getDatas().get(position).getIsTitle()) {//标题类型
                return TITLE_TYPE;

            } else {
                return super.getDefaultType(position);
            }
        }

        //增加一种类型 Title类型
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount()+1;
        }

        @Override
        public BaseHolder getHolder() { //有两种类型Holder  CategoryContentHolder&&CategoryTitleHolder
            if(getDatas().get(currentPos).getIsTitle())//返回标题Holder
                return new CategoryTitleHolder();
            else
                return new CategoryContentHolder();
        }

        @Override
        protected ArrayList<CategoryInfo> onLoad() {
            return null;
        }

        //没有更多数据 返回false
        @Override
        protected boolean hasMore() {
            return false;
        }

    }


}
