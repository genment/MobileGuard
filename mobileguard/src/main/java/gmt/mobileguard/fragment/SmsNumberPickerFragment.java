package gmt.mobileguard.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gmt.mobileguard.R;
import gmt.mobileguard.util.PickerNumberUtil;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.fragment
 * Created by Genment at 2015/11/27 23:37.
 */
public class SmsNumberPickerFragment extends BaseNumberPickerFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Data> datas = PickerNumberUtil.getDataFromSms(getActivity());
        setListAdapter(new SmsListAdapter(datas));
    }

    private class SmsListAdapter extends BasePickerAdapter<SmsListAdapter.ViewHolder> {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");

        public SmsListAdapter(List<Data> datas) {
            super(datas);
        }

        @Override
        protected ViewHolder onCreateHolder(ViewGroup parent) {
            View itemView = LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_picker_sms, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        protected void onBindViewHolder(ViewHolder viewHolder, int position) {
            Data data = (Data) getItem(position);
            viewHolder.name.setText(TextUtils.isEmpty(data.name) ? "(未知)" : data.name);
            viewHolder.number.setText(data.number);
            viewHolder.message.setText(data.message);
            viewHolder.time.setText(sdf.format(new Date(data.lastTimeStamp)));
        }

        class ViewHolder extends BaseNumberPickerFragment.ViewHolder {
            TextView name;
            TextView number;
            TextView time;
            TextView message;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                number = (TextView) itemView.findViewById(R.id.number);
                message = (TextView) itemView.findViewById(R.id.message);
                time = (TextView) itemView.findViewById(R.id.time);
            }
        }
    }
}
