package gmt.mobileguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.text.TextUtils;

import gmt.mobileguard.util.DeviceInfo;
import gmt.mobileguard.util.EncryptUtil;
import gmt.mobileguard.util.SharedPrefsCtrl;

public class DeviceBootReceiver extends BroadcastReceiver {
    public DeviceBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean securityStatus = SharedPrefsCtrl.getBoolean(SharedPrefsCtrl.Constant.SJFD_SECURITY_STATUS, false);
        String securityPhone = SharedPrefsCtrl.getString(SharedPrefsCtrl.Constant.SJFD_SECURITY_PHONE, null);
        if (securityStatus && !TextUtils.isEmpty(securityPhone)) {
            boolean isBind = SharedPrefsCtrl.getBoolean(SharedPrefsCtrl.Constant.SJFD_BIND_SIM, false);
            String simInfo = SharedPrefsCtrl.getString(SharedPrefsCtrl.Constant.SJFD_SIM, null);
            if (isBind && !TextUtils.isEmpty(simInfo)) {
                String currentSim = EncryptUtil.md5(DeviceInfo.getIMSI(context));
                if (!TextUtils.isEmpty(currentSim) && !currentSim.equals(simInfo)) {
                    sendSecuritySms(securityPhone);
                }
            }
        }
    }

    /**
     * 发送短信
     *
     * @param phoneNumber 接受短信的号码
     */
    // TODO: 2015/11/15 放到工具类？信息内容以后再说。
    // TODO: 2015/11/15 信息会在短信app中看得到
    private void sendSecuritySms(String phoneNumber) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(
                phoneNumber,
                null,
                "手机SIM卡已被更换，更换后的手机号为本信息的发信号码，有可能是手机被盗了。如果是本人操作，请忽略。【手机卫士】",
                null,
                null
        );
    }
}
