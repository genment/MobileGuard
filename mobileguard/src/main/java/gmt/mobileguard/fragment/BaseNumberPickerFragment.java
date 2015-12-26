package gmt.mobileguard.fragment;

import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import gmt.mobileguard.widget.SuperCursorAdapter;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.fragment
 * Created by Genment at 2015/11/27 11:41.
 */
public abstract class BaseNumberPickerFragment extends ListFragment {
    private OnListFragmentItemClickListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(createAdapter());
    }

    protected abstract SuperCursorAdapter createAdapter();

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        SuperCursorAdapter adapter = (SuperCursorAdapter) l.getAdapter();
        if (mListener != null) {
            mListener.onListFragmentItemClick(adapter.getItem(position));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentItemClickListener) {
            mListener = (OnListFragmentItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * 数据
     */
//    public static class Data {
//        public long id;            // 数据库id
//        public String number;      // 电话号码
//        public String name;        // 联系人名称
//        public String message;     // 短信内容
//        public long lastTimeStamp; // 最近通话时间
//    }

    /**
     * Activity 必须实现此接口
     */
    public interface OnListFragmentItemClickListener {
        void onListFragmentItemClick(Cursor cursor);
    }
}
