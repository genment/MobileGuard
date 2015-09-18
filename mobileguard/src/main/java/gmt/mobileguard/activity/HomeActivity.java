package gmt.mobileguard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import gmt.mobileguard.R;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getNewVersion();
    }

    private void getNewVersion() {
        Bundle updateInfo = getIntent().getExtras();
        if (updateInfo.getBoolean("hasNewVersion")) { // 如果有新版本
            new AlertDialog.Builder(this)
                    .setTitle("发现新版本: " + updateInfo.getString("versionName"))
                    .setMessage(updateInfo.getString("desc"))
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
    }


}
