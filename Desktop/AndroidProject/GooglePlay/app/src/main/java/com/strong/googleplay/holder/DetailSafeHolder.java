package com.strong.googleplay.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.strong.googleplay.R;
import com.strong.googleplay.domain.AppInfo;
import com.strong.googleplay.global.GlobalContants;
import com.strong.googleplay.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/2.
 */
public class DetailSafeHolder extends BaseHolder<AppInfo> implements View.OnClickListener{

    @ViewInject(R.id.safe_layout)
    RelativeLayout rlSafeLayout;
    ImageView[] ivs;
    @ViewInject(R.id.safe_arrow)
    ImageView ivSafeArrow;
    @ViewInject(R.id.safe_content)
    LinearLayout llSafeContent;
    ImageView[] ivDes;
    TextView[] tvs;
    LinearLayout[] llDes;

    @Override
    public View initView() {

        View view = View.inflate(UiUtils.getContext(), R.layout.detail_safe, null);
        ViewUtils.inject(this, view);

        rlSafeLayout.setOnClickListener(this);

        ivs = new ImageView[4];
        ivs[0] = (ImageView) view.findViewById(R.id.iv_1);
        ivs[1] = (ImageView) view.findViewById(R.id.iv_2);
        ivs[2] = (ImageView) view.findViewById(R.id.iv_3);
        ivs[3] = (ImageView) view.findViewById(R.id.iv_4);

        ivDes = new ImageView[4];
        ivDes[0] = (ImageView) view.findViewById(R.id.des_iv_1);
        ivDes[1] = (ImageView) view.findViewById(R.id.des_iv_2);
        ivDes[2] = (ImageView) view.findViewById(R.id.des_iv_3);
        ivDes[3] = (ImageView) view.findViewById(R.id.des_iv_4);

        tvs = new TextView[4];
        tvs[0] = (TextView) view.findViewById(R.id.des_tv_1);
        tvs[1] = (TextView) view.findViewById(R.id.des_tv_2);
        tvs[2] = (TextView) view.findViewById(R.id.des_tv_3);
        tvs[3] = (TextView) view.findViewById(R.id.des_tv_4);

        llDes = new LinearLayout[4];
        llDes[0] = (LinearLayout) view.findViewById(R.id.des_layout_1);
        llDes[1] = (LinearLayout) view.findViewById(R.id.des_layout_2);
        llDes[2] = (LinearLayout) view.findViewById(R.id.des_layout_3);
        llDes[3] = (LinearLayout) view.findViewById(R.id.des_layout_4);

//        安全内容默认收起来
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llSafeContent.getLayoutParams();
        layoutParams.height=0;
        llSafeContent.setLayoutParams(layoutParams);

        return view;
    }

    @Override
    public void setData(AppInfo info) {

        ArrayList<String> safeUrl=info.getSafeUrl();
        ArrayList<String> safeDesUrl=info.getSafeDesUrl();
        ArrayList<String> safeDes=info.getSafeDes();
        ArrayList<Integer> safeDesColor=info.getSafeDesColor();

        for (int i = 0; i < 4; i++) {
            if (i < safeUrl.size() && i < safeDesColor.size() && i < safeDesUrl.size() && i < safeDes.size()) {
                ivs[i].setVisibility(View.VISIBLE);
                llDes[i].setVisibility(View.VISIBLE);
                bitmapUtils.display(ivs[i], GlobalContants.SERVER_URL + "/image?name=" + safeUrl.get(i));
                bitmapUtils.display(ivDes[i], GlobalContants.SERVER_URL + "/image?name=" + safeDesUrl.get(i));

                int color;
                int colorType = safeDesColor.get(i);
                if (colorType > 0 && colorType < 4) {
                    color = Color.rgb(255, 153, 0);
                } else if (colorType == 4) {
                    color = Color.rgb(0, 177, 62);
                } else {
                    color = Color.rgb(122, 122, 122);
                }
                tvs[i].setTextColor(color);
                tvs[i].setText(safeDes.get(i));

            } else {
                ivs[i].setVisibility(View.GONE);
                llDes[i].setVisibility(View.GONE);
            }
        }

    }


    boolean flag=false;
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.safe_layout){
            int measureHeight = getMeasureHeight();
            int startHeight,endHeight;
            if (flag) {
                flag=false;    //关闭
                startHeight=measureHeight;
                endHeight=0;

            } else {
                flag=true;   //打开
                startHeight=0;
                endHeight = measureHeight;
            }
            ValueAnimator valueAnimator = ValueAnimator.ofInt(startHeight, endHeight);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value;
                    value= (int) animation.getAnimatedValue();
                    RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) llSafeContent.getLayoutParams();
                    layoutParams.height=value;
                    llSafeContent.setLayoutParams(layoutParams);
                }
            });
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (flag) {
                        ivSafeArrow.setImageResource(R.drawable.arrow_down);
                    } else {
                        ivSafeArrow.setImageResource(R.drawable.arrow_up);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            valueAnimator.setDuration(500);
            valueAnimator.start();
        }
    }

    private int getMeasureHeight() {

        int width=llSafeContent.getMeasuredWidth();//由于宽度不会发生变化，直接取出来
        llSafeContent.getLayoutParams().height= ViewGroup.LayoutParams.WRAP_CONTENT;//让宽度包裹内容

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int heightMessureSpec = View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.AT_MOST);
        llSafeContent.measure(widthMeasureSpec, heightMessureSpec);
        return llSafeContent.getMeasuredHeight();
    }
}
