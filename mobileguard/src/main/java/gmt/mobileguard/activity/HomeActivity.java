package gmt.mobileguard.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import gmt.mobileguard.R;
import gmt.mobileguard.sevice.UpdateService;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GridView gridView;
    private TextView notice;

    private Class[] item_class = {
            // TODO: 2015/9/21 定义各个功能的 Activity.class
            null, null, null,
            null, null, null,
            null, null, null
    };

    private String[] item_text = {
            "手机防盗", "通信卫士", "软件管理",
            "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"
    };

    private int[] item_icon = {
            R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_app,
            R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan,
            R.drawable.home_sysoptimize, R.drawable.home_advtools, R.drawable.home_settings
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("功能列表");
        setSupportActionBar(toolbar);

        notice = (TextView) findViewById(R.id.home_notice);
        gridView = ((GridView) findViewById(R.id.home_gridview));
        gridView.setAdapter(new GridViewAdapter());
        gridView.setOnItemClickListener(this);

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
                    .setNegativeButton("以后再说", null) // 只需显示button, 无需监听事件
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: 2015/9/21 点击item后，跳转到各个功能界面
        if (item_class[position] != null) {
            Intent intent = new Intent(this, item_class[position]);
            startActivity(intent);
        }
    }

    class GridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return item_text.length;
        }

        @Override
        public Object getItem(int position) {
            return item_text[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO: 2015/9/21 存在 position=0 会重复调用多次的问题，所幸item可以一屏显示，暂且通过 if (convertView == null)的方法来缓解
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_home, null);
                TextView item = (TextView) convertView.findViewById(R.id.home_gridview_item);
                item.setText(item_text[position]);
                item.setCompoundDrawablesWithIntrinsicBounds(0, item_icon[position], 0, 0);
            }
            /*if (convertView == null) {
                convertView = new TextView(HomeActivity.this);
                TextView item = (TextView) convertView;
                item.setText(item_text[position]);
                item.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(item_icon[position]), null, null);
            }*/
            return convertView;
        }
    }
}
