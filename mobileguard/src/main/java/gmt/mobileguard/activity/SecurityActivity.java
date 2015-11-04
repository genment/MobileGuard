package gmt.mobileguard.activity;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import gmt.mobileguard.R;
import gmt.mobileguard.fragment.SecurityGuide1;

public class SecurityActivity extends AppCompatActivity implements SecurityGuide1.OnStepButtonClickedListener {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_security);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getIntent().getBooleanExtra("configed", false)) {
            // TODO: 2015/11/4 进入[手机]界面
            toolbar.setTitle(R.string.title_guide1);
            //getFragmentManager().beginTransaction().add(R.id.contentPanel, SecurityFragment.newInstance(null, null), "SecurityFragment").commit();
            getFragmentManager().beginTransaction().add(R.id.contentPanel, new SecurityGuide1(), "SecurityGuide1").commit();
        } else {
            // TODO: 2015/11/4 进入[向导]界面
            getFragmentManager().beginTransaction().add(R.id.contentPanel, new SecurityGuide1(), "SecurityGuide1").commit();
        }
        setSupportActionBar(toolbar);
    }

    @Override
    public void onStepButtonClicked(int guide_id, boolean next) {
        Toast.makeText(this, "Guide ID: " + guide_id + ", " + (next ? "Next" : "Prev"), Toast.LENGTH_SHORT).show();
        Fragment fragment = null;
        switch (guide_id) {
            case 1:
//                fragment = new SecurityGuide2();
                break;
            case 2:
//                fragment = next ? new SecurityGuide3() : new SecurityGuide1();
                break;
            case 3:
//                fragment = next ? new SecurityGuide2() : new SecurityGuide4();
                break;
            case 4:
//                fragment = next ? new SecurityGuide3() : new SecurityFragment();
                break;
        }
        if (fragment != null) {
            getFragmentManager().beginTransaction().replace(R.id.contentPanel, fragment).commit();
        }
    }
}
