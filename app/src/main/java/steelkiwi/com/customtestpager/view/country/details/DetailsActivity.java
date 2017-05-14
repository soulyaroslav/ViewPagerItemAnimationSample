package steelkiwi.com.customtestpager.view.country.details;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;

import steelkiwi.com.customtestpager.R;
import steelkiwi.com.customtestpager.listener.CustomTouchListener;

/**
 * Created by yaroslav on 5/13/17.
 */

public class DetailsActivity extends Activity {

    private CountryDetailContract.View view;
    private CountryDetailContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        view = new CountryDetailView(this);
        presenter = new CountryDetailPresenter(this, view);
    }
}
