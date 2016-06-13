package com.strong.googleplay.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.strong.googleplay.R;
import com.strong.googleplay.domain.CategoryInfo;
import com.strong.googleplay.global.GlobalContants;
import com.strong.googleplay.utils.UiUtils;

/**
 * Created by Administrator on 2016/6/3.
 */
public class CategoryContentHolder extends BaseHolder<CategoryInfo> {

    ImageView[] ivs;
    TextView[] tvs;

    @Override
    public View initView() {

        View view = View.inflate(UiUtils.getContext(), R.layout.item_category_content, null);
        ivs = new ImageView[3];
        ivs[0] = (ImageView) view.findViewById(R.id.iv_1);
        ivs[1] = (ImageView) view.findViewById(R.id.iv_2);
        ivs[2] = (ImageView) view.findViewById(R.id.iv_3);

        tvs = new TextView[3];
        tvs[0] = (TextView) view.findViewById(R.id.tv_1);
        tvs[1] = (TextView) view.findViewById(R.id.tv_2);
        tvs[2] = (TextView) view.findViewById(R.id.tv_3);

        return view;
    }

    @Override
    public void setData(CategoryInfo info) {
        if (!TextUtils.isEmpty(info.getName1()) && !TextUtils.isEmpty(info.getUrl1())) {
            tvs[0].setText(info.getName1());
            bitmapUtils.display(ivs[0], GlobalContants.SERVER_URL + "/image?name=" + info.getUrl1());
            tvs[0].setVisibility(View.VISIBLE);
            ivs[0].setVisibility(View.VISIBLE);
        } else {
            tvs[0].setVisibility(View.INVISIBLE);
            ivs[0].setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(info.getName2()) && !TextUtils.isEmpty(info.getUrl2())) {
            tvs[1].setText(info.getName2());
            bitmapUtils.display(ivs[1], GlobalContants.SERVER_URL + "/image?name=" + info.getUrl2());
            tvs[1].setVisibility(View.VISIBLE);
            ivs[1].setVisibility(View.VISIBLE);
        } else {
            tvs[1].setVisibility(View.INVISIBLE);
            ivs[1].setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(info.getName3()) && !TextUtils.isEmpty(info.getUrl3())) {
            tvs[2].setText(info.getName3());
            bitmapUtils.display(ivs[2], GlobalContants.SERVER_URL + "/image?name=" + info.getUrl3());
            tvs[2].setVisibility(View.VISIBLE);
            ivs[2].setVisibility(View.VISIBLE);
        } else {
            tvs[2].setVisibility(View.INVISIBLE);
            ivs[2].setVisibility(View.INVISIBLE);
        }
    }
}
