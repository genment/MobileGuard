package gmt.mobileguard.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gmt.mobileguard.R;

public class AppsListFragment extends ListFragment implements View.OnClickListener {

    public static final int TYPE_USER_APP = 0;
    public static final int TYPE_SYS_APP = 1;
    private static final String ARG_TYPE = "type";
    private int mType = TYPE_USER_APP;

    private static final int REQUEST_UNINSTALL_PACKAGE = 1;
    private int mPos;

    private PopupWindow mPopupWindow;

    public static AppsListFragment newInstance(int type) {
        AppsListFragment fragment = new AppsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_TYPE);
        }
        setListAdapter(new Adapter(getActivity(), mType));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mPos = position;
        /**
         * 如果是用户应用，则弹出 PopupWindow，可选择操作；
         * 如果是系统应用，则无 PopupWindow，直接进入详情。
         */
        if (mType == TYPE_SYS_APP) {
            appDetail();
            return;
        }

        // 用户应用
        if (mPopupWindow == null) {
            initPopupWindow(v);
        }
        mPopupWindow.showAsDropDown(v, v.getWidth() / 2, -v.getHeight());
    }

    private void initPopupWindow(View itemView) {
        View popupView = LayoutInflater.from(getActivity())
                .inflate(R.layout.popup_app_manager, getListView(), false);
        popupView.findViewById(R.id.app_run).setOnClickListener(this);
        popupView.findViewById(R.id.app_uninstall).setOnClickListener(this);
        popupView.findViewById(R.id.app_detail).setOnClickListener(this);
        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                itemView.getHeight()); // TODO: 2015/12/14 由于背景图的原因，导致popupView过高。
        /**
         * 点击 popup 以外的地方 dismiss。
         * 参考：http://www.cnblogs.com/sw926/p/3230659.html
         */
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
    }

    private void dismissPopupWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * On PopupWindow's Item Click.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_run:
                appRun();
                break;
            case R.id.app_uninstall:
                appUninstall();
                break;
            case R.id.app_detail:
                appDetail();
                break;
        }
        dismissPopupWindow();
    }

    private void appRun() {
        ApplicationInfo appinfo = (ApplicationInfo) getListAdapter().getItem(mPos);
        // 如果是本程序自身，无需运行。
        if (appinfo.packageName.equals(getActivity().getPackageName())) {
            return;
        }
        Intent appLaunchIntent = getActivity().getPackageManager()
                .getLaunchIntentForPackage(appinfo.packageName);
        if (appLaunchIntent == null) {
            Snackbar.make(getListView(), "无法运行该APP。", Snackbar.LENGTH_SHORT).show();
            return;
        }
        startActivity(appLaunchIntent);
    }

    private void appUninstall() {
        ApplicationInfo appinfo = (ApplicationInfo) getListAdapter().getItem(mPos);
        Intent appUninstallIntent = new Intent();
        appUninstallIntent.setAction(Intent.ACTION_UNINSTALL_PACKAGE);
        appUninstallIntent.setData(Uri.parse("package:" + appinfo.packageName));
        appUninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true); // 必须，返回 resultCode。
        startActivityForResult(appUninstallIntent, REQUEST_UNINSTALL_PACKAGE);
    }

    private void appDetail() {
        ApplicationInfo appinfo = (ApplicationInfo) getListAdapter().getItem(mPos);
        Intent appDetailIntent = new Intent();
        appDetailIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        appDetailIntent.setData(Uri.parse("package:" + appinfo.packageName));
        startActivity(appDetailIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Adapter adapter = (Adapter) getListAdapter();
            adapter.mAppInfos.remove(mPos);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Close the popup window.
        dismissPopupWindow();
        mPopupWindow = null;
    }

    private static class Adapter extends BaseAdapter {

        private List<ApplicationInfo> mAppInfos;
        private Context mContext;
        private PackageManager mPm;

        public Adapter(Context mContext, int type) {
            this.mContext = mContext;
            mPm = mContext.getPackageManager();
            initAppsDatas(type);
        }

        private void initAppsDatas(int type) {
            mAppInfos = new ArrayList<>();
            List<ApplicationInfo> installedApplications = mPm.getInstalledApplications(0);
            if (type == AppsListFragment.TYPE_SYS_APP) {
                for (ApplicationInfo appinfo : installedApplications) {
                    if ((appinfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        mAppInfos.add(appinfo);
                    }
                }
            } else {
                for (ApplicationInfo appinfo : installedApplications) {
                    if ((appinfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        mAppInfos.add(appinfo);
                    }
                }
            }
        }

        @Override
        public int getCount() {
            return mAppInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return mAppInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_apps_list, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.app_icon);
                viewHolder.name = (TextView) convertView.findViewById(R.id.app_name);
                viewHolder.size = (TextView) convertView.findViewById(R.id.app_size);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ApplicationInfo appinfo = mAppInfos.get(position);
            viewHolder.icon.setImageDrawable(appinfo.loadIcon(mPm));
            viewHolder.name.setText(appinfo.loadLabel(mPm));
            viewHolder.size.setText(Formatter.formatFileSize(mContext,
                    new File(appinfo.sourceDir).length()));
            return convertView;
        }

        class ViewHolder {
            ImageView icon;
            TextView name;
            TextView size;
        }
    }
}
