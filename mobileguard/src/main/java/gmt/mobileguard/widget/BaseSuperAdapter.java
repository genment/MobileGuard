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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.widget
 * Created by Genment at 2015/12/16 16:57.
 */
public abstract class BaseSuperAdapter<T> extends RecyclerView.Adapter<SuperViewHolder>
        implements ListAdapter, SpinnerAdapter {

    private final LayoutInflater mLayoutInflater;
    private List<T> mDataList;
    private int mItemLayoutResId;
    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    private MultiItemTypeSupport<T> mMultiItemTypeSupport;
    private boolean mIsRecyclerView = true;

    public BaseSuperAdapter(@NonNull Context context, @LayoutRes int itemLayoutResId) {
        this(context, new ArrayList<T>(), itemLayoutResId, null);
    }

    public BaseSuperAdapter(@NonNull Context context, MultiItemTypeSupport<T> multiItemTypeSupport) {
        this(context, new ArrayList<T>(), -1, multiItemTypeSupport);
    }

    public BaseSuperAdapter(@NonNull Context context, @NonNull List<T> data,
                            @LayoutRes int itemLayoutResId) {
        this(context, data, itemLayoutResId, null);
    }

    public BaseSuperAdapter(@NonNull Context context, @NonNull List<T> data,
                            MultiItemTypeSupport<T> multiItemTypeSupport) {
        this(context, data, -1, multiItemTypeSupport);
    }

    protected BaseSuperAdapter(@NonNull Context context, @NonNull List<T> data,
                               @LayoutRes int itemLayoutResId,
                               @Nullable MultiItemTypeSupport<T> multiItemTypeSupport) {
        this.mDataList = data;
        this.mItemLayoutResId = itemLayoutResId;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mMultiItemTypeSupport = multiItemTypeSupport;
    }

    public void setMultiItemTypeSupport(MultiItemTypeSupport<T> multiItemTypeSupport) {
        this.mMultiItemTypeSupport = multiItemTypeSupport;
    }

    //********************************** AdapterView Adapter **********************************//
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
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

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public T getItem(int position) {
        return mDataList.get(position);
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
        return mDataList.isEmpty();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SuperViewHolder viewHolder;
        if (convertView == null) {
            mIsRecyclerView = false;
            viewHolder = createViewHolder(parent, getItemViewType(position));
            convertView = viewHolder.itemView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SuperViewHolder) convertView.getTag();
        }
        bindViewHolder(viewHolder, position);
        return convertView;
    }

    //********************************** RecyclerView Adapter **********************************//
    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = mItemLayoutResId;
        if (mMultiItemTypeSupport != null) {
            layout = mMultiItemTypeSupport.getLayoutResId(viewType);
        }
        View itemView = mLayoutInflater.inflate(layout, parent, false);
        return new SuperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SuperViewHolder holder, int position) {
        T obj = mDataList.get(position);
        convert(holder, obj);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
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

    //********************************** Abstract Method **********************************//

    /**
     * You MUST implement this method to convert the views. If you need to use multiple item type,
     * you can get the item type by {@link SuperViewHolder#getItemViewType()} method.
     *
     * @param holder view holder.
     * @param t      data object.
     */
    public abstract void convert(SuperViewHolder holder, T t);

    //********************************** MultiItemType **********************************//

    /**
     * From AdapterView Adapter
     */
    @Override
    public final int getViewTypeCount() {
        if (mMultiItemTypeSupport != null) {
            return mMultiItemTypeSupport.getViewTypeCount();
        }
        return 1;
    }

    /**
     * From All
     */
    @Override
    public final int getItemViewType(int position) {
        if (mMultiItemTypeSupport != null) {
            return mMultiItemTypeSupport.getItemViewType(position, mDataList.get(position));
        }
        return 0;
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
    //********************************** Data Operator **********************************//

    /**
     * Adds the specified object at the end of the list.
     *
     * @param object The object to add at the end of the list.
     */
    public void add(T object) {
        int position = mDataList.size();
        mDataList.add(object);
        if (mIsRecyclerView) {
            notifyItemInserted(position);
        } else {
            notifyAdapterViewDataSetChanged();
        }
    }

    /**
     * Inserts the specified object at the specified index in the list.
     *
     * @param object The object to insert into the list.
     * @param index  The index at which the object must be inserted.
     */
    public void insert(T object, int index) {
        mDataList.add(index, object);
        if (mIsRecyclerView) {
            notifyItemInserted(index);
        } else {
            notifyAdapterViewDataSetChanged();
        }
    }

    /**
     * Adds the specified Collection at the end of the list.
     *
     * @param collection The Collection to add at the end of the list.
     */
    public void addAll(Collection<? extends T> collection) {
        int start = mDataList.size();
        mDataList.addAll(collection);
        if (mIsRecyclerView) {
            notifyItemRangeInserted(start, collection.size());
        } else {
            notifyAdapterViewDataSetChanged();
        }
    }

    /**
     * Adds the specified Collection at the specified index in the list.
     *
     * @param collection The Collection to add at the end of the list.
     */
    public void insertAll(int index, Collection<? extends T> collection) {
        mDataList.addAll(index, collection);
        if (mIsRecyclerView) {
            notifyItemRangeInserted(index, collection.size());
        } else {
            notifyAdapterViewDataSetChanged();
        }
    }

    /**
     * Replaces the specified object at the list with the other specified object.
     */
    public void set(T oldObj, T newObj) {
        int index = mDataList.indexOf(oldObj);
        set(index, newObj);
    }

    /**
     * Replaces the object at the specified location in the list with the specified object.
     */
    public void set(int index, T object) {
        mDataList.set(index, object);
        if (mIsRecyclerView) {
            notifyItemChanged(index);
        } else {
            notifyAdapterViewDataSetChanged();
        }
    }

    /**
     * Removes the specified object from the list.
     *
     * @param object The object to remove.
     */
    public void remove(T object) {
        int position = mDataList.indexOf(object);
        mDataList.remove(object);
        if (mIsRecyclerView) {
            notifyItemRemoved(position);
        } else {
            notifyAdapterViewDataSetChanged();
        }
    }

    /**
     * Removes the object at the specified index from the list.
     *
     * @param index The object to remove.
     */
    public void remove(int index) {
        mDataList.remove(index);
        if (mIsRecyclerView) {
            notifyItemRemoved(index);
        } else {
            notifyAdapterViewDataSetChanged();
        }
    }

    /**
     * Replaces all objects at the list with the other list.
     */
    public void replaceAll(Collection<? extends T> collection) {
        int oldSize = mDataList.size();
        int newSize = collection.size();
        mDataList.clear();
        mDataList.addAll(collection);
        if (mIsRecyclerView) {
            if (oldSize > newSize) {
                notifyItemRangeRemoved(0, oldSize);
                notifyItemRangeInserted(0, oldSize);
            } else {
                notifyItemRangeChanged(0, newSize);
            }
        } else {
            notifyAdapterViewDataSetChanged();
        }
    }

    /**
     * Remove all elements from the list.
     */

    public void clear() {
        int size = mDataList.size();
        mDataList.clear();
        if (mIsRecyclerView) {
            notifyItemRangeRemoved(0, size);
        } else {
            notifyAdapterViewDataSetChanged();
        }
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained
     *                   in this adapter.
     */
    public void sort(Comparator<? super T> comparator) {
        Collections.sort(mDataList, comparator);
        if (mIsRecyclerView) {
            notifyItemRangeChanged(0, mDataList.size());
        } else {
            notifyAdapterViewDataSetChanged();
        }
    }
}
