package steelkiwi.com.customtestpager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import steelkiwi.com.customtestpager.R;
import steelkiwi.com.customtestpager.listener.CustomTouchListener;

/**
 * Created by yaroslav on 5/13/17.
 */

public class CountryPagerAdapter extends CustomPagerAdapter<CountryPagerAdapter.ViewHolder> {

    private CustomTouchListener touchListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pager_item_layout, parent, false);
        prepareDefaultParams(view);
        if(touchListener != null) {
            view.setOnTouchListener(touchListener);
        }
        return new ViewHolder(view);
    }

    private void prepareDefaultParams(View view) {
        view.setScaleX(.8f);
        view.setScaleY(.8f);
        view.setAlpha(.8f);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

    }

    @Override
    public int getCount() {
        return 10;
    }

    public static class ViewHolder extends CustomPagerAdapter.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setTouchListener(CustomTouchListener touchListener) {
        this.touchListener = touchListener;
    }
}
