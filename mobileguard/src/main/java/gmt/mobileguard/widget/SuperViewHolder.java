package gmt.mobileguard.widget;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;


/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.widget
 * Created by Genment at 2015/12/16 17:17.
 */
public class SuperViewHolder extends RecyclerView.ViewHolder {

    /**
     * Views indexed with their IDs.
     */
    private final SparseArray<View> mViews;

    /**
     * Package private field to retain the associated user object and detect a change
     */
    private Object mAssociatedObject;

    protected SuperViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    /**
     * Get the view (such as TextView, ImageView, Button, etc.) that in the ItemView.
     *
     * @param viewId the id of the view.
     * @return view.
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param resId  The text to put in the text view.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setText(@IdRes int viewId, @StringRes int resId) {
        TextView tv = getView(viewId);
        tv.setText(resId);
        return this;
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setText(@IdRes int viewId, CharSequence value) {
        TextView tv = getView(viewId);
        tv.setText(value);
        return this;
    }

    /**
     * Will set the drawables of a TextView.
     *
     * @param viewId The view id.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setCompoundDrawables(@IdRes int viewId,
                                               @DrawableRes int left, @DrawableRes int top,
                                               @DrawableRes int right, @DrawableRes int bottom) {
        TextView tv = getView(viewId);
        tv.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        return this;
    }

    /**
     * Will set the drawables of a TextView.
     *
     * @param viewId The view id.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setCompoundDrawables(@IdRes int viewId,
                                               @Nullable Drawable left, @Nullable Drawable top,
                                               @Nullable Drawable right, @Nullable Drawable bottom) {
        TextView tv = getView(viewId);
        tv.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        return this;
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param viewId     The view id.
     * @param imageResId The image resource id.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setImageDrawable(@IdRes int viewId, @Nullable Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * Add an action to set the image of an image view. Can be called multiple times.
     */
    public SuperViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Will set background color of a view.
     *
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * Will set background of a view.
     *
     * @param viewId        The view id.
     * @param backgroundRes A resource to use as a background.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setBackgroundRes(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * Will set background of a view.
     *
     * @param viewId   The view id.
     * @param drawable The Drawable to use as the background, or null to remove the background
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setBackground(@IdRes int viewId, @Nullable Drawable drawable) {
        View view = getView(viewId);
        //noinspection deprecation
        view.setBackgroundDrawable(drawable);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }
//*************************************************************//
//
//    /**
//     * Will set text color of a TextView.
//     *
//     * @param viewId       The view id.
//     * @param textColorRes The text color resource id.
//     * @return The SuperViewHolder for chaining.
//     */
//    public SuperViewHolder setTextColorRes(int viewId, int textColorRes) {
//        TextView view = getView(viewId);
//        view.setTextColor(context.getResources().getColor(textColorRes));
//        return this;
//    }
//
//    /**
//     * Will download an image from a URL and put it in an ImageView.<br/>
//     * It uses Square's Picasso library to download the image asynchronously and put the result into the ImageView.<br/>
//     * Picasso manages recycling of views in a ListView.<br/>
//     * If you need more control over the Picasso settings, use {SuperViewHolder#setImageBuilder}.
//     *
//     * @param viewId   The view id.
//     * @param imageUrl The image URL.
//     * @return The SuperViewHolder for chaining.
//     */
//    public SuperViewHolder setImageUrl(int viewId, String imageUrl) {
//        ImageView view = getView(viewId);
//        Picasso.with(context).load(imageUrl).into(view);
//        return this;
//    }
//
//    /**
//     * Will download an image from a URL and put it in an ImageView.<br/>
//     *
//     * @param viewId         The view id.
//     * @param requestBuilder The Picasso request builder. (e.g. Picasso.with(context).load(imageUrl))
//     * @return The SuperViewHolder for chaining.
//     */
//    public SuperViewHolder setImageBuilder(int viewId, RequestCreator requestBuilder) {
//        ImageView view = getView(viewId);
//        requestBuilder.into(view);
//        return this;
//    }
//*************************************************************//

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    public SuperViewHolder setAlpha(@IdRes int viewId, @FloatRange(from = 0f, to = 1f) float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for GONE.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * Add links into a TextView.
     *
     * @param viewId The id of the TextView to linkify.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder linkify(@IdRes int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    public SuperViewHolder setTypeface(@IdRes int viewId, Typeface typeface) {
        TextView view = getView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    public SuperViewHolder setTypeface(Typeface typeface, @IdRes int... viewIds) {
        for (@IdRes int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    /**
     * Sets the progress of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setProgress(@IdRes int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     *
     * @param viewId The view id.
     * @param max    The max value of a ProgressBar.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setMax(@IdRes int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    /**
     * Sets the progress and max of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @param max      The max value of a ProgressBar.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setProgress(@IdRes int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setRating(@IdRes int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @param max    The range of the RatingBar to 0...max.
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the on click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on click listener;
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setOnClickListener(@IdRes int viewId,
                                             @Nullable View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * Sets the on touch listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on touch listener;
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setOnTouchListener(@IdRes int viewId,
                                             @Nullable View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * Sets the on long click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on long click listener;
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setOnLongClickListener(@IdRes int viewId,
                                                 @Nullable View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    /**
     * Sets the listview or gridview's item click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item on click listener;
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setOnItemClickListener(@IdRes int viewId,
                                                 @Nullable AdapterView.OnItemClickListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemClickListener(listener);
        return this;
    }

    /**
     * Sets the listview or gridview's item long click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item long click listener;
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setOnItemLongClickListener(@IdRes int viewId,
                                                     @Nullable AdapterView.OnItemLongClickListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemLongClickListener(listener);
        return this;
    }

    /**
     * Sets the listview or gridview's item selected click listener of the view
     *
     * @param viewId   The view id.
     * @param listener The item selected click listener;
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setOnItemSelectedClickListener(@IdRes int viewId,
                                                         @Nullable AdapterView.OnItemSelectedListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemSelectedListener(listener);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param tag    The tag;
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setTag(@IdRes int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param key    The key of tag;
     * @param tag    The tag;
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setTag(@IdRes int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * Sets the checked status of a checkable.
     *
     * @param viewId  The view id.
     * @param checked The checked status;
     * @return The SuperViewHolder for chaining.
     */
    public SuperViewHolder setChecked(@IdRes int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * Retrieves the last converted object on this view.
     */
    public Object getAssociatedObject() {
        return mAssociatedObject;
    }

    /**
     * Should be called during convert
     */
    public void setAssociatedObject(Object associatedObject) {
        mAssociatedObject = associatedObject;
    }
}
