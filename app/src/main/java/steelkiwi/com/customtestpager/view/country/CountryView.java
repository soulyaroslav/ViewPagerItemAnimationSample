package steelkiwi.com.customtestpager.view.country;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;

import steelkiwi.com.customtestpager.R;
import steelkiwi.com.customtestpager.adapter.CountryPagerAdapter;
import steelkiwi.com.customtestpager.adapter.CustomPagerAdapter;
import steelkiwi.com.customtestpager.listener.CustomTouchListener;

/**
 * Created by yaroslav on 5/14/17.
 */

public class CountryView implements CountryContract.View {

    private Activity activity;
    private View view;
    // views
    private ViewPager viewPager;
    // adapter
    private CountryPagerAdapter adapter;

    public CountryView(Activity activity) {
        this.activity = activity;
        initViews();
        init();
    }

    private void init() {
        prepareViewPagerAdapter();
    }

    private void initViews() {
        view = activity.findViewById(R.id.root);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
    }

    @Override
    public void prepareViewPagerAdapter() {
        adapter = new CountryPagerAdapter();
        viewPager.setAdapter(adapter);
    }

    @Override
    public void setCustomTouchListener(CustomTouchListener listener) {
        adapter.setTouchListener(listener);
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        viewPager.addOnPageChangeListener(listener);
    }

    @Override
    public void setPageMargin(int margin) {
        viewPager.setPageMargin(margin);
    }

    @Override
    public void setPadding(int padding) {
        viewPager.setPadding(padding, 0, padding, 0);
    }

    @Override
    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public CountryPagerAdapter getAdapter() {
        return adapter;
    }
}
