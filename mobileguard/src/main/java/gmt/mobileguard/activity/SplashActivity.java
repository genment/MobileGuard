package gmt.mobileguard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import gmt.mobileguard.BuildConfig;
import gmt.mobileguard.R;

public class SplashActivity extends Activity {

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //显示版本号
        ((TextView) findViewById(R.id.version_name)).setText(getString(R.string.version_name, BuildConfig.VERSION_NAME));

        requestQueue = Volley.newRequestQueue(this);
        checkNewVersion();
    }

    private void checkNewVersion() {
        requestQueue.add(new JsonObjectRequest("http://192.168.1.4/update.php", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int versionCode = jsonObject.getInt("versionCode");
                    if (versionCode > BuildConfig.VERSION_CODE) {
                        new AlertDialog.Builder(SplashActivity.this)
                                .setTitle("发现新版本: " + jsonObject.getString("versionName"))
                                .setMessage(jsonObject.getString("desc"))
                                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getApplicationContext(), "正在下载新版本", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getApplicationContext(), "取消更新", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setCancelable(false) // 不能关闭, 只能选择
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }));
    }

}
