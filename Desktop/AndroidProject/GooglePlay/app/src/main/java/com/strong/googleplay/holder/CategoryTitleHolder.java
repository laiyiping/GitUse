package com.strong.googleplay.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.strong.googleplay.domain.CategoryInfo;
import com.strong.googleplay.utils.UiUtils;

/**
 * Created by Administrator on 2016/6/3.
 */
public class CategoryTitleHolder extends BaseHolder<CategoryInfo> {

    private TextView textView;

    @Override
    public View initView() {
        textView = new TextView(UiUtils.getContext());
        textView.setTextColor(Color.BLACK);
        textView.setPadding(10,10,10,10);
        textView.setTextSize(22);
        textView.setText("标题");
        return textView;
    }

    @Override
    public void setData(CategoryInfo categoryInfo) {
        textView.setText(categoryInfo.getTitle());
    }
}
