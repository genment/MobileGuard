package gmt.mobileguard.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import gmt.mobileguard.R;

public class SecurityGuide3 extends Fragment implements View.OnClickListener {
    // 表示是否已经绑定 SIM 卡的 key
    private static final String ARG_PHONE_NUMBER = "phone_number";

    // 表示是否已经绑定 SIM 卡的 value
    private String mPhoneNumber;

    private OnStepButtonClickedListener mListener;

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

        TextView phone = (TextView) contentView.findViewById(R.id.guide_3_phone);
        phone.setText(mPhoneNumber);
        Button select_phone = ((Button) contentView.findViewById(R.id.guide_3_select_phone));
        select_phone.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.guide_3_select_phone) {
            // TODO: 2015/11/5 跳转到系统的获取联系人界面
            Toast.makeText(getActivity(), R.string.tip_guide3_3, Toast.LENGTH_SHORT).show();
        } else if (mListener != null) {
            if (v.getId() == R.id.next_step) {
                mListener.onStepButtonClicked(3, true);
            } else {
                mListener.onStepButtonClicked(3, false);
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
         * @param guide_id 当前 Guide 位置
         * @param next     下一步还是上一步，true 表示 下一步，false 表示 上一步
         */
        public void onStepButtonClicked(int guide_id, boolean next);
    }
}
