package gmt.mobileguard;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //显示版本号
        ((TextView) findViewById(R.id.version_name)).setText(getString(R.string.version_name, BuildConfig.VERSION_NAME));
    }

}
