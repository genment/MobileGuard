package gmt.mobileguard.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.fragment
 * Created by Genment at 2015/11/27 11:41.
 */
public class BaseNumberPickerFragment extends ListFragment {

    private OnListFragmentItemClickListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (mListener != null) {
            mListener.onListFragmentItemClick((Data) l.getAdapter().getItem(position));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnListFragmentItemClickListener) {
            mListener = (OnListFragmentItemClickListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListFragmentItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static abstract class BasePickerAdapter<VH extends ViewHolder> extends BaseAdapter {

        private final List<Data> Datas;

        public BasePickerAdapter(List<Data> datas) {
            this.Datas = datas;
        }

        @Override
        public int getCount() {
            return Datas.size();
        }

        @Override
        public Object getItem(int position) {
            return Datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            VH viewHolder;
            if (convertView == null) {
                viewHolder = onCreateHolder(parent);
                convertView = viewHolder.mItemView;
                convertView.setTag(viewHolder);
            } else {
                //noinspection unchecked
                viewHolder = (VH) convertView.getTag();
            }
            onBindViewHolder(viewHolder, position);
            return convertView;
        }

        protected abstract VH onCreateHolder(ViewGroup parent);

        protected abstract void onBindViewHolder(VH viewHolder, int position);
    }

    public static abstract class ViewHolder {
        View mItemView;

        public ViewHolder(View itemView) {
            mItemView = itemView;
        }
    }

    /**
     * 数据
     */
    public static class Data {
        public long id;            // 数据库id
        public String number;      // 电话号码
        public String name;        // 联系人名称
        public String message;     // 短信内容
        public long lastTimeStamp; // 最近通话时间
    }

    /**
     * Activity 必须实现此接口
     */
    public interface OnListFragmentItemClickListener {
        void onListFragmentItemClick(Data data);
    }
}
