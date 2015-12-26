package gmt.mobileguard.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.FilterQueryProvider;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.widget
 * Created by Genment at 2015/12/22 16:57.
 */
public abstract class SuperCursorAdapter extends BaseSuperAdapter<Cursor, SuperViewHolder>
        implements CursorFilter.CursorFilterClient {

    private boolean mDataValid;
    private boolean mAutoRequery;
    private int mRowIDColumn;
    private Cursor mCursor;
    private ChangeObserver mChangeObserver;
    private DataSetObserver mDataSetObserver;
    private CursorFilter mCursorFilter;
    private FilterQueryProvider mFilterQueryProvider;

    @Deprecated
    public static final int FLAG_AUTO_REQUERY = 0x01;

    public static final int FLAG_REGISTER_CONTENT_OBSERVER = 0x02;

    public SuperCursorAdapter(@NonNull Context context, @NonNull Cursor cursor,
                              @LayoutRes int itemLayoutResId, boolean autoRequery) {
        this(context, cursor, itemLayoutResId, null,
                autoRequery ? FLAG_AUTO_REQUERY : FLAG_REGISTER_CONTENT_OBSERVER);
    }

    public SuperCursorAdapter(@NonNull Context context, @NonNull Cursor cursor,
                              @LayoutRes int itemLayoutResId, int flags) {
        this(context, cursor, itemLayoutResId, null, flags);
    }

    public SuperCursorAdapter(@NonNull Context context, @NonNull Cursor cursor,
                              @Nullable MultiItemTypeSupport<Cursor> multiTypeSupport,
                              boolean autoRequery) {
        this(context, cursor, -1, multiTypeSupport,
                autoRequery ? FLAG_AUTO_REQUERY : FLAG_REGISTER_CONTENT_OBSERVER);
    }

    public SuperCursorAdapter(@NonNull Context context, @NonNull Cursor cursor,
                              @Nullable MultiItemTypeSupport<Cursor> multiTypeSupport, int flags) {
        this(context, cursor, -1, multiTypeSupport, flags);
    }

    protected SuperCursorAdapter(@NonNull Context context, @NonNull Cursor cursor,
                                 @LayoutRes int itemLayoutResId,
                                 @Nullable MultiItemTypeSupport<Cursor> multiTypeSupport, int flags) {
        super(context, itemLayoutResId, multiTypeSupport);
        init(context, cursor, flags);
    }

    private void init(Context context, Cursor cursor, int flags) {
        if ((flags & FLAG_AUTO_REQUERY) == FLAG_AUTO_REQUERY) {
            flags |= FLAG_REGISTER_CONTENT_OBSERVER;
            mAutoRequery = true;
        } else {
            mAutoRequery = false;
        }
        boolean cursorPresent = cursor != null;
        mCursor = cursor;
        mDataValid = cursorPresent;
        mRowIDColumn = cursorPresent ? cursor.getColumnIndexOrThrow("_id") : -1;
        if ((flags & FLAG_REGISTER_CONTENT_OBSERVER) == FLAG_REGISTER_CONTENT_OBSERVER) {
            mChangeObserver = new ChangeObserver();
            mDataSetObserver = new MyDataSetObserver();
        } else {
            mChangeObserver = null;
            mDataSetObserver = null;
        }

        if (cursorPresent) {
            if (mChangeObserver != null) cursor.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) cursor.registerDataSetObserver(mDataSetObserver);
        }
        setHasStableIds(true);
    }

    @Override
    public int getCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public Cursor getItem(int position) {
        if (mDataValid && mCursor != null) {
            mCursor.moveToPosition(position);
            return mCursor;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                return mCursor.getLong(mRowIDColumn);
            }
        }
        return 0;
    }

    @Override
    public Cursor getCursor() {
        return mCursor;
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     *
     * @param cursor The new cursor to be used
     */
    @Override
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     *
     * @param newCursor The new cursor to be used.
     * @return Returns the previously set Cursor, or null if there wasa not one.
     * If the given new Cursor is the same instance is the previously set
     * Cursor, null is also returned.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        int oldSize = 0, newSize;
        Cursor oldCursor = mCursor;
        if (oldCursor != null) {
            oldSize = oldCursor.getCount();
            if (mChangeObserver != null) oldCursor.unregisterContentObserver(mChangeObserver);
            if (mDataSetObserver != null) oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (newCursor != null) {
            newSize = newCursor.getCount();
            if (mChangeObserver != null) newCursor.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) newCursor.registerDataSetObserver(mDataSetObserver);
            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            // notify the observers about the new cursor
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
        } else {
            mRowIDColumn = -1;
            mDataValid = false;
            // notify the observers about the lack of a data set
            if (mIsRecyclerView) {
                notifyItemRangeRemoved(0, oldSize);
            } else {
                notifyAdapterViewDataSetInvalidated();
            }
        }
        return oldCursor;
    }

    /**
     * Runs a query with the specified constraint. This query is requested
     * by the filter attached to this adapter.
     * <p/>
     * The query is provided by a
     * {@link android.widget.FilterQueryProvider}.
     * If no provider is specified, the current cursor is not filtered and returned.
     * <p/>
     * After this method returns the resulting cursor is passed to {@link #changeCursor(Cursor)}
     * and the previous cursor is closed.
     * <p/>
     * This method is always executed on a background thread, not on the
     * application's main thread (or UI thread.)
     * <p/>
     * Contract: when constraint is null or empty, the original results,
     * prior to any filtering, must be returned.
     *
     * @param constraint the constraint with which the query must be filtered
     * @return a Cursor representing the results of the new query
     * @see #getFilter()
     * @see #getFilterQueryProvider()
     * @see #setFilterQueryProvider(android.widget.FilterQueryProvider)
     */
    @Override
    @WorkerThread
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (mFilterQueryProvider != null) {
            return mFilterQueryProvider.runQuery(constraint);
        }

        return mCursor;
    }

    /**
     * <p>Converts the cursor into a CharSequence. Subclasses should override this
     * method to convert their results. The default implementation returns an
     * empty String for null values or the default String representation of
     * the value.</p>
     *
     * @param cursor the cursor to convert to a CharSequence
     * @return a CharSequence representing the value
     */
    @Override
    public CharSequence convertToString(Cursor cursor) {
        return cursor == null ? "" : cursor.toString();
    }

    public Filter getFilter() {
        if (mCursorFilter == null) {
            mCursorFilter = new CursorFilter(this);
        }
        return mCursorFilter;
    }

    /**
     * Returns the query filter provider used for filtering. When the
     * provider is null, no filtering occurs.
     *
     * @return the current filter query provider or null if it does not exist
     * @see #setFilterQueryProvider(android.widget.FilterQueryProvider)
     * @see #runQueryOnBackgroundThread(CharSequence)
     */
    public FilterQueryProvider getFilterQueryProvider() {
        return mFilterQueryProvider;
    }

    /**
     * Sets the query filter provider used to filter the current Cursor.
     * The provider's
     * {@link android.widget.FilterQueryProvider#runQuery(CharSequence)}
     * method is invoked when filtering is requested by a client of
     * this adapter.
     *
     * @param filterQueryProvider the filter query provider or null to remove it
     * @see #getFilterQueryProvider()
     * @see #runQueryOnBackgroundThread(CharSequence)
     */
    public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
        mFilterQueryProvider = filterQueryProvider;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (mDataValid) {
            mCursor.moveToPosition(position);
            return super.getView(position, convertView, parent);
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        return super.getView(position, convertView, parent);
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

    /**
     * Called when the {@link ContentObserver} on the cursor receives a change notification.
     * The default implementation provides the auto-requery logic, but may be overridden by
     * sub classes.
     *
     * @see ContentObserver#onChange(boolean)
     */
    protected void onContentChanged() {
        if (mAutoRequery && mCursor != null && !mCursor.isClosed()) {
            mDataValid = mCursor.requery();
        }
    }

    private class ChangeObserver extends ContentObserver {
        public ChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            onContentChanged();
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }
    }

    private class MyDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            mDataValid = true;
            if (mIsRecyclerView) {
                notifyRecyclerViewDataSetChanged();
            } else {
                notifyAdapterViewDataSetChanged();
            }
        }

        @Override
        public void onInvalidated() {
            mDataValid = false;
            if (mIsRecyclerView) {
                notifyRecyclerViewDataSetChanged();
            } else {
                notifyAdapterViewDataSetInvalidated();
            }
        }
    }
}
