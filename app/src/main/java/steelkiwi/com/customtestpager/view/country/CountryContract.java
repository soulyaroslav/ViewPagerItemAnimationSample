package steelkiwi.com.customtestpager.view.country;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import steelkiwi.com.customtestpager.adapter.CountryPagerAdapter;
import steelkiwi.com.customtestpager.listener.CustomTouchListener;

/**
 * Created by yaroslav on 5/14/17.
 */

public interface CountryContract {
    interface View {
        void prepareViewPagerAdapter();
        void setCustomTouchListener(CustomTouchListener listener);
        void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);
        void setPageMargin(int margin);
        void setPadding(int padding);
        ViewPager getViewPager();
        CountryPagerAdapter getAdapter();
    }

    interface Presenter {
        void onResume();
        void prepareViewPager();
        void prepareParameters();
        void startDetailActivity(android.view.View view);
        void expose();
        void changeAlpha(ViewGroup viewGroup, float alpha);
        ValueAnimator getHeightValueAnimator(final android.view.View view, int startHeight, int targetHeight, int duration);
        ValueAnimator getWidthValueAnimator(final android.view.View view, int startWidth, int targetWidth, int duration);
        ObjectAnimator getTranslationYAnimator(final android.view.View view, int value);
        void hide(final int position);
        void returnViewToDefaultState(final int position);
        int getCurrentPosition();
        android.view.View getCurrentView();
        android.view.View getCurrentView(final int position);
    }
}
