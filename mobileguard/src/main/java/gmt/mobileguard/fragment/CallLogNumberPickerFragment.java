package gmt.mobileguard.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import gmt.mobileguard.R;
import gmt.mobileguard.util.PickerNumberUtil;
import gmt.mobileguard.widget.SuperCursorAdapter;
import gmt.mobileguard.widget.SuperViewHolder;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.fragment
 * Created by Genment at 2015/11/27 23:35.
 */
public class CallLogNumberPickerFragment extends BaseNumberPickerFragment {

    @Override
    protected SuperCursorAdapter createAdapter() {
        Cursor cursor = PickerNumberUtil.getCallLogs(getActivity());
        if (cursor != null) {
            return new SuperCursorAdapter(getActivity(), cursor, R.layout.item_picker_calllog, 0) {
                //                SimpleDateFormat sdf = new SimpleDateFormat(/*"MM-dd HH:mm", */);
                SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getInstance();

                @Override
                public void convert(SuperViewHolder holder, Cursor cursor) {
                    String name = cursor.getString(1);
                    if (TextUtils.isEmpty(name)) {
                        name = "(未知)";
                        holder.setTextColor(R.id.name, Color.GRAY);
                    } else {
                        holder.setTextColor(R.id.name, Color.BLACK);
                    }
                    holder.setText(R.id.name, name)
                            .setText(R.id.number, cursor.getString(2))
                            .setText(R.id.time, sdf.format(new Date(cursor.getLong(3))));
                }
            };
        }
        return null;
    }
}
