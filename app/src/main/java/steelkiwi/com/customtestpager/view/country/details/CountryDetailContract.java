package steelkiwi.com.customtestpager.view.country.details;

import steelkiwi.com.customtestpager.listener.CustomTouchListener;

/**
 * Created by yaroslav on 5/14/17.
 */

public interface CountryDetailContract {
    interface View {
        void setCustomTouchListener(CustomTouchListener listener);
    }

    interface Presenter {
        void setListeners();
    }
}
