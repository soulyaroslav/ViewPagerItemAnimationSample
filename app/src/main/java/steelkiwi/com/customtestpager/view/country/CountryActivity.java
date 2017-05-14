package steelkiwi.com.customtestpager.view.country;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import steelkiwi.com.customtestpager.R;

/**
 * Created by yaroslav on 5/14/17.
 */

public class CountryActivity extends AppCompatActivity {

    private CountryContract.View view;
    private CountryContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countries_activity);
        view = new CountryView(this);
        presenter = new CountryPresenter(this, view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }
}
