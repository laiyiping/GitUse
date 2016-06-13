package com.strong.googleplay.holder;

import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.strong.googleplay.R;
import com.strong.googleplay.domain.AppInfo;
import com.strong.googleplay.global.GlobalContants;
import com.strong.googleplay.utils.UiUtils;

/**
 * Created by Administrator on 2016/6/1.
 */
public class DetailInfoHolder extends BaseHolder<AppInfo> {

    @ViewInject(R.id.item_icon)
    ImageView itemIcon;
    @ViewInject(R.id.item_title)
    TextView itemTitle;
    @ViewInject(R.id.item_rating)
    RatingBar itemRating;
    @ViewInject(R.id.item_download)
    TextView itemDownload;
    @ViewInject(R.id.item_version)
    TextView itemVersion;
    @ViewInject(R.id.item_date)
    TextView itemDate;
    @ViewInject(R.id.item_size)
    TextView itemSizw;

    //实例化布局和控件
    @Override
    public View initView() {

        View view = View.inflate(UiUtils.getContext(), R.layout.detail_app_info, null);
        ViewUtils.inject(this, view);
        return view;
    }

    //给控件设置数据
    @Override
    public void setData(AppInfo data) {

        bitmapUtils.display(itemIcon, GlobalContants.SERVER_URL+"/image?name="+ data.getIconUrl());
        itemTitle.setText(data.getName());
        itemRating.setRating(data.getStars());
        itemDownload.setText("下载："+data.getDownloadNum());
        itemVersion.setText("版本："+data.getVersion());
        itemDate.setText("时间："+data.getDate());
        itemSizw.setText("大小："+Formatter.formatFileSize(UiUtils.getContext(), data.getSize()));

    }
}
