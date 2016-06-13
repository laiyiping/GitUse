package com.strong.googleplay.view;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.strong.googleplay.R;
import com.strong.googleplay.manager.ThreadManager;
import com.strong.googleplay.utils.UiUtils;

/**
 * 创建自定义帧布局 将BaseFragment一部分代码抽取到这里
 * Created by Administrator on 2016/5/29.
 */
public abstract class LoadingPage extends FrameLayout {

    public static final int STATE_UNKOWN=0;
    public static final int STATE_LOADING=1;
    public static final int STATE_ERROR=2;
    public static final int STATE_EMPTY=3;
    public static final int STATE_SUCCESS=4;
    public int mCurrentState=STATE_UNKOWN;

    private View loadingView=null;
    private View errView=null;
    private View emptyView=null;
    private View successView=null;

//    private Activity mActivity;

    public LoadingPage(Context context) {
        super(context);

//        mActivity= (Activity) context;
        init();

    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);

//        mActivity= (Activity) context;
        init();

    }

    //在fl中加载不同状态界面
    private void init() {
        if (loadingView == null) {
            loadingView = createLoadingView();
            this.addView(loadingView,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));
        }
        if (errView == null) {
            errView = createErrView();
            this.addView(errView,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));
        }
        if (emptyView == null) {
            emptyView = createEmptyView();
            this.addView(emptyView,
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));
        }

        showPage();
    }



    private View createEmptyView() {
        return View.inflate(UiUtils.getContext(), R.layout.loadpage_empty, null);
    }

    private View createErrView() {
        View view = View.inflate(UiUtils.getContext(), R.layout.loadpage_error, null);
        Button button= (Button) view.findViewById(R.id.page_bt);
        ImageView imageView = (ImageView) view.findViewById(R.id.page_iv);
        View.OnClickListener clickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        };
        button.setOnClickListener(clickListener);
        imageView.setOnClickListener(clickListener);
        return view;
    }

    private View createLoadingView() {
        return View.inflate(UiUtils.getContext(), R.layout.loadpage_loading, null);
    }

    //根据不同状态显示不同界面
    private void showPage() {
        if (loadingView != null) {
            loadingView.setVisibility(mCurrentState==STATE_UNKOWN||mCurrentState==STATE_LOADING?
                    View.VISIBLE:View.INVISIBLE);
        }
        if (errView != null) {
            errView.setVisibility(mCurrentState == STATE_ERROR ? View.VISIBLE : View.INVISIBLE);
        }
        if (emptyView != null) {
            emptyView.setVisibility(mCurrentState == STATE_EMPTY ? View.VISIBLE : View.INVISIBLE);
        }
        if (mCurrentState == STATE_SUCCESS) {
            if (successView == null) {
                successView = createSuccessView();
                this.addView(successView,new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT));
            }
            successView.setVisibility(VISIBLE);
        }else {
            if (successView != null) {
                successView.setVisibility(INVISIBLE);
            }
        }
    }

    public enum LoadResult{
        error(2), empty(3), success(4);
        int value;

        LoadResult(int value) {
            this.value=value;
        }

        public int getValue() {
            return value;
        }
    }

    //根据服务器的数据  切换状态
    public void show() {

        if (mCurrentState == STATE_ERROR||mCurrentState==STATE_EMPTY) {
            mCurrentState=STATE_LOADING;
            showPage();
        }

        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                final LoadResult result = load();
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null) {
                            mCurrentState = result.getValue();
                            showPage();
                        }
                    }
                });
            }
        });
    }

    /**
     * 从网络获取数据
     * @return 获取结果的状态
     */
    protected abstract LoadResult load();
    protected abstract View createSuccessView();


}
