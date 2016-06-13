package com.strong.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.strong.googleplay.R;
import com.strong.googleplay.domain.SubjectInfo;
import com.strong.googleplay.utils.BatmapHelper;
import com.strong.googleplay.utils.UiUtils;
import com.strong.googleplay.utils.bitmap.MyBitmapUtils;

/**
 * Created by Administrator on 2016/5/31.
 */
public abstract class BaseHolder<Data>  {

    private View contentView;
    public BitmapUtils bitmapUtils;
    public MyBitmapUtils myBitmapUtils;

    public BaseHolder() {
        bitmapUtils= BatmapHelper.getBitmapUtils();
        myBitmapUtils=MyBitmapUtils.getMyBitmapUtils();
        contentView = initView();
        contentView.setTag(this);

    }

    public View getContentView() {
        return contentView;
    }

    public abstract View initView();

    public abstract void setData(Data data);
}
