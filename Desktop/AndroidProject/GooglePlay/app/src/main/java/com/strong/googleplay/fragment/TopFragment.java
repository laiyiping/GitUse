package com.strong.googleplay.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.strong.googleplay.protocol.TopProtocol;
import com.strong.googleplay.utils.DrawableUtils;
import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.utils.UiUtils;
import com.strong.googleplay.view.Flowlayout;
import com.strong.googleplay.view.LoadingPage;
import com.strong.googleplay.view.MyFlowlayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TopFragment extends BaseFragment {

    private ArrayList<String> mDatas;

    // 当Fragment挂载的activity创建的时候调用
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        show(); //加载数据并切换界面
    }

    @Override
    protected View createSuccessView() {
        LogUtils.e("新建 Flowlayout");
        MyFlowlayout flowlayout = new MyFlowlayout(UiUtils.getContext());
        int p = UiUtils.dip2px(13);
        flowlayout.setPadding(p,p,p,p);
        flowlayout.setBackgroundColor(Color.GRAY);


        ScrollView scrollView = new ScrollView(UiUtils.getContext());
        scrollView.addView(flowlayout);


        Drawable pressedDrawable = DrawableUtils.creatShape(0xffcecece);
        for (int i = 0; i < mDatas.size(); i++) {
            final TextView view = new TextView(UiUtils.getContext());

            Random random = new Random();
            int red=random.nextInt(150)+20;
            int green=random.nextInt(150)+20;
            int blue=random.nextInt(150)+20;
            GradientDrawable shape = DrawableUtils.creatShape(Color.rgb(red, green,blue));
            view.setBackgroundDrawable(DrawableUtils.creatStateListDrawable(pressedDrawable,shape));
            view.setTextColor(Color.WHITE);
            view.setTextSize(22);
            int topPadding = UiUtils.dip2px(4);
            int leftPadding = UiUtils.dip2px(7);
            view.setPadding(leftPadding,topPadding,leftPadding,topPadding);
            view.setText(mDatas.get(i));
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UiUtils.getContext(),view.getText(),Toast.LENGTH_SHORT).show();
                }
            });
            flowlayout.addView(view,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        return scrollView;
    }


    @Override
    protected LoadingPage.LoadResult load() {
        TopProtocol topProtocol=new TopProtocol();
        mDatas =topProtocol.load(0);
        return LoadResult(mDatas);
    }
}
