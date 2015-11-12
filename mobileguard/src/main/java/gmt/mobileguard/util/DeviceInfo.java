package gmt.mobileguard.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.util
 * Created by Genment at 2015/11/11 22:53.
 */
public class DeviceInfo {

    /**
     * 获取SIM卡的标识（IMSI）
     * <p/>
     * IMSI 号码是 SIM 卡的唯一标识，详情请见: https://en.wikipedia.org/wiki/International_mobile_subscriber_identity
     * <p/>
     * Requires Permission:
     * {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     *
     * @param context Context 对象
     * @return IMSI 号码
     */
    public static String getIMSI(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    /**
     * 获取设备ID（IMEI）
     * <p/>
     * IMEI 号码是设备的唯一标识，详情请见: https://zh.wikipedia.org/wiki/IMEI
     * <p/>
     * Requires Permission:
     * {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     *
     * @param context Context 对象
     * @return IMEI 号码
     */
    public static String getIMEI(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 获取设备的唯一识别码（UUID）
     * <p/>
     * 详情请见: https://en.wikipedia.org/wiki/Universally_unique_identifier
     * <p/>
     * 算法来源：http://www.cnblogs.com/xiaowenji/archive/2011/01/11/1933087.html
     * <p/>
     * Requires Permission:
     * {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     *
     * @param context Context 对象
     * @return UUID 值
     */
    public static String getDeviceUUID(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        String imsi = tm.getSubscriberId();
        return new UUID(imei.hashCode(), imsi.hashCode()).toString();
    }
}
