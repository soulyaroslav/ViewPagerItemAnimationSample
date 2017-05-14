package steelkiwi.com.customtestpager.view.country.details;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;

import steelkiwi.com.customtestpager.adapter.CountryPagerAdapter;
import steelkiwi.com.customtestpager.listener.CustomTouchListener;

/**
 * Created by yaroslav on 5/14/17.
 */

public class CountryDetailPresenter implements CountryDetailContract.Presenter {

    private Activity activity;
    private CountryDetailContract.View view;

    public CountryDetailPresenter(Activity activity, CountryDetailContract.View view) {
        this.activity = activity;
        this.view = view;
        init();
    }

    private void init() {
        setListeners();
    }

    @Override
    public void setListeners() {
        view.setCustomTouchListener(new CustomTouchListener(activity) {
            @Override
            public boolean onSwipeDown() {
                ActivityCompat.finishAfterTransition(activity);
                return true;
            }

            @Override
            public boolean onSwipeTop() {
                return false;
            }

            @Override
            public boolean onClick() {
                return false;
            }
        });
    }
}
