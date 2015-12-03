package gmt.mobileguard.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import gmt.mobileguard.R;
import gmt.mobileguard.storage.db.dao.BlacklistDao;
import gmt.mobileguard.storage.db.entity.BlackEntity;

public class BlacklistActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mBlacklist;
    private BlacklistDao mBlacklistDao;
    private RecyclerViewAdapter mAdapter;

    /**
     * 标识进入 EditBlackActivity 的动作，如果是 true，表示添加，
     * 如果是 false，表示编辑。
     *
     * @see #onRestart()
     * @see #onClick(View)
     * @see #onContextItemSelected(MenuItem)
     */
    private boolean mIsAddAction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacklist);

        mBlacklistDao = new BlacklistDao(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_blacklist);
        fab.setOnClickListener(this);

        mAdapter = new RecyclerViewAdapter(mBlacklistDao.getAll());
        mBlacklist = (RecyclerView) findViewById(R.id.blacklist);
        mBlacklist.setHasFixedSize(true);
        mBlacklist.setAdapter(mAdapter);
        mBlacklist.setLayoutManager(new LinearLayoutManager(this));
        mBlacklist.addItemDecoration(new RecyclerViewItemDecoration());
    }

    /**
     * Fab click
     *
     * @param v fab
     */
    @Override
    public void onClick(View v) {
        mIsAddAction = true;
        startActivity(new Intent(BlacklistActivity.this, EditBlackActivity.class));
    }

    /**
     * 添加黑名单后，返回到当前列表时，刷新列表数据
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        if (mIsAddAction) {
            mAdapter.updateLastOneData();
        } else {
            mAdapter.changeData(mAdapter.onLongClickPosition);
        }
    }

    /**
     * Adapter
     */
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private List<BlackEntity> mDatas;
        private int onLongClickPosition;

        public RecyclerViewAdapter(List<BlackEntity> datas) {
            this.mDatas = datas;
        }

        // 调用一屏
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
            View item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_blacklist, parent, false);
            return new ViewHolder(item);
        }

        // 滚动时
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            BlackEntity black = mDatas.get(position);
            holder.blackNumber.setText(black.getNumber());
            holder.blackDescription.setText(black.getDescription());
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

        public void updateLastOneData() {
            BlackEntity blackEntity = mBlacklistDao.getLastOne();
            if (blackEntity != null
                    && (mDatas.size() < 1 || blackEntity.getId() != mDatas.get(0).getId())) {
                mDatas.add(0, blackEntity);
                notifyItemInserted(0);
                mBlacklist.scrollToPosition(0);
            }
        }

        public void removeData(int position) {
            mBlacklistDao.removeBlack(mDatas.get(position));
            mDatas.remove(position);
            notifyItemRemoved(position);
        }

        public void clearDatas() {
            notifyItemRangeRemoved(0, mDatas.size());
            mDatas.clear();
            mBlacklistDao.clearAllBlack();
        }

        public void changeData(int position) {
            BlackEntity blackEntity = mBlacklistDao.getOne(mDatas.get(position).getId());
            mDatas.set(position, blackEntity);
            notifyItemChanged(position);
        }

        public void updateAllDatas() {
            mDatas = mBlacklistDao.getAll();
            notifyItemRangeChanged(0, mDatas.size());
        }

        public void changeMode(int position) {
            mBlacklistDao.changeMode(mDatas.get(position));
        }

        /**
         * ViewHolder
         */
        class ViewHolder extends RecyclerView.ViewHolder implements
                View.OnClickListener,
                View.OnLongClickListener {

            private TextView blackNumber;
            private TextView blackCount;
            private TextView blackDescription;
            private TextView blackAttribution;
            private CheckBox blackPhone;
            private CheckBox blackMessage;

            public ViewHolder(View itemView) {
                super(itemView);
                blackNumber = (TextView) itemView.findViewById(R.id.black_number);
                blackCount = (TextView) itemView.findViewById(R.id.black_count);
                blackDescription = (TextView) itemView.findViewById(R.id.black_description);
                blackAttribution = (TextView) itemView.findViewById(R.id.black_attribution);
                blackPhone = (CheckBox) itemView.findViewById(R.id.black_phone);
                blackMessage = (CheckBox) itemView.findViewById(R.id.block_message);

                itemView.setOnLongClickListener(this);

                blackPhone.setOnClickListener(this);
                blackMessage.setOnClickListener(this);
            }

            /**
             * 修改mode时，不用 #OnCheckedChangeListener，因为滚动屏幕时，是通过setChecked()进行设置的，
             * 而setChecked()内部会回调onCheckedChanged()，导致滚动列表时，不停地调用 changeMode(int) 操作数据库
             */
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (view instanceof CheckBox) {
                    BlackEntity blackEntity = mDatas.get(position);
                    boolean isChecked = ((CheckBox) view).isChecked();
                    switch (view.getId()) {
                        case R.id.black_phone:
                            blackEntity.setBlackPhone(isChecked);
                            break;
                        case R.id.block_message:
                            blackEntity.setBlackMessage(isChecked);
                            break;
                    }
                    changeMode(position);
                }
            }

            @Override
            public boolean onLongClick(View v) {
                onLongClickPosition = getAdapterPosition();
                registerForContextMenu(v);
                return false;
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
            dividerDrawable = getResources()
                    .getDrawable(android.R.drawable.divider_horizontal_bright);
        }

        // item 偏移位置
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, R.string.clear);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            if (mAdapter.getItemCount() > 0) {
                new AlertDialog.Builder(this)
                        .setMessage("确定要清空黑名单列表吗？清空后就不能拦截了。")
                        .setPositiveButton(R.string.delete_all, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.clearDatas();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            } else {
                Snackbar.make(mBlacklist, "还没有黑名单呢。", Snackbar.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(mAdapter.mDatas.get(mAdapter.onLongClickPosition).getNumber());
        menu.add(Menu.NONE, 1, Menu.NONE, R.string.edit);
        menu.add(Menu.NONE, 2, Menu.NONE, R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                mIsAddAction = false;
                startActivity(new Intent(this, EditBlackActivity.class)
                        .putExtra(EditBlackActivity.EXTRA_KEY,
                                mAdapter.mDatas.get(mAdapter.onLongClickPosition)));
                break;
            case 2:
                mAdapter.removeData(mAdapter.onLongClickPosition);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        menu.clear();
        super.onContextMenuClosed(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBlacklistDao.close();
    }
}
