package com.strong.googleplay.holder;

import android.view.View;
import android.widget.ImageView;

import com.strong.googleplay.R;
import com.strong.googleplay.domain.AppInfo;
import com.strong.googleplay.global.GlobalContants;
import com.strong.googleplay.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/2.
 */
public class DetailScreenHolder extends BaseHolder<AppInfo> {
    ImageView[] imageViews;
    @Override
    public View initView() {
        View view = View.inflate(UiUtils.getContext(), R.layout.detail_screen, null);
        imageViews = new ImageView[5];
        imageViews[0] = (ImageView) view.findViewById(R.id.screen_1);
        imageViews[1] = (ImageView) view.findViewById(R.id.screen_2);
        imageViews[2] = (ImageView) view.findViewById(R.id.screen_3);
        imageViews[3] = (ImageView) view.findViewById(R.id.screen_4);
        imageViews[4] = (ImageView) view.findViewById(R.id.screen_5);
        return view;
    }

    @Override
    public void setData(AppInfo info) {
        ArrayList<String> strings = info.getScreen();
        for (int i = 0; i < 5; i++) {
            if (i < strings.size()) {
                bitmapUtils.display(imageViews[i], GlobalContants.SERVER_URL + "/image?name=" + strings.get(i));
            } else {
                imageViews[i].setVisibility(View.GONE);
            }
        }
    }
}
