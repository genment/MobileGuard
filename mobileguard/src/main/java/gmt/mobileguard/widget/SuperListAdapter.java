package gmt.mobileguard.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

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
public abstract class SuperListAdapter<T> extends BaseSuperAdapter<T, SuperViewHolder> {

    private List<T> mList;

    public SuperListAdapter(@NonNull Context context, @LayoutRes int itemLayoutResId) {
        this(context, new ArrayList<T>(), itemLayoutResId, null);
    }

    public SuperListAdapter(@NonNull Context context, MultiItemTypeSupport<T> multiTypeSupport) {
        this(context, new ArrayList<T>(), -1, multiTypeSupport);
    }

    public SuperListAdapter(@NonNull Context context, @NonNull List<T> data,
                            @LayoutRes int itemLayoutResId) {
        this(context, data, itemLayoutResId, null);
    }

    public SuperListAdapter(@NonNull Context context, @NonNull List<T> data,
                            MultiItemTypeSupport<T> multiTypeSupport) {
        this(context, data, -1, multiTypeSupport);
    }

    protected SuperListAdapter(@NonNull Context context, @NonNull List<T> data,
                               @LayoutRes int itemLayoutResId,
                               @Nullable MultiItemTypeSupport<T> multiTypeSupport) {
        super(context, itemLayoutResId, multiTypeSupport);
        this.mList = data;
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = mItemLayoutResId;
        if (mMultiTypeSupport != null) {
            layout = mMultiTypeSupport.getLayoutResId(viewType);
        }
        View itemView = mLayoutInflater.inflate(layout, parent, false);
        return new SuperViewHolder(itemView);
    }

    //********************************** Data Operator **********************************//

    /**
     * Adds the specified object at the end of the list.
     *
     * @param object The object to add at the end of the list.
     */
    public void add(T object) {
        int position = mList.size();
        mList.add(object);
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
        mList.add(index, object);
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
        int start = mList.size();
        mList.addAll(collection);
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
        mList.addAll(index, collection);
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
        int index = mList.indexOf(oldObj);
        set(index, newObj);
    }

    /**
     * Replaces the object at the specified location in the list with the specified object.
     */
    public void set(int index, T object) {
        mList.set(index, object);
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
        int position = mList.indexOf(object);
        mList.remove(object);
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
        mList.remove(index);
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
        int oldSize = mList.size();
        int newSize = collection.size();
        mList.clear();
        mList.addAll(collection);
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
        int size = mList.size();
        mList.clear();
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
        Collections.sort(mList, comparator);
        if (mIsRecyclerView) {
            notifyItemRangeChanged(0, mList.size());
        } else {
            notifyAdapterViewDataSetChanged();
        }
    }
}
