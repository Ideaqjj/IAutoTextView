package com.idea.idea;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idea.idea.viewutils.AutoPagerAdapter;
import com.idea.idea.viewutils.AutoViewpager;
import com.idea.idea.viewutils.IdeaAutoTextview;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private IdeaAutoTextview mAutoText;

    private String[] names = {"测试1","测试2.", "测试3", "测试4", "测试5", "测试6"};

    private List<String> textlist = new ArrayList<>();

    View layoutView = null;
    private AutoViewpager mAutoViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(layoutView);
        mAutoText = (IdeaAutoTextview) findViewById(R.id.iat_aotuo);
        textlist.add(0, "测试1");
        textlist.add(1, "测试2");
        textlist.add(2, "测试3");
        textlist.add(3, "测试4");
        textlist.add(4, "测试5");
        mAutoViewPager= (AutoViewpager) findViewById(R.id.av_viewpager);
        mAutoViewPager.setAutoTime(2);
        mAutoViewPager.setAdapter(new TestAutoAdapter(mAutoViewPager));

        mAutoText.getTextData(mAutoText, textlist, 2);


    }

    private class TestAutoAdapter extends AutoPagerAdapter {

        public TestAutoAdapter(AutoViewpager viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            TextView view = new TextView(container.getContext());
            view.setText(names[position]);
            view.setGravity(Gravity.CENTER);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getRealCount() {
            return names.length;
        }

    }

}
