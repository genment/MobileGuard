package gmt.mobileguard.util;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import java.io.IOException;

import gmt.mobileguard.sevice.LocationService;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.util
 * Created by Genment at 2015/11/16 15:25.
 */
public class SecurityUtil {

    public static final String ACTION_LOCATION = "*#location#*";
    public static final String ACTION_ALARM = "*#alarm#*";
    public static final String ACTION_WIPE_DATA = "*#wipedata#*";
    public static final String ACTION_LOCK_SCREEN = "*#lockscreen#*";

    /**
     * The MD5 value of {@link #ACTION_LOCATION}
     */
    public static final String ACTION_LOCATION_MD5 = "8aa3f4751f4f99e83f113d155c8f3073";

    /**
     * The MD5 value of {@link #ACTION_ALARM}
     */
    public static final String ACTION_ALARM_MD5 = "9aca3e050c71aa6a25aa3fb2b142ec54";

    /**
     * The MD5 value of {@link #ACTION_WIPE_DATA}
     */
    public static final String ACTION_WIPE_DATA_MD5 = "415190a06ae7d000c2a5051dd9721b72";

    /**
     * The MD5 value of {@link #ACTION_LOCK_SCREEN}
     */
    public static final String ACTION_LOCK_SCREEN_MD5 = "06f25c9c40f28139f6478d1c9815a9e8";

    /**
     * 发送防盗短信
     *
     * @param msg 短信内容
     */
    public static void sendSecuritySms(@NonNull String msg) {
        String dest = getSecurityPhone();
        if (!TextUtils.isEmpty(dest) && !TextUtils.isEmpty(msg)) {
            sendSms(dest, msg);
        }
    }

    /**
     * 处理短信内容。
     *
     * @param context 短信接收者
     *                （{@link gmt.mobileguard.receiver.SmsReceivingReceiver#onReceive(Context, Intent)}）
     *                接收到的 intent 对象，用于解析短信内容
     * @param intent  短信接收者
     *                （{@link gmt.mobileguard.receiver.SmsReceivingReceiver#onReceive(Context, Intent)}）
     *                接收到的 intent 对象，用于播放音频
     * @return 是否属于安全短信
     */
    public static boolean disposeSecuritySms(Context context, Intent intent) {
        boolean result = true;
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        StringBuilder messageBody = new StringBuilder(32);
        int pduCount = messages.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            byte[] pdu = (byte[]) messages[i];
            msgs[i] = SmsMessage.createFromPdu(pdu);
            messageBody.append(msgs[i].getMessageBody());
        }
        switch (messageBody.toString()) {
            case ACTION_LOCATION:
            case ACTION_LOCATION_MD5:
                startLocationService(context);
                break;
            case ACTION_ALARM:
            case ACTION_ALARM_MD5:
                playAlarmSound(context);
                break;
            case ACTION_WIPE_DATA:
            case ACTION_WIPE_DATA_MD5:
                wipeUserData(context);
                break;
            case ACTION_LOCK_SCREEN:
            case ACTION_LOCK_SCREEN_MD5:
                lockScreen(context);
                break;
            default:
                result = false;
        }
        return result;
    }

    private static String getSecurityPhone() {
        return SharedPrefsCtrl.getString(SharedPrefsCtrl.Constant.SJFD_SECURITY_PHONE, null);
    }

    private static void sendSms(String dest, String msg) {
        SmsManager.getDefault().sendTextMessage(dest, null, msg, null, null);
    }

    private static void startLocationService(Context context) {
        Intent intent = new Intent(context, LocationService.class);
        context.startService(intent);
    }

    private static void playAlarmSound(Context context) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        AssetManager assetManager = context.getAssets();
        try {
            mediaPlayer.setDataSource(assetManager.openFd("thief.mp3").getFileDescriptor());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void lockScreen(Context context) {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdmin = new ComponentName(context, MyDeviceAdminReceiver.class);
        if (dpm.isAdminActive(deviceAdmin)) {
            dpm.resetPassword(getSecurityPhone(), DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
            dpm.lockNow();
        }
    }

    private static void wipeUserData(Context context) {
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdmin = new ComponentName(context, MyDeviceAdminReceiver.class);
        if (dpm.isAdminActive(deviceAdmin)) {
            dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        }
    }

    /**
     * 设备管理器
     */
    public static class MyDeviceAdminReceiver extends DeviceAdminReceiver {
        // Just Empty.
    }
}
