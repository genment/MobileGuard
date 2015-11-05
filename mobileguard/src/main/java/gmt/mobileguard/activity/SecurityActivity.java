package gmt.mobileguard.activity;

import android.app.Fragment;
import android.os.Bundle;
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
        SecurityGuide4.OnStepButtonClickedListener {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_security);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getIntent().getBooleanExtra("configed", false)) {
            // 进入[手机]界面
            getFragmentManager().beginTransaction().add(R.id.contentPanel, new SecurityFragment(), "SecurityFragment").commit();
        } else {
            // 进入[向导]界面
            toolbar.setTitle(R.string.title_guide1);
            getFragmentManager().beginTransaction().add(R.id.contentPanel, new SecurityGuide1(), "SecurityGuide1").commit();
        }
        setSupportActionBar(toolbar); // 一定要在这里，要不然第一次进入向导时，标题不对
    }

    @Override
    public void onStepButtonClicked(int guide_id, boolean next) {
        int titleId = 0;
        Fragment fragment = null;
        switch (guide_id) {
            case 1:
                titleId = R.string.title_guide2;
                fragment = new SecurityGuide2();
                break;
            case 2:
                if (next) {
                    titleId = R.string.title_guide3;
                    fragment = new SecurityGuide3();
                } else {
                    titleId = R.string.title_guide1;
                    fragment = new SecurityGuide1();
                }
                break;
            case 3:
                if (next) {
                    titleId = R.string.title_guide4;
                    fragment = new SecurityGuide4();
                } else {
                    titleId = R.string.title_guide2;
                    fragment = new SecurityGuide2();
                }
                break;
            case 4:
                if (next) {
                    titleId = R.string.title_security;
                    fragment = new SecurityFragment();
                } else {
                    titleId = R.string.title_guide3;
                    fragment = new SecurityGuide3();
                }
                break;
        }
        if (fragment != null) {
            toolbar.setTitle(titleId);
            getFragmentManager().beginTransaction().replace(R.id.contentPanel, fragment).commit();
        }
    }
}
