package steelkiwi.com.customtestpager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;

import steelkiwi.com.customtestpager.listener.CustomTouchListener;

/**
 * Created by yaroslav on 5/13/17.
 */

public class DetailsActivity extends Activity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        imageView = (ImageView) findViewById(R.id.topImage);

        imageView.setOnTouchListener(new CustomTouchListener(this) {
            @Override
            public boolean onSwipeDown() {
                ActivityCompat.finishAfterTransition(DetailsActivity.this);
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
