package ext.arch.components.banner;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ext.arch.components.util.ArchCollections;
import ext.java8.function.Function;

/**
 * Created by Administrator on 2017/7/5.
 */

public abstract class BannerAdapter<T> extends PagerAdapter {
    private List<String> titles;
    private List<T> items;

    public BannerAdapter() {
        items = Collections.emptyList();
    }

    public BannerAdapter(@NonNull List<T> items, @NonNull List<String> titles) {
        this.items = items;
        this.titles = titles;
    }

    public BannerAdapter(@NonNull List<T> items, @NonNull String[] titles) {
        this(items, Arrays.asList(titles));
    }

    public BannerAdapter(@NonNull List<T> items, @NonNull Function<T, String> function) {
        this(items, ArchCollections.mapList(items, function));
    }

    public void setItems(List<T> items, @NonNull Function<T, String> function) {
        this.items = items;
        this.titles = ArchCollections.mapList(items, function);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        convert(items.get(position), imageView);
        container.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles == null ? super.getPageTitle(position) : titles.get(position);
    }

    public abstract void convert(T t, ImageView imageView);
}
