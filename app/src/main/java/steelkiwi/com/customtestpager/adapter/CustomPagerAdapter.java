package steelkiwi.com.customtestpager.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by yaroslav on 5/13/17.
 */

public abstract class CustomPagerAdapter<VH extends CustomPagerAdapter.ViewHolder> extends PagerAdapter {

    public static abstract class ViewHolder {
        final View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }

    private Queue<VH> destroyedItems = new LinkedList<>();
    private SparseArray<View> pages = new SparseArray<>();

    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        VH viewHolder = destroyedItems.poll();
        if (viewHolder != null) {
            // Re-add existing view before rendering so that we can make change inside getView()
            container.addView(viewHolder.itemView);
            onBindViewHolder(viewHolder, position);
        } else {
            viewHolder = onCreateViewHolder(container);
            onBindViewHolder(viewHolder, position);
            container.addView(viewHolder.itemView);
        }
        pages.put(position, viewHolder.itemView);
        return viewHolder;
    }

    @Override
    public final void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((VH) object).itemView);
        destroyedItems.add((VH) object);
        pages.remove(position);
    }

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return ((VH) object).itemView == view;
    }

    public View getViewByPosition(final int position) {
        return pages.get(position);
    }

    public abstract VH onCreateViewHolder(ViewGroup parent);
    public abstract void onBindViewHolder(VH viewHolder, int position);
}
