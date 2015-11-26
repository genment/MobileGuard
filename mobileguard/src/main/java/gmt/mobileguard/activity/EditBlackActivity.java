package gmt.mobileguard.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import gmt.mobileguard.R;
import gmt.mobileguard.storage.db.dao.BlacklistDao;
import gmt.mobileguard.storage.db.entity.BlackEntity;

public class EditBlackActivity extends AppCompatActivity {

    private static final String EXTRA_KEY = "blacklist";
    private static final int REQUEST_CODE_RECORD = 0;
    private static final int REQUEST_CODE_MESSAGE = 1;
    private static final int REQUEST_CODE_CONTACT = 2;
    private static final int REQUEST_CODE_INPUT = 3;

    private BlackEntity mBlackEntity = null;
    private BlacklistDao mBlacklistDao = null;

    private Toolbar mToolbar;

    private RadioGroup mMode;
    private EditText mNumber;
    private EditText mDescription;
    private TextView mAttribution;

    private AlertDialog mDescriptionsSelectionDialog;

    /**
     * ture 为添加，false 为编辑。
     */
    private boolean mIsAddMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_black);

        mBlacklistDao = new BlacklistDao(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mNumber = (EditText) findViewById(R.id.black_number);
        mAttribution = (TextView) findViewById(R.id.black_attribution);
        mMode = (RadioGroup) findViewById(R.id.black_mode);
        mDescription = (EditText) findViewById(R.id.black_description);

        initListeners();

        checkMode();
    }

    private void initListeners() {
        mDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && TextUtils.isEmpty(mDescription.getText()))
                    showDescriptionsSelectionDialog();
            }
        });
        mDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mDescription.getText()))
                    showDescriptionsSelectionDialog();
            }
        });
        // TODO: 2015/11/25 查询号码的归属地
        // mNumber.addTextChangedListener(null);
    }

    private void checkMode() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && (mBlackEntity = (BlackEntity) extras.get(EXTRA_KEY)) != null) {
            mIsAddMode = false;
            mToolbar.setTitle(R.string.title_edit_black);

            mNumber.setText(mBlackEntity.getNumber());
            mAttribution.setText(mBlackEntity.getAttribution());
            mMode.check(setBlackMode(mBlackEntity.getMode()));
            mDescription.setText(mBlackEntity.getDescription());
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("添加方式")
                    .setItems(R.array.add_black_from, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which != REQUEST_CODE_INPUT) { // which == 3 是手动
                                startSelectionActivity(which);
                            }
                        }
                    })
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.black_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_black_record:
                startSelectionActivity(REQUEST_CODE_RECORD);
                break;
            case R.id.add_black_message:
                startSelectionActivity(REQUEST_CODE_MESSAGE);
                break;
            case R.id.add_black_contact:
                startSelectionActivity(REQUEST_CODE_CONTACT);
                break;
            case R.id.black_save:
                saveBlackItem(); // no break; should be finish().
            default:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBlacklistDao.close();
    }

    /**
     * 保存
     */
    private void saveBlackItem() {
        String number = mNumber.getText().toString();

        if (TextUtils.isEmpty(number)) {
            return;
        }
        if (mBlackEntity == null) {
            mBlackEntity = new BlackEntity();
        }

        mBlackEntity.setNumber(number);
        mBlackEntity.setMode(getBlackMode());
        mBlackEntity.setAttribution(mAttribution.getText().toString());
        mBlackEntity.setDescription(mDescription.getText().toString());

        if (mIsAddMode) {
            mBlacklistDao.addBlack(mBlackEntity);
        } else {
            mBlacklistDao.updateBlack(mBlackEntity);
        }
    }

    /**
     * 将 UI 转换为 {@link BlackEntity#getMode()}
     *
     * @return {@link BlackEntity#mode}
     */
    private int getBlackMode() {
        switch (mMode.getCheckedRadioButtonId()) {
            case R.id.black_phone:
                return 1;
            case R.id.block_message:
                return 2;
            default:
                return 3;
        }
    }

    /**
     * 将 {@link BlackEntity#getMode()} 转换为 UI
     *
     * @param mode {@link BlackEntity#mode}
     * @return UI 控件的 id
     */
    private int setBlackMode(int mode) {
        switch (mode) {
            case 1:
                return R.id.black_phone;
            case 2:
                return R.id.block_message;
            default:
                return android.R.id.selectAll;
        }
    }

    private void showDescriptionsSelectionDialog() {
        if (mDescriptionsSelectionDialog == null) {
            mDescriptionsSelectionDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.description)
                    .setItems(R.array.black_descriptions, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which != 5) {
                                mDescription.setText(((AlertDialog) dialog)
                                        .getListView().getAdapter().getItem(which).toString());
                            }
                        }
                    }).create();
        }
        mDescriptionsSelectionDialog.show();
    }

    /**
     * 开启 NumberSelectionActivity 获取电话号码
     *
     * @param which 获取方式。
     */
    private void startSelectionActivity(int which) {
        // TODO: 2015/11/26 开启对应的 Activity
        switch (which) {
            case REQUEST_CODE_RECORD:
                break;
            case REQUEST_CODE_MESSAGE:
                break;
            case REQUEST_CODE_CONTACT:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: 2015/11/26 处理返回数据
    }
}
