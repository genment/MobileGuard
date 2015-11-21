package gmt.mobileguard.activity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gmt.mobileguard.R;
import gmt.mobileguard.util.entity.BlackEntity;

public class BlacklistActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private RecyclerView mBlacklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab_add_blacklist);
        mFab.setOnClickListener(this);

        mBlacklist = (RecyclerView) findViewById(R.id.blacklist);
        mBlacklist.setHasFixedSize(true);
        mBlacklist.setAdapter(new RecyclerViewAdapter());
        mBlacklist.setLayoutManager(new LinearLayoutManager(this));
        mBlacklist.addItemDecoration(new RecyclerViewItemDecoration());
        mBlacklist.setOnScrollListener(new RecyclerView.OnScrollListener() {
            // Fab的显示状态，防止重复调用
            boolean show = true;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // dx < 0: 向左, dx > 0: 向右
                // dy < 0: 向下, dy > 0: 向上
                if (!show && dy < 0) {
                    mFab.show();
                    show = true;
                } else if (show && dy > 0) {
                    mFab.hide();
                    show = false;
                }
            }
        });
    }

    private List<BlackEntity> getData() {
        Random random = new Random();
        List<BlackEntity> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BlackEntity blackEntity = new BlackEntity();
            blackEntity.setNumber("138" + random.nextInt());
            blackEntity.setCount(random.nextInt() % 20 + 1);
            blackEntity.setName("黄飞鸿");
            blackEntity.setAttribution("广东佛山");
            blackEntity.setMode(i % 4);
            datas.add(blackEntity);
        }
        return datas;
    }

    /**
     * Fab click
     *
     * @param v fab
     */
    @Override
    public void onClick(View v) {
        Random random = new Random();
        BlackEntity blackEntity = new BlackEntity();
        blackEntity.setNumber("138" + random.nextInt());
        blackEntity.setCount(random.nextInt() % 10 + 1);
        blackEntity.setName("黄飞鸿");
        blackEntity.setAttribution("广东佛山");
        blackEntity.setMode(random.nextInt() % 3);
        ((RecyclerViewAdapter) mBlacklist.getAdapter()).addData(blackEntity);
    }

    /**
     * Adapter
     */
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private List<BlackEntity> mDatas = getData();

        // 调用一屏
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blacklist, parent, false);
            return new ViewHolder(item);
        }

        // 滚动时
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            BlackEntity black = mDatas.get(position);
            holder.blackNumber.setText(black.getNumber());
            holder.blackName.setText(black.getName());
            holder.blackAttribution.setText(black.getAttribution());
            holder.blackPhone.setChecked(black.getBlackPhone());
            holder.blackMessage.setChecked(black.getBlackMessage());
            int count = black.getCount();
            if (count > 0) {
                holder.blackCount.setText(getString(R.string.black_count, count));
                holder.blackCount.setVisibility(View.VISIBLE);
            } else {
                holder.blackCount.setVisibility(View.GONE);
            }
        }

        // 滚动时、notify时
        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        public void addData(BlackEntity data) {
            int position = mDatas.size();
            mDatas.add(data);
            notifyItemInserted(position);
        }

        public void removeData(int position) {
            mDatas.remove(position);
            notifyItemRemoved(position);
        }

        /**
         * ViewHolder
         */
        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView blackNumber;
            private TextView blackCount;
            private TextView blackName;
            private TextView blackAttribution;
            private CheckBox blackPhone;
            private CheckBox blackMessage;

            public ViewHolder(View itemView) {
                super(itemView);
                blackNumber = (TextView) itemView.findViewById(R.id.black_number);
                blackCount = (TextView) itemView.findViewById(R.id.black_count);
                blackName = (TextView) itemView.findViewById(R.id.black_name);
                blackAttribution = (TextView) itemView.findViewById(R.id.black_attribution);
                blackPhone = (CheckBox) itemView.findViewById(R.id.black_phone);
                blackMessage = (CheckBox) itemView.findViewById(R.id.block_message);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeData(getLayoutPosition());
                        Snackbar.make(v, "你删除了一个", Snackbar.LENGTH_SHORT).setAction("再来一个", BlacklistActivity.this).show();
                    }
                });
            }
        }
    }


    /**
     * ItemDecoration
     * <p/>
     * From: http://willclub.me/android/2015/04/24/recyclerview3/
     */
    class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable dividerDrawable;

        public RecyclerViewItemDecoration() {
            dividerDrawable = getResources().getDrawable(android.R.drawable.divider_horizontal_bright);
        }

        // item 偏移位置
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
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

        // item 底部
        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
        }

        // item 顶部
        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            if (layoutManager == null) {
                return;
            }

            int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
            int childCount = parent.getChildCount();

            int right = parent.getWidth();
            for (int i = 0; i < childCount; i++) {
                //判断第一个item的下标是不是0，是则return，不需要draw divider
                if (i == 0 && firstVisiblePosition == 0) {
                    continue;
                }
                View childView = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
                int left = parent.getPaddingLeft() + childView.getPaddingLeft();
                int bottom = childView.getTop() - params.topMargin;
                int top = bottom - dividerDrawable.getIntrinsicHeight();
                dividerDrawable.setBounds(left, top, right, bottom);
                dividerDrawable.draw(c);

            }
        }
    }
}
