package ext.arch.components.banner;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ext.arch.components.R;
import ext.arch.components.util.ArchCollections;
import jdk8.function.Function;

/**
 * Created by Administrator on 2017/7/5.
 */

public abstract class BannerAdapter<T> extends PagerAdapter {
    private List<String> titles;
    @NonNull
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
        imageView.setTag(R.id.arch_item_position, new ItemInfo<>(items.get(position), position));
        convert(items.get(position), imageView);
        container.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles == null || position > titles.size() - 1 ? super.getPageTitle(position) : titles.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (getCount() == 0) {
            return POSITION_NONE;
        }
        if (!(object instanceof View)) {
            return super.getItemPosition(object);
        }
        final View item = (View) object;
        final Object tag = item.getTag(R.id.arch_item_position);
        if (!(tag instanceof ItemInfo)) {
            return super.getItemPosition(object);
        }
        ItemInfo itemInfo = (ItemInfo) tag;
        if (itemInfo.position > getCount() - 1
                || !itemInfo.item.equals(this.items.get(itemInfo.position))) {
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    public abstract void convert(T t, ImageView imageView);

    private static class ItemInfo<T> {
        T item;
        int position;

        ItemInfo(T item, int position) {
            this.item = item;
            this.position = position;
        }
    }
}
