package gmt.mobileguard.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

import gmt.mobileguard.util.InterceptUtil;

public class InterceptingService extends Service {

    private TelephonyManager mTelephonyManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 注销监听
     */
    @Override
    public void onDestroy() {
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    private void checkBlack(String number) {
        boolean isBlack = InterceptUtil.checkIntercept(InterceptingService.this,
                new String[]{number, null}, "call");
        if (isBlack && endCall()) {
            // TODO: 2015/12/1 显示通知？？？
            Log.i("InterceptingService", "------ 已拦截: " + number + " 的来电 ------");
        }
    }

    /**
     * 参考：http://blog.sina.com.cn/s/blog_a28e3dd901018wky.html
     */
    private boolean endCall() {
        try {
            Class<?> telephonyManagerClass = TelephonyManager.class;
            Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony", (Class[]) null);
            getITelephonyMethod.setAccessible(true);
            ITelephony iTelephonyObject = (ITelephony) getITelephonyMethod.invoke(mTelephonyManager, (Object[]) null);
            return iTelephonyObject.endCall();
        } catch (Exception e) {
            // Never. Nothing to do.
        }
        return false;
    }

    PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {

                // 1.来电不接: 有号码 2.来电接: 无号码 3.4.去电: 一律没有
                case TelephonyManager.CALL_STATE_IDLE: {
                    // TODO: 2015/11/30 隐藏归属地？？？
                    break;
                }
                // 肯定是来电，而且有号码
                case TelephonyManager.CALL_STATE_RINGING: {
                    // TODO: 2015/11/30 显示归属地？？？
                    checkBlack(incomingNumber);
                    break;
                }
                // 要么是去电，要么是通话中，都没有号码
                case TelephonyManager.CALL_STATE_OFFHOOK: {
                    // Nothing to do.
                }
            }
        }
    };
}
