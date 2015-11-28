package gmt.mobileguard.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gmt.mobileguard.R;
import gmt.mobileguard.activity.NumberSelectionActivity;
import gmt.mobileguard.util.SharedPrefsCtrl;

public class SecurityGuide3 extends Fragment implements View.OnClickListener {
    // 表示是否已经绑定 SIM 卡的 key
    private static final String ARG_PHONE_NUMBER = "phone_number";

    // 表示是否已经绑定 SIM 卡的 value
    private String mPhoneNumber;

    private OnStepButtonClickedListener mListener;
    private static final int REQUEST_CODE_CONTACT = 1;

    private TextView phone;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param phone_number 从Activity中传入安全号码
     * @return A new instance of fragment SecurityGuide3.
     */
    public static SecurityGuide3 newInstance(String phone_number) {
        SecurityGuide3 fragment = new SecurityGuide3();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phone_number);
        fragment.setArguments(args);
        return fragment;
    }

    public SecurityGuide3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取是否绑定SIM卡
        mPhoneNumber = SharedPrefsCtrl.getString(SharedPrefsCtrl.Constant.SJFD_SECURITY_PHONE, "");

        // TODO: 2015/11/14 暂时没用，以后再说。
        if (getArguments() != null) {
            mPhoneNumber = getArguments().getString(ARG_PHONE_NUMBER, "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_security_guide_3, null);

        contentView.findViewById(R.id.prev_step).setOnClickListener(this);
        contentView.findViewById(R.id.next_step).setOnClickListener(this);

        phone = (TextView) contentView.findViewById(R.id.guide_3_phone);
        phone.setText(mPhoneNumber);
        contentView.findViewById(R.id.guide_3_select_phone).setOnClickListener(this);
        return contentView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.guide_3_select_phone) {
            // 跳转到系统的获取联系人界面
            startSelectionActivity();
        } else if (mListener != null) {
            // 点击“上一步”/“下一步”时，保存号码
            SharedPrefsCtrl.putString(SharedPrefsCtrl.Constant.SJFD_SECURITY_PHONE, phone.getText().toString());
            if (v.getId() == R.id.next_step) {
                mListener.onStepButtonClicked(4);
            } else {
                mListener.onStepButtonClicked(2);
            }
        }
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
     * 开启 NumberSelectionActivity 获取电话号码
     */
    private void startSelectionActivity() {
        Intent intent = new Intent();
        intent.setAction(NumberSelectionActivity.ActionIntents.ACITON_PICK_CONTACT);
        startActivityForResult(intent, REQUEST_CODE_CONTACT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            phone.setText(data.getStringExtra("number"));
        }
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
         * 当步骤按钮被点击时回调
         *
         * @param goToGuide 要切换到哪个 Guide
         */
        public void onStepButtonClicked(int goToGuide);
    }
}
