package gmt.mobileguard.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import gmt.mobileguard.R;
import gmt.mobileguard.util.SharedPrefsCtrl;

public class SecurityGuide4 extends Fragment implements View.OnClickListener {
    // 表示是否已经开启防盗的 key
    private static final String ARG_IS_OPENED = "is_opened";

    // 表示是否已经开启防盗的 value
    private boolean mIsOpened = true;

    private OnStepButtonClickedListener mListener;
    private TextView open_security_tips;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param isOpened 从Activity中传入是否已经开启防盗
     * @return A new instance of fragment SecurityGuide4.
     */
    public static SecurityGuide4 newInstance(boolean isOpened) {
        SecurityGuide4 fragment = new SecurityGuide4();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_OPENED, isOpened);
        fragment.setArguments(args);
        return fragment;
    }

    public SecurityGuide4() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取是否开启防盗
        mIsOpened = SharedPrefsCtrl.getBoolean(SharedPrefsCtrl.Constant.SJFD_SECURITY_STATUS, true);

        // TODO: 2015/11/14 暂时没用，以后再说。
        if (getArguments() != null) {
            mIsOpened = getArguments().getBoolean(ARG_IS_OPENED, true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_security_guide_4, null);

        contentView.findViewById(R.id.prev_step).setOnClickListener(this);
        contentView.findViewById(R.id.next_step).setOnClickListener(this);

        open_security_tips = (TextView) contentView.findViewById(R.id.guide_4_open_security_tips);
        updateStatus(mIsOpened);
        Switch _switch = ((Switch) contentView.findViewById(R.id.guide_4_open_security));
        _switch.setChecked(mIsOpened);
        _switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateStatus(isChecked);
            }
        });
        return contentView;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            if (v.getId() == R.id.next_step) {
                mListener.onStepButtonClicked(5);
            } else {
                mListener.onStepButtonClicked(3);
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

    public void updateStatus(boolean status) {
        // 保存状态
        SharedPrefsCtrl.putBoolean(SharedPrefsCtrl.Constant.SJFD_SECURITY_STATUS, status);
        if (status) {
            open_security_tips.setText(R.string.tip_guide4_2_on);
        } else {
            open_security_tips.setText(R.string.tip_guide4_2_off);
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
