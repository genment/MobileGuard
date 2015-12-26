package gmt.mobileguard.activity;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import gmt.mobileguard.R;
import gmt.mobileguard.fragment.BaseNumberPickerFragment;
import gmt.mobileguard.fragment.CallLogNumberPickerFragment;
import gmt.mobileguard.fragment.ContactNumberPickerFragment;
import gmt.mobileguard.fragment.SmsNumberPickerFragment;
import gmt.mobileguard.util.PickerNumberUtil;

public class NumberSelectionActivity extends AppCompatActivity implements
        BaseNumberPickerFragment.OnListFragmentItemClickListener {

    private static final int NO_ACTION_CODE = -1;
    private static final int ACTION_CODE_CALL_LOG = 0;
    private static final int ACTION_CODE_SMS = 1;
    private static final int ACTION_CODE_CONTACT = 2;

    private int mActionCode = NO_ACTION_CODE;

    private BaseNumberPickerFragment mListFragment;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_selection);

        mActionCode = ActionIntents.resolveIntent(getIntent());
        if (mActionCode != NO_ACTION_CODE)
            configureListFragment();

        prepareSearchViewAndActionBar();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof BaseNumberPickerFragment) {
            mListFragment = (BaseNumberPickerFragment) fragment;
        }
    }

    @Override
    public void onListFragmentItemClick(Cursor c) {
        Intent intent = new Intent();
        intent.putExtra("number", PickerNumberUtil.getPureNumber(getNumber(c)));
        intent.putExtra("name", getName(c));
        setResult(RESULT_OK, intent);
        finish(); // 选择后，finish。
    }

    private String getNumber(Cursor cursor) {
        int columnIndex = -1;
        switch (mActionCode) {
            case ACTION_CODE_CALL_LOG:
                columnIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                break;
            case ACTION_CODE_SMS:
                columnIndex = cursor.getColumnIndex(Telephony.Sms.Inbox.ADDRESS);
                break;
            case ACTION_CODE_CONTACT:
                columnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                break;
        }
        return cursor.getString(columnIndex);
    }

    private String getName(Cursor cursor) {
        int columnIndex = -1;
        switch (mActionCode) {
            case ACTION_CODE_CALL_LOG:
                columnIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
                break;
            case ACTION_CODE_SMS:
                // nothing.
                break;
            case ACTION_CODE_CONTACT:
                columnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                break;
        }
        return cursor.getString(columnIndex);
    }

    /**
     * Creates the fragment based on the current request.
     */

    public void configureListFragment() {
        switch (mActionCode) {
            case ACTION_CODE_CALL_LOG: {
                mListFragment = new CallLogNumberPickerFragment();
                break;
            }
            case ACTION_CODE_SMS: {
                mListFragment = new SmsNumberPickerFragment();
                break;
            }
            case ACTION_CODE_CONTACT: {
                mListFragment = new ContactNumberPickerFragment();
                break;
            }
            default:
                throw new IllegalStateException("Invalid action code: " + mActionCode);
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.list_container, mListFragment)
                .commitAllowingStateLoss();
    }

    private void prepareSearchViewAndActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Go back to previous screen, intending "cancel"
                setResult(RESULT_CANCELED);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ActionIntents {
        public static final String ACITON_PICK_CALL_LOG = "action.PICK_FROM_CALL_LOG";
        public static final String ACITON_PICK_SMS = "action.PICK_FROM_SMS";
        public static final String ACITON_PICK_CONTACT = "action.PICK_FROM_CONTACT";

        public static int resolveIntent(Intent intent) {
            String action = intent.getAction();
            if (ACITON_PICK_CALL_LOG.equals(action)) {
                return ACTION_CODE_CALL_LOG;
            } else if (ACITON_PICK_SMS.equals(action)) {
                return ACTION_CODE_SMS;
            } else if (ACITON_PICK_CONTACT.equals(action)) {
                return ACTION_CODE_CONTACT;
            }
            return NO_ACTION_CODE;
        }
    }
}
