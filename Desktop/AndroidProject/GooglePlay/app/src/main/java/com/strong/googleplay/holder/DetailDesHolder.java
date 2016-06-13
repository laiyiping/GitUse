package com.strong.googleplay.holder;

import android.animation.ValueAnimator;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.strong.googleplay.R;
import com.strong.googleplay.domain.AppInfo;
import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.utils.UiUtils;

/**
 * Created by Administrator on 2016/6/2.
 */
public class DetailDesHolder extends BaseHolder<AppInfo> implements View.OnClickListener {

    @ViewInject(R.id.des_layout)
    RelativeLayout rlDesLayout;

    @ViewInject(R.id.des_content)
    TextView tvDesContent;
    @ViewInject(R.id.des_author)
    TextView tvDesAuthor;
    @ViewInject(R.id.des_arrow)
    ImageView ivDesArrow;


    @Override
    public View initView() {

        View view = View.inflate(UiUtils.getContext(), R.layout.detail_des, null);
        ViewUtils.inject(this, view);

        return view;
    }

    @Override
    public void setData(AppInfo info) {
        tvDesContent.setText(info.getDes());
        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) tvDesContent.getLayoutParams();
        layoutParams.height=getShortMeasureHeight();
        tvDesContent.setLayoutParams(layoutParams);
        tvDesAuthor.setText("作者："+info.getAuthor());
        ivDesArrow.setOnClickListener(this);
    }


    private int getShortMeasureHeight() {

        TextView textView = new TextView(UiUtils.getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        textView.setLines(2);
        int width = tvDesContent.getMeasuredWidth();
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.AT_MOST);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        int height = textView.getMeasuredHeight();
        return height;
    }
    private int getLongMeasureHeight() {

        int width=tvDesContent.getMeasuredWidth();
        tvDesContent.getLayoutParams().height= ViewGroup.LayoutParams.WRAP_CONTENT;

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.AT_MOST);
        tvDesContent.measure(widthMeasureSpec, heightMeasureSpec);
        int height=tvDesContent.getMeasuredHeight();

        return height;
    }

    public ScrollView getScrollView(ViewGroup Group) {

        ViewParent viewParent = Group.getParent();
        if (viewParent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) viewParent;
            if (viewGroup instanceof ScrollView) {
                return (ScrollView) viewGroup;
            } else {
                return getScrollView(viewGroup);
            }
        } else {
            return null;
        }
    }


    boolean flag=false;
    ScrollView scrollView;
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.des_arrow) {
            scrollView = getScrollView(rlDesLayout);
            int start;
            int end;
            if (flag) {//收起
                flag = false;
                start=getLongMeasureHeight();
                end=getShortMeasureHeight();
                ivDesArrow.setImageResource(R.drawable.arrow_down);

            } else { //展开
                flag = true;
                start=getShortMeasureHeight();
                end=getLongMeasureHeight();
                ivDesArrow.setImageResource(R.drawable.arrow_up);
            }

            ValueAnimator valueAnimator=new ValueAnimator();
            valueAnimator.setIntValues(start, end);
            valueAnimator.setDuration(500);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int height= (int) animation.getAnimatedValue();
                    RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) tvDesContent.getLayoutParams();
                    layoutParams.height=height;
                    tvDesContent.setLayoutParams(layoutParams);
                    LogUtils.e(""+height);
                    scrollView.scrollTo(0,scrollView.getMeasuredHeight() );
                }
            });
            valueAnimator.start();
        }
    }
}
