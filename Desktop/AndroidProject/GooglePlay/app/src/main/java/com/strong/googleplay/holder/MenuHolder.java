package com.strong.googleplay.holder;

import android.view.View;

import com.strong.googleplay.R;
import com.strong.googleplay.domain.UserInfo;
import com.strong.googleplay.utils.UiUtils;

/**
 * Created by Administrator on 2016/6/1.
 */
public class MenuHolder extends BaseHolder<UserInfo> {
    @Override
    public View initView() {
        View view = View.inflate(UiUtils.getContext(), R.layout.menu_holder, null);
        return view;
    }

    @Override
    public void setData(UserInfo userInfo) {

    }
}
