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

public class SecurityGuide2 extends Fragment implements View.OnClickListener {
    // 表示是否已经绑定 SIM 卡的 key
    private static final String ARG_IS_BINDED = "is_binded";

    // 表示是否已经绑定 SIM 卡的 value
    private boolean mIsBinded = true;

    private OnStepButtonClickedListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param isBonded 从Activity中传入是否已经绑定SIM卡
     * @return A new instance of fragment SecurityGuide2.
     */
    public static SecurityGuide2 newInstance(boolean isBonded) {
        SecurityGuide2 fragment = new SecurityGuide2();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_BINDED, isBonded);
        fragment.setArguments(args);
        return fragment;
    }

    public SecurityGuide2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsBinded = getArguments().getBoolean(ARG_IS_BINDED, true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_security_guide_2, null);

        contentView.findViewById(R.id.prev_step).setOnClickListener(this);
        contentView.findViewById(R.id.next_step).setOnClickListener(this);

        final TextView bind_sim_tips = (TextView) contentView.findViewById(R.id.guide_2_bind_sim_tips);
        Switch swich = ((Switch) contentView.findViewById(R.id.guide_2_bind_sim));
        swich.setChecked(mIsBinded);
        swich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bind_sim_tips.setText(R.string.tip_guide2_3_on);
                } else {
                    bind_sim_tips.setText(R.string.tip_guide2_3_off);
                }
            }
        });
        return contentView;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            if (v.getId() == R.id.next_step) {
                mListener.onStepButtonClicked(2, true);
            } else {
                mListener.onStepButtonClicked(2, false);
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
