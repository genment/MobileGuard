package gmt.mobileguard.fragment;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gmt.mobileguard.R;

public class AppsListFragment extends ListFragment {

    public static final int TYPE_USER_APP = 0;
    public static final int TYPE_SYS_APP = 1;
    private static final String ARG_TYPE = "type";
    private int mType = TYPE_USER_APP;

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

    static class Adapter extends BaseAdapter {

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
            for (ApplicationInfo appinfo : installedApplications) {
                if (type == AppsListFragment.TYPE_SYS_APP
                        && (appinfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    mAppInfos.add(appinfo);
                }
                if (type == AppsListFragment.TYPE_USER_APP
                        && (appinfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    mAppInfos.add(appinfo);
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
