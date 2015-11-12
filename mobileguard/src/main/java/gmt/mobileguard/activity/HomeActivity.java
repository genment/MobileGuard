package gmt.mobileguard.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import gmt.mobileguard.R;
import gmt.mobileguard.sevice.UpdateService;
import gmt.mobileguard.util.EncryptUtil;
import gmt.mobileguard.util.SharedPrefsCtrl;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GridView gridView;
    private TextView notice;
    private AlertDialog dialog;

    private Class[] item_class = {
            // TODO: 2015/9/21 定义各个功能的 Activity.class
            SecurityActivity.class, null, null,
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
        setSupportActionBar(toolbar);

        notice = (TextView) findViewById(R.id.home_notice);
        gridView = (GridView) findViewById(R.id.home_gridview);
        gridView.setAdapter(new GridViewAdapter());
        gridView.setOnItemClickListener(this);

        getNewVersion();
    }

    private void getNewVersion() {
        final Bundle updateInfo = getIntent().getExtras();
        if (updateInfo != null && !updateInfo.isEmpty()) { // 如果有新版本
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
        // TODO: 2015/9/21 点击item后，跳转到各个功能界面(position==0除外,另外处理)
        if (position == 0) {
            showDialog();
        } else if (item_class[position] != null) {
            Intent intent = new Intent(this, item_class[position]);
            startActivity(intent);
        }
    }

    /**
     * 手机防盗的Dialog
     * <p/>
     * <p>点击按钮后对话框不消失的解决方案</p>
     * <a href="http://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked">How to prevent a dialog from closing when a button is clicked</a>
     */
    private void showDialog() {
        final boolean configed = SharedPrefsCtrl.getBoolean(SharedPrefsCtrl.Constant.SJFD_CONFIG, false);
        View dialogView;
        final EditText pwd2;
        if (configed) {
            dialogView = getLayoutInflater().inflate(R.layout.dialog_password_2, null);
            pwd2 = null;
        } else {
            dialogView = getLayoutInflater().inflate(R.layout.dialog_password_1, null);
            pwd2 = (EditText) dialogView.findViewById(R.id.password2);
        }
        final EditText pwd1 = (EditText) dialogView.findViewById(R.id.password);

        dialog = new AlertDialog.Builder(this)
                .setTitle(configed ? "输入密码" : "设置密码")
                .setView(dialogView)
                .setPositiveButton("确定", null) // 为了在验证密码时不会dismiss
                .setNegativeButton("取消", null)
                .show();

        // 为了在验证密码时不会dismiss，重写了PositiveButton的OnClickListener
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 已设置
                if (configed) {
                    if (!checkPassword(EncryptUtil.md5(pwd1.getText().toString()),
                            SharedPrefsCtrl.getString(SharedPrefsCtrl.Constant.SJFD_PWD, null))) {
                        Toast.makeText(HomeActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 未设置
                else {
                    if (checkPassword(pwd1.getText().toString(), pwd2.getText().toString())) {
                        SharedPrefsCtrl.putString(SharedPrefsCtrl.Constant.SJFD_PWD, EncryptUtil.md5(pwd1.getText().toString()));
                        SharedPrefsCtrl.putBoolean(SharedPrefsCtrl.Constant.SJFD_CONFIG, true);
                        Toast.makeText(HomeActivity.this, "设置密码成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        pwd2.setError("密码不一致");
                        return;
                    }
                }
                dialog.dismiss();
                // TODO: 2015/10/11 通过configed的值，决定SecurityActivity显示[向导]还是[手机防盗]的Fragment
                Intent intent = new Intent(HomeActivity.this, item_class[0]);
                intent.putExtra("configed", configed);
                startActivity(intent);
            }
        });
    }

    private boolean checkPassword(String pwd1, String pwd2) {
        return !TextUtils.isEmpty(pwd1) && TextUtils.equals(pwd1, pwd2);
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
