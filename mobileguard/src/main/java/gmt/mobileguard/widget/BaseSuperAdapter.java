package gmt.mobileguard.widget;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.widget
 * Created by Genment at 2015/12/22 16:57.
 */
public abstract class BaseSuperAdapter<T, VH extends SuperViewHolder>
        extends RecyclerView.Adapter<VH>
        implements ListAdapter, SpinnerAdapter {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    protected int mItemLayoutResId;
    protected boolean mIsRecyclerView = true;
    protected LayoutInflater mLayoutInflater;
    protected MultiItemTypeSupport<T> mMultiTypeSupport;

    protected BaseSuperAdapter(@NonNull Context context, @LayoutRes int itemLayoutResId,
                               @Nullable MultiItemTypeSupport<T> multiTypeSupport) {
        mItemLayoutResId = itemLayoutResId;
        mLayoutInflater = LayoutInflater.from(context);
        mMultiTypeSupport = multiTypeSupport;
    }

    public void setMultiItemTypeSupport(MultiItemTypeSupport<T> multiTypeSupport) {
        this.mMultiTypeSupport = multiTypeSupport;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    /**
     * Notifies the attached observers that the underlying data is no longer valid
     * or available. Once invoked this adapter is no longer valid and should
     * not report further data set changes.
     * <p/>
     * <strong>ONLY</strong> for {@link android.widget.ListView ListView} and
     * {@link android.widget.GridView GridView}
     */
    public void notifyAdapterViewDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     * <p/>
     * <strong>ONLY</strong> for {@link android.widget.ListView ListView} and
     * {@link android.widget.GridView GridView}
     */
    public void notifyAdapterViewDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    /**
     * Notify any registered observers that the data set has changed.
     * <p/>
     * <strong>ONLY</strong> for {@link RecyclerView}
     *
     * @see #notifyItemChanged(int)
     * @see #notifyItemInserted(int)
     * @see #notifyItemRemoved(int)
     * @see #notifyItemRangeChanged(int, int)
     * @see #notifyItemRangeInserted(int, int)
     * @see #notifyItemRangeRemoved(int, int)
     */
    public final void notifyRecyclerViewDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return getCount() > 0;
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    @Override
    public abstract T getItem(int position);

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {
        VH viewHolder;
        if (convertView == null) {
            mIsRecyclerView = false;
            viewHolder = createViewHolder(parent, getItemViewType(position));
            convertView = viewHolder.itemView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (VH) convertView.getTag();
        }
        bindViewHolder(viewHolder, position);
        return convertView;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        convert(holder, getItem(position));
    }

    /**
     * You MUST implement this method to convert the views. If you need to use multiple item type,
     * you can get the item type by {@link SuperViewHolder#getItemViewType()} method.
     *
     * @param holder view holder.
     * @param t      data object.
     */
    public abstract void convert(VH holder, T t);

    @Override
    public final int getItemViewType(int position) {
        if (mMultiTypeSupport != null) {
            return mMultiTypeSupport.getItemViewType(position, getItem(position));
        }
        return 0;
    }

    @Override
    public final int getViewTypeCount() {
        if (mMultiTypeSupport != null) {
            return mMultiTypeSupport.getViewTypeCount();
        }
        return 1;
    }

    /**
     * @param <T> Data object Type.
     */
    public static abstract class MultiItemTypeSupport<T> {
        @LayoutRes
        protected abstract int getLayoutResId(int type);

        /**
         * 如果是 {@link AdapterView}，Type 必须是在 0 到 {@link #getViewTypeCount()} - 1 的范围内。
         * <p/>
         * 如果是 {@link RecyclerView}，Type 可以随意设置，建议使用 ID。
         *
         * @see android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)
         * @see android.widget.Adapter#getItemViewType(int)
         */
        protected abstract int getItemViewType(int position, T t);

        /**
         * AdapterView. Default 2.
         * <p/>
         * 如果是 {@link AdapterView}，不是必须 Override，但应该要 Override 该方法。
         * <p/>
         * 如果是 {@link RecyclerView}，不必 Override。
         *
         * @see Adapter#getViewTypeCount()
         */
        protected int getViewTypeCount() {
            return 2;
        }
    }
}
