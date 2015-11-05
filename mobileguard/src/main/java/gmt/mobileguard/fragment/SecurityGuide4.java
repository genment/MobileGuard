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

public class SecurityGuide4 extends Fragment implements View.OnClickListener {
    // 表示是否已经开启防盗的 key
    private static final String ARG_IS_OPENED = "is_opened";

    // 表示是否已经开启防盗的 value
    private boolean mIsOpened = true;

    private OnStepButtonClickedListener mListener;

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

        final TextView open_security_tips = (TextView) contentView.findViewById(R.id.guide_4_open_security_tips);
        Switch swich = ((Switch) contentView.findViewById(R.id.guide_4_open_security));
        swich.setChecked(mIsOpened);
        swich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    open_security_tips.setText(R.string.tip_guide4_2_on);
                } else {
                    open_security_tips.setText(R.string.tip_guide4_2_off);
                }
            }
        });
        return contentView;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            if (v.getId() == R.id.next_step) {
                mListener.onStepButtonClicked(4, true);
            } else {
                mListener.onStepButtonClicked(4, false);
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
