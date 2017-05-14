package steelkiwi.com.customtestpager;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import steelkiwi.com.customtestpager.adapter.CountryPagerAdapter;
import steelkiwi.com.customtestpager.adapter.CustomPagerAdapter;
import steelkiwi.com.customtestpager.listener.CustomTouchListener;

public class MainActivity extends AppCompatActivity {

    private static final float SCALE_SIZE = .2f;
    private static final int DELAY = 500;
    private static final int MAX_CLICKS = 2;

    private ViewPager viewPager;
    private CustomPagerAdapter adapter;
    private int startHeight;
    private int targetHeight;
    private int startWidth;
    private int targetWidth;
    private boolean isExpose = false;
    private int clickCount;
    // position to detect swipe direction;
    private int previousPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareParameters();
        prepareViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isExpose()) {
            setClicks(1);
        }
    }

    private void prepareViewPager() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new CountryPagerAdapter(new CustomTouchListener(this) {
            @Override
            public boolean onSwipeDown() {
                int position = viewPager.getCurrentItem();
                if(isExpose()) {
                    hide(position);
                }
                setClicks(0);
                return true;
            }

            @Override
            public boolean onSwipeTop() {
                return false;
            }

            @Override
            public boolean onClick() {
               incrementClicks();
                if(getClicks() == MAX_CLICKS) {
                    View view = getCurrentView();
                    CardView image = (CardView) view.findViewById(R.id.lImage);
                    // start detail activity
                    startDetailActivity(image);
                    setClicks(0);
                } else {
                    if(!isExpose()) {
                        expose();
                    }
                }
                return true;
            }
        });
        viewPager.setAdapter(adapter);
        viewPager.setClipToPadding(false);
        int padding = getResources().getDimensionPixelSize(R.dimen.padding);
        viewPager.setPadding(padding, 0, padding, 0);
        viewPager.setPageMargin(0);
        viewPager.addOnPageChangeListener(onPageChangeListener);

    }

    private void prepareParameters() {
        // view start and target size for animation
        startHeight = getResources().getDimensionPixelSize(R.dimen.start_height_background);
        targetHeight = getResources().getDimensionPixelSize(R.dimen.target_height_background);
        // view start and target size for animation
        startWidth = getResources().getDimensionPixelSize(R.dimen.start_width_background);
        targetWidth = getResources().getDimensionPixelSize(R.dimen.target_width_background);
    }

    private void startDetailActivity(View view) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        ImageView imageView = (ImageView) view.findViewById(R.id.ivCountryImage);
        TextView textView = (TextView) view.findViewById(R.id.tvCountryName);
        Pair<View, String> pair1 = Pair.create((View) imageView, "detail");
        Pair<View, String> pair2 = Pair.create((View) textView, "detail_country_name");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getContext(), pair1, pair2);
        startActivity(intent, options.toBundle());
    }

    private void expose() {
        // get current view
        View view = getCurrentView();
        // find view what need to animation
        CardView image = (CardView) view.findViewById(R.id.lImage);
        final RelativeLayout background = (RelativeLayout) view.findViewById(R.id.lBackground);
        // change child alpha
        changeAlpha(background, 1f);
        // animation set instance
        AnimatorSet animatorSet = new AnimatorSet();
        // get height value for animation
        ValueAnimator heightValueAnimator = getHeightValueAnimator(background, startHeight, targetHeight, DELAY);
        // get width value for animation
        ValueAnimator widthValueAnimator = getWidthValueAnimator(background, startWidth, targetWidth, DELAY);
        // translate view
        ObjectAnimator imageTranslationY = getTranslationYAnimator(background, 150);
        // translate view
        ObjectAnimator backgroundTranslationY = getTranslationYAnimator(image, -70);
        // set all animator together
        animatorSet.playTogether(heightValueAnimator, widthValueAnimator, imageTranslationY, backgroundTranslationY);
        animatorSet.start();
        // set flag that is view is expanded
        setExpose(true);
    }

    private void changeAlpha(ViewGroup viewGroup, float alpha) {
        int count = viewGroup.getChildCount();
        for(int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            view.animate().alpha(alpha).setDuration(DELAY).start();
        }
    }

    private ValueAnimator getHeightValueAnimator(final View view, int startHeight, int targetHeight, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();
                view.requestLayout();
            }
        });
        valueAnimator.setDuration(DELAY);
        return valueAnimator;
    }

    private ValueAnimator getWidthValueAnimator(final View view, int startWidth, int targetWidth, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startWidth, targetWidth);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.getLayoutParams().width = (int) valueAnimator.getAnimatedValue();
                view.requestLayout();
            }
        });
        valueAnimator.setDuration(DELAY);
        return valueAnimator;
    }

    private ObjectAnimator getTranslationYAnimator(final View view, int value) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", value);
        objectAnimator.setDuration(DELAY);
        return objectAnimator;
    }

    private void hide(final int position) {
        // get current view
        View view = getCurrentView(position);
        // find view what need to animation
        CardView image = (CardView) view.findViewById(R.id.lImage);
        final RelativeLayout background = (RelativeLayout) view.findViewById(R.id.lBackground);
        // change child alpha
        changeAlpha(background, 0f);
        // animation set instance
        AnimatorSet animatorSet = new AnimatorSet();
        // get height value for animation
        ValueAnimator heightValueAnimator = getHeightValueAnimator(background, targetHeight, startHeight, 400);
        // get width value for animation
        ValueAnimator widthValueAnimator = getWidthValueAnimator(background, targetWidth, startWidth, 500);
        // translate view
        ObjectAnimator imageTranslationY = getTranslationYAnimator(background, 0);
        // translate view
        ObjectAnimator backgroundTranslationY = getTranslationYAnimator(image, 0);
        // set all animator together
        animatorSet.playTogether(heightValueAnimator, widthValueAnimator, imageTranslationY, backgroundTranslationY);
        animatorSet.start();
        // set flag that is view is not expanded
        setExpose(false);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            float currentScaleFactor = (1 - positionOffset) * SCALE_SIZE + (1 - SCALE_SIZE);
            float nextScaleFactor = (positionOffset * SCALE_SIZE) + (1 - SCALE_SIZE);
            View currentView = getCurrentView(position);
            currentView.setScaleX(currentScaleFactor);
            currentView.setScaleY(currentScaleFactor);
            currentView.setAlpha(currentScaleFactor);
            if(position < adapter.getCount() - 1) {
                View nextView = getCurrentView(position + 1);
                nextView.setScaleX(nextScaleFactor);
                nextView.setScaleY(nextScaleFactor);
                nextView.setAlpha(nextScaleFactor);
            }
        }

        @Override
        public void onPageSelected(int position) {
            returnViewToDefaultState(position);
            setClicks(0);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void returnViewToDefaultState(final int position) {
        if(getPreviousPosition() < position) {
            // left direction
            if (position > 0 && isExpose()) {
                hide(position - 1);
            }
        } else {
            // right direction
            if (position < adapter.getCount() - 1 && isExpose()) {
                hide(position + 1);
            }
        }
        setPreviousPosition(position);
    }

    private int getCurrentPosition() {
        return viewPager.getCurrentItem();
    }

    private View getCurrentView() {
        return adapter.getViewByPosition(getCurrentPosition());
    }

    private View getCurrentView(final int position) {
        return adapter.getViewByPosition(position);
    }

    private Activity getContext() {
        return this;
    }

    private void incrementClicks() {
        clickCount++;
    }

    private void setClicks(int clickCount) {
        this.clickCount = clickCount;
    }

    private int getClicks() {
        return clickCount;
    }

    private boolean isExpose() {
        return isExpose;
    }

    private void setExpose(boolean expose) {
        isExpose = expose;
    }

    public int getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(int previousPosition) {
        this.previousPosition = previousPosition;
    }
}
