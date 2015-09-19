package gmt.mobileguard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import gmt.mobileguard.R;
import gmt.mobileguard.sevice.UpdateService;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getNewVersion();
    }

    private void getNewVersion() {
        final Bundle updateInfo = getIntent().getExtras();
        if (updateInfo.getBoolean("hasNewVersion")) { // 如果有新版本
            new AlertDialog.Builder(this)
                    .setTitle("发现新版本: " + updateInfo.getString("versionName"))
                    .setMessage(updateInfo.getString("desc"))
                    .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (checkExternalStorageFreeSize()) { // 检测是否有足够的可用空间
                                startDownloadService(updateInfo.getString("apk"));
                                Toast.makeText(getApplicationContext(), "正在下载新版本", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "SD卡可用空间少于10MB, 请清理后再重试", Toast.LENGTH_SHORT).show();
                            }
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

    private void startDownloadService(String url) {
        Intent intent = new Intent(this, UpdateService.class);
        intent.putExtra("apk", url);
        startService(intent);
    }

    private boolean checkExternalStorageFreeSize() {
        return Environment.getExternalStorageDirectory().getUsableSpace() / 1024 / 1024 >= 10;
    }
}
