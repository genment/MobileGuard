package gmt.mobileguard.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import gmt.mobileguard.R;
import gmt.mobileguard.util.SharedPrefsCtrl;

public class SecurityFragment extends Fragment {

    private OnStepButtonClickedListener mListener;

    public SecurityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_security, container, false);

        String security_phone = SharedPrefsCtrl.getString(SharedPrefsCtrl.Constant.SJFD_SECURITY_PHONE, null);

        ((TextView) contentView.findViewById(R.id.phone)).setText(
                TextUtils.isEmpty(security_phone) ?
                        getString(R.string.unsetting) :
                        security_phone);
        ((ImageView) contentView.findViewById(R.id.security_status)).setImageResource(
                SharedPrefsCtrl.getBoolean(SharedPrefsCtrl.Constant.SJFD_SECURITY_STATUS, false) ?
                        R.drawable.lock :
                        R.drawable.unlock);
        contentView.findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onStepButtonClicked(1);
                }
            }
        });

        return contentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnStepButtonClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnStepButtonClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStepButtonClickedListener {
        /**
         * 当"设置"按钮被点击时回调
         *
         * @param goToGuide 要切换到哪个 Guide
         */
        public void onStepButtonClicked(int goToGuide);
    }
}
