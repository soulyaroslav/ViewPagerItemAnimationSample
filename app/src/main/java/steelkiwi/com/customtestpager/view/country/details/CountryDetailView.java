package steelkiwi.com.customtestpager.view.country.details;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import steelkiwi.com.customtestpager.R;
import steelkiwi.com.customtestpager.listener.CustomTouchListener;

/**
 * Created by yaroslav on 5/14/17.
 */

public class CountryDetailView implements CountryDetailContract.View {

    private Activity activity;
    private View view;
    // views
    private ImageView countryImage;

    public CountryDetailView(Activity activity) {
        this.activity = activity;
        initViews();
    }

    private void initViews() {
        view = activity.findViewById(R.id.root);
        countryImage = (ImageView) view.findViewById(R.id.countryImage);
    }

    @Override
    public void setCustomTouchListener(CustomTouchListener listener) {
        countryImage.setOnTouchListener(listener);
    }
}
