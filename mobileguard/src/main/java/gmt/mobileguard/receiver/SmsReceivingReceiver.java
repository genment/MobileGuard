package gmt.mobileguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import gmt.mobileguard.util.SecurityUtil;

public class SmsReceivingReceiver extends BroadcastReceiver {
    public SmsReceivingReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SecurityUtil.disposeSecuritySms(context, intent)) {
            abortBroadcast();
        }
    }
}
