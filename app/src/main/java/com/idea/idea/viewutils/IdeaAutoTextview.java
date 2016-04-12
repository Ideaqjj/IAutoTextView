package com.idea.idea.viewutils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * todo：继承TextView并设置动画效果
 *
 * @author: Create by qjj
 * @email: gxuqjj@163.com
 */
public class IdeaAutoTextview extends TextView implements Animator.AnimatorListener {

    private static final int DURATION = 20;
    private float height;
    private AnimatorSet mAnimStart;
    private AnimatorSet mAnimOver;
    private String mText;
    private int index = 0;
    private ScheduledExecutorService scheduledExecutorService;
    private List<String> textList = new ArrayList<>();
    private IdeaAutoTextview textView;

    public IdeaAutoTextview(Context context) {
        super(context);
    }

    public IdeaAutoTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i("Tag", "--context--");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("Tag", "--onDraw--");
        height = getHeight();
    }

    /**
     * 获取数据并设置滚动播放
     *
     * @param textView
     * @param list
     * @param autoPlayTime
     */
    public void getTextData(final IdeaAutoTextview textView, List<String> list, int autoPlayTime) {
        this.textView = textView;
        this.textList = list;
        if (autoPlayTime != 0) {

            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleWithFixedDelay(new WeakTimerTask(this), autoPlayTime, autoPlayTime, TimeUnit.SECONDS);
        }

    }

    private TimeTaskHandler mHandler = new TimeTaskHandler(this);

    private static class WeakTimerTask extends TimerTask {
        private WeakReference<IdeaAutoTextview> autoTextReference;

        public WeakTimerTask(IdeaAutoTextview mautoText) {
            this.autoTextReference = new WeakReference<>(mautoText);
        }

        @Override
        public void run() {
            IdeaAutoTextview autoText = autoTextReference.get();
            if (autoText != null) {
                if (autoText.isShown()) {
                    autoText.mHandler.sendEmptyMessage(0);
                }
            } else {
                cancel();
            }
        }
    }

    /**
     * 异步切换文字
     */
    private final class TimeTaskHandler extends Handler {
        private WeakReference<IdeaAutoTextview> autoTextReference;

        public TimeTaskHandler(IdeaAutoTextview autoText) {
            this.autoTextReference = new WeakReference<>(autoText);
        }

        @Override
        public void handleMessage(Message msg) {
            IdeaAutoTextview autoText = autoTextReference.get();

            if (autoText != null) {
                /**
                 * 设置当前文字
                 */
                String text = textList.get(index);
                index++;
                if (index > textList.size() - 1) {
                    index = 0;
                }
                textView.setAutoText(text);
            }


        }
    }

    /**
     * 向上脱离屏幕的动画效果
     */
    private void animationStart() {
        ObjectAnimator translate = ObjectAnimator.ofFloat(this, "translationY", 0, -height);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        mAnimStart = new AnimatorSet();
        mAnimStart.play(translate).with(alpha);
        mAnimStart.setDuration(DURATION);
        mAnimStart.addListener(this);

    }

    /**
     * 从屏幕下面向上的动画效果
     */
    public void animationOver() {
        ObjectAnimator translate = ObjectAnimator.ofFloat(this, "translationY", height, 0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
        mAnimOver = new AnimatorSet();
        mAnimOver.play(translate).with(alpha);
        mAnimOver.setDuration(DURATION);

    }

    /**
     * --- 设置内容 ---
     **/
    public void setAutoText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mText = text;
        if (mAnimStart == null) {
            animationStart();
        }
        mAnimStart.start();
    }


    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        super.setText(mText);
        if (mAnimOver == null) {
            animationOver();
        }

        mAnimOver.start();
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

}
