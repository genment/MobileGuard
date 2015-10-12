package gmt.mobileguard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import gmt.mobileguard.BuildConfig;
import gmt.mobileguard.R;

public class SplashActivity extends Activity {

    private RequestQueue requestQueue;
    private Bundle updateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //显示版本号
        ((TextView) findViewById(R.id.version_name)).setText(getString(R.string.version_name, BuildConfig.VERSION_NAME));

        updateInfo = new Bundle();
        updateInfo.putBoolean("hasNewVersion", false); // 默认为false
        requestQueue = Volley.newRequestQueue(this);
        checkNewVersion();

        waitForShowSplash();
    }

    private void checkNewVersion() {
        requestQueue.add(new JsonObjectRequest("http://192.168.1.2/update.php", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int versionCode = jsonObject.getInt("versionCode");
                    if (versionCode > BuildConfig.VERSION_CODE) {
                        updateInfo.putBoolean("hasNewVersion", true);
                        updateInfo.putString("versionName", jsonObject.getString("versionName"));
                        updateInfo.putString("desc", jsonObject.getString("desc"));
                        updateInfo.putString("apk", jsonObject.getString("apk"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "网络异常, 请检查网络是否开启", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void waitForShowSplash() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                goHomeActivity();
            }
        }, 1500);
    }

    private void goHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtras(updateInfo);
        startActivity(intent);
        finish();
    }
}