package com.strong.googleplay.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.strong.googleplay.R;
import com.strong.googleplay.global.GlobalContants;
import com.strong.googleplay.utils.LogUtils;
import com.strong.googleplay.utils.UiUtils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/5/31.
 */
public class HomePictureHolder extends BaseHolder<ArrayList<String >> {

    private ViewPager viewPager;
    private ArrayList<String> mPictures;
    private AutoRunTask autoRunTask;

    @Override
    public View initView() {

        View view = View.inflate(UiUtils.getContext(), R.layout.home_picture_holder, null);
        viewPager = (ViewPager) view.findViewById(R.id.vp_home_picture);
//        viewPager = new ViewPager(UiUtils.getContext());
//        //viewPager没设置数前  默认高度为0 所以必须指定一个高度
//        viewPager.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                UiUtils.getDimens(R.dimen.home_picture_height)));
        return view;
    }

    @Override
    public void setData(ArrayList<String> datas) {
        mPictures = datas;
        viewPager.setAdapter(new HomeAdpapter());
        viewPager.setCurrentItem(1000*mPictures.size());
        autoRunTask = new AutoRunTask();
        autoRunTask.start();
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        autoRunTask.stop();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        autoRunTask.start();
                        break;
                }
                return false;//返回fals  不处理事件
            }
        });
    }

    boolean flag;
    public class AutoRunTask implements Runnable {

        @Override
        public void run() {
            if (flag) {
                int pos=viewPager.getCurrentItem();
                pos++;
                viewPager.setCurrentItem(pos);
                UiUtils.postDelayed(this, 5000);
            }

        }

        public void start() {
            if (!flag) {
                flag = true;
                UiUtils.postDelayed(this, 3000);
            }
        }

        public void stop() {
            if (flag) {
                flag = false;
                UiUtils.cancelDelayed(this);
            }
        }
    }


    private class HomeAdpapter extends PagerAdapter {

        LinkedList<ImageView> imageViewList = new LinkedList<>();//缓存复用

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView;
            if (imageViewList.size() != 0) {
                imageView = imageViewList.remove(0);
            } else {
                imageView = new ImageView(UiUtils.getContext());
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            myBitmapUtils.display(imageView, GlobalContants.SERVER_URL+"/image?name="+mPictures.get(position%mPictures.size()));
            container.addView(imageView);
            imageView.setOnClickListener(myListener);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view= (ImageView) object;
            imageViewList.add(view);
            container.removeView(view);
        }
    }

     private View.OnClickListener myListener=new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             LogUtils.e("位置"+viewPager.getCurrentItem());
         }
     };
}
