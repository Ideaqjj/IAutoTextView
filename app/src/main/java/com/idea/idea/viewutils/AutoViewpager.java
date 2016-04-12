package com.idea.idea.viewutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * todo：修改ViewPager方法实现文字滚动
 *
 * @author: Create by qjj
 * @email: gxuqjj@163.com
 */
public class AutoViewpager extends RelativeLayout{

	private VerticalViewPager mVerticalViewPager;
	private PagerAdapter mAdapter;
	private int autoPlayTime;
	private ScheduledExecutorService scheduledExecutorService;

	public AutoViewpager(Context context){
		this(context,null);
	}

	public AutoViewpager(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public AutoViewpager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}


	/**
	 * 初始化view
	 */
	private void initView(){
		if(mVerticalViewPager!=null){
			removeView(mVerticalViewPager);
		}
		mVerticalViewPager = new VerticalViewPager(getContext());
		mVerticalViewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(mVerticalViewPager);

	}

    private final static class TimeTaskHandler extends Handler {
        private WeakReference<AutoViewpager> mRollPagerViewWeakReference;

        public TimeTaskHandler(AutoViewpager autoViewpager) {
            this.mRollPagerViewWeakReference = new WeakReference<>(autoViewpager);
        }

        @Override
        public void handleMessage(Message msg) {
            AutoViewpager autoViewpager = mRollPagerViewWeakReference.get();
            int cur = autoViewpager.getViewPager().getCurrentItem()+1;
            if(cur>= autoViewpager.mAdapter.getCount()){
                cur=0;
            }
            autoViewpager.getViewPager().setCurrentItem(cur);

        }
    }
    private TimeTaskHandler mHandler = new TimeTaskHandler(this);

    private static class WeakTimerTask extends TimerTask {
        private WeakReference<AutoViewpager> mRollPagerViewWeakReference;

        public WeakTimerTask(AutoViewpager mAutoViewpager) {
            this.mRollPagerViewWeakReference = new WeakReference<>(mAutoViewpager);
        }

        @Override
        public void run() {
            AutoViewpager autoViewpager = mRollPagerViewWeakReference.get();
            if (autoViewpager !=null){
                if(autoViewpager.isShown()){
                    autoViewpager.mHandler.sendEmptyMessage(0);
                }
            }else{
                cancel();
            }
        }
    }

	/**
	 * 开始轮播
	 */
	private void autoPlay(){
		if(autoPlayTime<=0||mAdapter == null||mAdapter.getCount()<=1){
			return;
		}

		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleWithFixedDelay(new WeakTimerTask(this), autoPlayTime, autoPlayTime, TimeUnit.SECONDS);
	}

    public void setAutoTime(int autoPlayTime){
        this.autoPlayTime = autoPlayTime;
        autoPlay();
    }


	/**
	 * viewpager
	 * @return
	 */
	public ViewPager getViewPager() {
		return mVerticalViewPager;
	}

	/**
	 * 设置Adapter
	 * @param adapter
	 */
	public void setAdapter(PagerAdapter adapter){
		mVerticalViewPager.setAdapter(adapter);
		mAdapter = adapter;
		dataChanged();
	}
	private void dataChanged(){
		autoPlay();
	}

}
