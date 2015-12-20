package gmt.mobileguard.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.widget
 * Created by Genment at 2015/12/16 16:06.
 * <p/>
 * From: http://willclub.me/android/2015/04/24/recyclerview3/
 */
public class CommonRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable dividerDrawable;

    /**
     * @param context Current context, will be used to access resources.
     */
    public CommonRecyclerViewItemDecoration(Context context) {
        /*dividerDrawable = context.getResources()
                .getDrawable(android.R.drawable.divider_horizontal_bright);*/
        final TypedArray a = context.obtainStyledAttributes(new int[]{
                android.R.attr.listDivider
        });
        dividerDrawable = a.getDrawable(0);
        a.recycle();
    }

    /**
     * item 偏移位置
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (dividerDrawable == null) {
            return;
        }

        //如果是第一个item，不需要divider，所以直接return
        if (parent.getChildLayoutPosition(view) < 1) {
            return;
        }

        //相当于给itemView设置margin，给divider预留空间
        outRect.top = dividerDrawable.getIntrinsicHeight();

    }

    /**
     * item 底层
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    /**
     * item 顶层
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        if (layoutManager == null) {
            return;
        }

        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        int childCount = parent.getChildCount();

        int left = 0;
        int right = parent.getWidth();
        for (int i = 0; i < childCount; i++) {
            //判断第一个item的下标是不是0，是则return，不需要draw divider
            if (i == 0 && firstVisiblePosition == 0) {
                continue;
            }
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
            int bottom = childView.getTop() - params.topMargin;
            int top = bottom - dividerDrawable.getIntrinsicHeight();
            dividerDrawable.setBounds(left, top, right, bottom);
            dividerDrawable.draw(c);

        }
    }
}
