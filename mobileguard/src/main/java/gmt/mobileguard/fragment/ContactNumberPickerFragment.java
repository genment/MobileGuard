package gmt.mobileguard.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;

import gmt.mobileguard.R;
import gmt.mobileguard.util.PickerNumberUtil;
import gmt.mobileguard.widget.SuperCursorAdapter;
import gmt.mobileguard.widget.SuperViewHolder;

public class ContactNumberPickerFragment extends BaseNumberPickerFragment {

    @Override
    protected SuperCursorAdapter createAdapter() {
        Cursor cursor = PickerNumberUtil.getContact(getActivity());
        if (cursor != null) {
            return new SuperCursorAdapter(getActivity(), cursor,
                    R.layout.item_picker_contact, SuperCursorAdapter.NO_FLAGS) {

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
                            .setText(R.id.number, cursor.getString(2));
                }
            };
        }
        return null;
    }
}
