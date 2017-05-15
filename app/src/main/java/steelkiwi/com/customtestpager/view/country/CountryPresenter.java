package steelkiwi.com.customtestpager.view.country;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import steelkiwi.com.customtestpager.view.country.details.DetailsActivity;
import steelkiwi.com.customtestpager.R;
import steelkiwi.com.customtestpager.listener.CustomTouchListener;

/**
 * Created by yaroslav on 5/14/17.
 */

public class CountryPresenter implements CountryContract.Presenter {

    private static final float SCALE_SIZE = .2f;
    private static final int DELAY = 500;
    private static final int MAX_CLICKS = 2;

    private Activity activity;
    private CountryContract.View view;
    private int startHeight;
    private int targetHeight;
    private int startWidth;
    private int targetWidth;
    private boolean isExpose = false;
    private int clickCount;
    // position to detect swipe direction;
    private int previousPosition;

    public CountryPresenter(Activity activity, CountryContract.View view) {
        this.activity = activity;
        this.view = view;
        init();
    }

    private void init() {
        prepareViewPager();
        prepareParameters();
    }

    @Override
    public void onResume() {
        if(isExpose()) {
            setClicks(1);
        }
    }

    @Override
    public void prepareViewPager() {
        int padding = activity.getResources().getDimensionPixelSize(R.dimen.padding);
        view.getViewPager().setClipToPadding(false);
        view.setPageMargin(0);
        view.setPadding(padding);
        view.setCustomTouchListener(touchListener);
        view.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public void prepareParameters() {
        // view start and target size for animation
        startHeight = activity.getResources().getDimensionPixelSize(R.dimen.start_height_background);
        targetHeight = activity.getResources().getDimensionPixelSize(R.dimen.target_height_background);
        // view start and target size for animation
        startWidth = activity.getResources().getDimensionPixelSize(R.dimen.start_width_background);
        targetWidth = activity.getResources().getDimensionPixelSize(R.dimen.target_width_background);
    }

    @Override
    public void startDetailActivity(View view) {
        Intent intent = new Intent(activity, DetailsActivity.class);
        ImageView imageView = (ImageView) view.findViewById(R.id.ivCountryImage);
        TextView textView = (TextView) view.findViewById(R.id.tvCountryName);
        Pair<View, String> pair1 = Pair.create((View) imageView, "detail");
        Pair<View, String> pair2 = Pair.create((View) textView, "detail_country_name");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair1, pair2);
        activity.startActivity(intent, options.toBundle());
    }

    @Override
    public void expose() {
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
        ObjectAnimator imageTranslationY = getTranslationYAnimator(background, 110);
        // translate view
        ObjectAnimator backgroundTranslationY = getTranslationYAnimator(image, -60);
        // set all animator together
        animatorSet.playTogether(heightValueAnimator, widthValueAnimator, imageTranslationY, backgroundTranslationY);
        animatorSet.start();
        // set flag that is view is expanded
        setExpose(true);
    }

    @Override
    public void changeAlpha(ViewGroup viewGroup, float alpha) {
        int count = viewGroup.getChildCount();
        for(int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            view.animate().alpha(alpha).setDuration(DELAY).start();
        }
    }

    @Override
    public ValueAnimator getHeightValueAnimator(final View view, int startHeight, int targetHeight, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();
                view.requestLayout();
            }
        });
        valueAnimator.setDuration(duration);
        return valueAnimator;
    }

    @Override
    public ValueAnimator getWidthValueAnimator(final View view, int startWidth, int targetWidth, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startWidth, targetWidth);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.getLayoutParams().width = (int) valueAnimator.getAnimatedValue();
                view.requestLayout();
            }
        });
        valueAnimator.setDuration(duration);
        return valueAnimator;
    }

    @Override
    public ObjectAnimator getTranslationYAnimator(View view, int value) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", value);
        objectAnimator.setDuration(DELAY);
        return objectAnimator;
    }

    @Override
    public void hide(int position) {
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

    @Override
    public void returnViewToDefaultState(int position) {
        if(getPreviousPosition() < position) {
            // left direction
            if (position > 0 && isExpose()) {
                hide(position - 1);
            }
        } else {
            // right direction
            if (position < view.getAdapter().getCount() - 1 && isExpose()) {
                hide(position + 1);
            }
        }
        setPreviousPosition(position);
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
            if(position < view.getAdapter().getCount() - 1) {
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

    private CustomTouchListener touchListener = new CustomTouchListener(activity) {
        @Override
        public boolean onSwipeDown() {
            int position = getCurrentPosition();
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
    };

    @Override
    public int getCurrentPosition() {
        return view.getViewPager().getCurrentItem();
    }

    @Override
    public View getCurrentView() {
        return view.getAdapter().getViewByPosition(getCurrentPosition());
    }

    @Override
    public View getCurrentView(final int position) {
        return view.getAdapter().getViewByPosition(position);
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

    private int getPreviousPosition() {
        return previousPosition;
    }

    private void setPreviousPosition(int previousPosition) {
        this.previousPosition = previousPosition;
    }
}
