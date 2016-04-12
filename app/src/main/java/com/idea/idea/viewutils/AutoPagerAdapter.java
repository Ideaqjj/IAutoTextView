package com.idea.idea.viewutils;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * todo：适配器
 *
 * @author: Create by qjj
 * @email: gxuqjj@163.com
 */
public abstract class AutoPagerAdapter extends PagerAdapter {
    private AutoViewpager mVerticalViewPager;
    private List<View> vlist = new ArrayList<>();

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        vlist.clear();
    }

    public AutoPagerAdapter(AutoViewpager viewPager) {
        this.mVerticalViewPager = viewPager;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int rPostion = position % getRealCount();
        View itemView = viewPosition(container, rPostion);
        container.addView(itemView);
        return itemView;
    }


    private View viewPosition(ViewGroup container, int position) {
        for (View view : vlist) {
            if (((int) view.getTag()) == position && view.getParent() == null) {
                return view;
            }
        }
        View mView = getView(container, position);
        mView.setTag(position);
        vlist.add(mView);
        return mView;
    }

    public abstract View getView(ViewGroup container, int position);

    @Deprecated
    @Override
    public final int getCount() {
        return getRealCount() <= 1 ? getRealCount() : Integer.MAX_VALUE;
    }

    protected abstract int getRealCount();
}
