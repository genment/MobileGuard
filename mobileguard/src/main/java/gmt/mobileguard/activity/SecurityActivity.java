package gmt.mobileguard.activity;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import gmt.mobileguard.R;
import gmt.mobileguard.fragment.SecurityFragment;
import gmt.mobileguard.fragment.SecurityGuide1;
import gmt.mobileguard.fragment.SecurityGuide2;
import gmt.mobileguard.fragment.SecurityGuide3;
import gmt.mobileguard.fragment.SecurityGuide4;

public class SecurityActivity extends AppCompatActivity implements
        SecurityGuide1.OnStepButtonClickedListener,
        SecurityGuide2.OnStepButtonClickedListener,
        SecurityGuide3.OnStepButtonClickedListener,
        SecurityGuide4.OnStepButtonClickedListener,
        SecurityFragment.OnStepButtonClickedListener {

    private Toolbar mToolbar;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_security);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getIntent().getBooleanExtra("configed", false)) {
            // 进入[手机防盗]界面
            onStepButtonClicked(5);
        } else {
            // 进入[向导]界面
            onStepButtonClicked(1);
        }
        setSupportActionBar(mToolbar); // 一定要在这里，要不然第一次进入向导时，标题不对
    }

    @Override
    public void onStepButtonClicked(int goToGuide) {
        int titleId = R.string.title_security;
        switch (goToGuide) {
            case 1:
                titleId = R.string.title_guide1;
                mFragment = new SecurityGuide1();
                break;
            case 2:
                titleId = R.string.title_guide2;
                mFragment = new SecurityGuide2();
                break;
            case 3:
                titleId = R.string.title_guide3;
                mFragment = new SecurityGuide3();
                break;
            case 4:
                titleId = R.string.title_guide4;
                mFragment = new SecurityGuide4();
                break;
            case 5:
                mFragment = new SecurityFragment();
        }
        if (mFragment != null) {
            mToolbar.setTitle(titleId);
            getFragmentManager().beginTransaction().replace(R.id.contentPanel, mFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (isOnGuide()) {
            showExitDialog(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (isOnGuide()) {
            showExitDialog(true);
            return false;
        }
        return super.onSupportNavigateUp();
    }

    /**
     * 如果当前是在“向导”中，则返回 <code>true</code>，否则返回 <code>false</code>
     *
     * @return 是否在“向导”中。
     */
    private boolean isOnGuide() {
        return mFragment != null && !(mFragment instanceof SecurityFragment);
    }

    /**
     * 退出“向导”时，提示用户。
     *
     * @param toHome 是否“向上”导航到 HomeActivity，用于区分“向上”和“返回”两种操作。
     */
    private void showExitDialog(final boolean toHome) {
        new AlertDialog.Builder(this)
                .setTitle("确定要退出？")
                .setMessage("退出设置后，设置项将不会保存。")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (toHome) {            // 返回 HomeActivity
                            SecurityActivity.super.onSupportNavigateUp(); // TODO: 2015/11/15 暂时没想到更好的办法
                        } else {                 // 直接进入[手机防盗]界面
                            onStepButtonClicked(5);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

}
