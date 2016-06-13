package com.strong.googleplay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.strong.googleplay.R;
import com.strong.googleplay.activity.DetailActivity;
import com.strong.googleplay.adapter.DefaultAdapter;
import com.strong.googleplay.domain.SubjectInfo;
import com.strong.googleplay.holder.BaseHolder;
import com.strong.googleplay.protocol.SubjectProtocol;
import com.strong.googleplay.utils.UiUtils;
import com.strong.googleplay.view.LoadingPage;

import java.util.ArrayList;


public class SubjectFragment extends BaseFragment {

    private ArrayList<SubjectInfo> mSubjectInfoList;

    // 当Fragment挂载的activity创建的时候调用
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        show(); //加载数据并切换界面
    }

    @Override
    protected View createSuccessView() {

        ListView listView = new ListView(UiUtils.getContext());
        if (mSubjectInfoList != null) {
            listView.setAdapter(new SubjectAdapter(mSubjectInfoList,listView));
        }
        return listView;
    }


    @Override
    protected LoadingPage.LoadResult load() {

        SubjectProtocol subjectProtocol=new SubjectProtocol();
        mSubjectInfoList = subjectProtocol.load(0);
        return LoadResult(mSubjectInfoList);
    }


    public class SubjectAdapter extends DefaultAdapter<SubjectInfo> {

        public SubjectAdapter(ArrayList datas, ListView listView) {
            super(datas, listView);
        }

        @Override
        public void onInnerItemClick(int position) {
            Toast.makeText(UiUtils.getContext(), "点击" + getDatas().get(position).getDes(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public BaseHolder getHolder() {
            return new SubjectHolder();
        }

        @Override
        protected ArrayList<SubjectInfo> onLoad() {
            SubjectProtocol subjectProtocol=new SubjectProtocol();
            ArrayList<SubjectInfo> newDatas = subjectProtocol.load(getDatas().size());
            return newDatas;
        }
    }
    static class SubjectHolder extends BaseHolder<SubjectInfo>{

        ImageView item_icon;
        TextView item_txt;

        @Override
        public View initView() {
            View contentView = View.inflate(UiUtils.getContext(), R.layout.item_subject, null);
            item_icon = (ImageView) contentView.findViewById(R.id.item_icon);
            item_txt = (TextView) contentView.findViewById(R.id.item_txt);
            return contentView;
        }

        @Override
        public void setData(SubjectInfo data) {
            item_txt.setText(data.getDes());
            myBitmapUtils.display(item_icon,"http://127.0.0.1:8090/"+"image?name="+data.getUrl());
        }

    }
}
