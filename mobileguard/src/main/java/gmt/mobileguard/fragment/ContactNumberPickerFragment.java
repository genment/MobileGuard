package gmt.mobileguard.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gmt.mobileguard.R;
import gmt.mobileguard.util.PickerNumberUtil;

public class ContactNumberPickerFragment extends BaseNumberPickerFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Data> datas = PickerNumberUtil.getDataFromContact(getActivity());
        setListAdapter(new ContactListAdapter(datas));
    }

    /**
     * Adapter
     */
    private class ContactListAdapter extends BasePickerAdapter<ContactListAdapter.ViewHolder> {

        public ContactListAdapter(List<Data> datas) {
            super(datas);
        }

        @Override
        public ViewHolder onCreateHolder(ViewGroup parent) {
            View itemView = LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_picker_contact, parent, false);
            return new ViewHolder(itemView);
        }


        @Override
        protected void onBindViewHolder(ViewHolder viewHolder, int position) {
            Data data = (Data) getItem(position);
            viewHolder.name.setText(data.name);
            viewHolder.number.setText(data.number);
        }

        class ViewHolder extends BaseNumberPickerFragment.ViewHolder {
            TextView name;
            TextView number;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                number = (TextView) itemView.findViewById(R.id.number);
            }
        }
    }
}
