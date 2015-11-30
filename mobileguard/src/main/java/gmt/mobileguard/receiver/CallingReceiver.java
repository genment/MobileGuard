package gmt.mobileguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import gmt.mobileguard.sevice.InterceptingService;

/**
 * <p>接受两种广播：外拨广播、电话状态广播</p>
 * <p>以下是两种情况下的完整的通话过程，包括外拨和来电。</p>
 * <p>外拨情况：</p>
 * <p>1. 外拨电话时，此接收者会收到两次广播：先接收 NEW_OUTGOING_CALL，再接收 PHONE_STATE。</p>
 * <p>x.（对方接通时，本机没有任何状态的改变。）</p>
 * <p>2. 任何一方挂断时，接收到 PHONE_STATE。</p>
 * <p>来电情况：</p>
 * <p>1. 响铃时，接收到 PHONE_STATE。</p>
 * <p>2. 通话时，接收到 PHONE_STATE。（任何一方直接挂断时，没有这一步）</p>
 * <p>3. 任何一方挂断时，接收到 PHONE_STATE。</p>
 * <p/>
 * <p>由于 PHONE_STATE 不管是外拨还是来电，都会被接收多次。</p>
 * <p>所以相对来说比较适合用于开启拦截服务（多次startService()并无影响，只回调onStartCommand()）</p>
 */
public class CallingReceiver extends BroadcastReceiver {
    public CallingReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(action)) {
            // TODO: 2015/11/30 显示归属地？？？
            Log.i("CallingReceiver", "onReceive: NEW_OUTGOING_CALL - "
                    + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));

        } else if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(action)) {

            // 开启拦截服务
            context.startService(intent.setClass(context, InterceptingService.class));
        }
    }
}