package gmt.mobileguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import gmt.mobileguard.util.InterceptUtil;
import gmt.mobileguard.util.SecurityUtil;

public class SmsReceivingReceiver extends BroadcastReceiver {
    public SmsReceivingReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // 获取短信的手机号码和内容, [0]:address, [1]:message body
        String[] addressAndBody = SecurityUtil.getSmsAddressAndBody(intent);

        // 优先处理防盗指令
        if (SecurityUtil.checkSecuritySms(context, addressAndBody[1])          // 是否防盗指令短信
                || InterceptUtil.checkIntercept(context, addressAndBody, "sms")) { // 是否需要拦截

            abortBroadcast();
        }
    }
}
