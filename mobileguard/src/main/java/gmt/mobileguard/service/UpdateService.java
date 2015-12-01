package gmt.mobileguard.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import gmt.mobileguard.R;

/**
 * Project: StudyDemo
 * Package: gmt.mobileguard.utils
 * Created by Genment at 2015/9/19 02:05.
 */
public class UpdateService extends Service {
    DownloadManager.Request request;
    DownloadManager manager;
    long fileId;
    DownloadReceiver downloadReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("apk");
        request = new DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription("正在下载更新包...")
                .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, url.substring(url.lastIndexOf('/') + 1))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // visible and shows while in progress and after completion.
                /*.setVisibleInDownloadsUi(true)*/; // Set whether this download should be displayed in the system's Downloads UI. true by default,
        startListenDownload();
        startDownload();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopListenDownload();
    }

    public void startDownload() {
        fileId = manager.enqueue(request);
    }

    public void cancelDownload() {
        manager.remove(fileId);
    }

    private void startListenDownload() {
        downloadReceiver = new DownloadReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        intentFilter.addAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        this.registerReceiver(downloadReceiver, intentFilter);
    }

    private void stopListenDownload() {
        if (downloadReceiver != null) {
            this.unregisterReceiver(downloadReceiver);
        }
    }

    class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
                    stopService(new Intent(UpdateService.this, UpdateService.class)); // 下载完成后, 停止服务
                    Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
                    break;
                case DownloadManager.ACTION_NOTIFICATION_CLICKED:
                    // TODO: 2015/9/19 下载时点击通知后执行的事情
                    break;
            }
        }
    }
}
