package gmt.mobileguard.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import gmt.mobileguard.widget.CommonRecyclerViewItemDecoration;

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
        mBlacklist.addItemDecoration(new CommonRecyclerViewItemDecoration(this));
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
        private View mEmptyView;

        public RecyclerViewAdapter(List<BlackEntity> datas) {
            this.mDatas = datas;
            checkDataEmpty();
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
                checkDataEmpty();
            }
        }

        public void removeData(int position) {
            mBlacklistDao.removeBlack(mDatas.get(position));
            mDatas.remove(position);
            notifyItemRemoved(position);
            checkDataEmpty();
        }

        public void clearDatas() {
            notifyItemRangeRemoved(0, mDatas.size());
            mDatas.clear();
            mBlacklistDao.clearAllBlack();
            checkDataEmpty();
        }

        public void changeData(int position) {
            BlackEntity blackEntity = mBlacklistDao.getOne(mDatas.get(position).getId());
            mDatas.set(position, blackEntity);
            notifyItemChanged(position);
        }

        public void updateAllDatas() {
            mDatas = mBlacklistDao.getAll();
            notifyItemRangeChanged(0, mDatas.size());
            checkDataEmpty();
        }

        public void changeMode(int position) {
            mBlacklistDao.changeMode(mDatas.get(position));
        }

        private void checkDataEmpty() {
            if (mEmptyView == null) {
                mEmptyView = findViewById(R.id.emptyView);
            }
            if (mDatas.size() < 1) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
            }
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
                Snackbar.make(mBlacklist, R.string.tip_black_list_empty, Snackbar.LENGTH_SHORT).show();
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
