package gmt.mobileguard.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.widget
 * Created by Genment at 2015/12/21 08:00.
 */
public abstract class EnhancedSuperAdapter<T> extends BaseSuperAdapter<T> {

    public EnhancedSuperAdapter(@NonNull Context context, @NonNull List<T> data,
                                @LayoutRes int itemLayoutResId) {
        super(context, data, itemLayoutResId);
    }

    protected EnhancedSuperAdapter(@NonNull Context context, @NonNull List<T> data,
                                   @LayoutRes int itemLayoutResId,
                                   @Nullable MultiItemTypeSupport<T> multiItemTypeSupport) {
        super(context, data, itemLayoutResId, multiItemTypeSupport);
    }

    public EnhancedSuperAdapter(@NonNull Context context, @NonNull List<T> data,
                                MultiItemTypeSupport<T> multiItemTypeSupport) {
        super(context, data, multiItemTypeSupport);
    }

    public EnhancedSuperAdapter(@NonNull Context context, @LayoutRes int itemLayoutResId) {
        super(context, itemLayoutResId);
    }

    public EnhancedSuperAdapter(@NonNull Context context,
                                MultiItemTypeSupport<T> multiItemTypeSupport) {
        super(context, multiItemTypeSupport);
    }

    @Override
    public void convert(SuperViewHolder holder, T t) {
        Object obj = holder.getAssociatedObject();
        convert(holder, t, obj == null || !obj.equals(t));
        holder.setAssociatedObject(t);
    }

    /**
     * You MUST implement this method to convert the views. If you need to use multiple item type,
     * you can get the item type by {@link SuperViewHolder#getItemViewType()} method.
     *
     * @param holder      View Holder.
     * @param t           Object data. <strong>Note:</strong> This object should be override the
     *                    {@link T#equals(Object)}.
     * @param itemChanged Whether or not the data object was changed.
     */
    public abstract void convert(SuperViewHolder holder, T t, boolean itemChanged);
}
