package gmt.mobileguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import gmt.mobileguard.service.InterceptingService;
import gmt.mobileguard.util.DeviceInfo;
import gmt.mobileguard.util.EncryptUtil;
import gmt.mobileguard.util.SecurityUtil;
import gmt.mobileguard.util.SharedPrefsCtrl;

public class DeviceBootReceiver extends BroadcastReceiver {
    public DeviceBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        checkSimInfo(context);
        startInterceptingService(context);
    }

    private void checkSimInfo(Context context) {
        boolean securityStatus = SharedPrefsCtrl.getBoolean(SharedPrefsCtrl.Constant.SJFD_SECURITY_STATUS, false);
        if (securityStatus) {
            boolean isBind = SharedPrefsCtrl.getBoolean(SharedPrefsCtrl.Constant.SJFD_BIND_SIM, false);
            String simInfo = SharedPrefsCtrl.getString(SharedPrefsCtrl.Constant.SJFD_SIM, null);
            if (isBind && !TextUtils.isEmpty(simInfo)) {
                String currentSim = EncryptUtil.md5(DeviceInfo.getIMSI(context));
                if (!TextUtils.isEmpty(currentSim) && !currentSim.equals(simInfo)) {
                    SecurityUtil.sendSecuritySms("手机SIM卡已被更换，更换后的手机号为本信息的发信号码，有可能是手机被盗了。如果是本人操作，请忽略。【手机卫士】");
                }
            }
        }
    }

    private void startInterceptingService(Context context) {
        Intent intent = new Intent(context, InterceptingService.class);
        context.startService(intent);
    }
}
