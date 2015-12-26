package gmt.mobileguard.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.util
 * Created by Genment at 2015/11/27 15:10.
 */
public class PickerNumberUtil {

    /**
     * 通话记录
     *
     * @param context Context
     * @return Call Log
     */
    public static Cursor getCallLogs(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                CallLog.Calls.CONTENT_URI,
                new String[]{
                        CallLog.Calls._ID,
                        CallLog.Calls.CACHED_NAME,
                        CallLog.Calls.NUMBER,
                        CallLog.Calls.DATE
                },
                null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            return cursor;
        }
        return null;
    }

    /**
     * 短信
     *
     * @param context Context
     * @return SMS
     */
    public static Cursor getSms(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = compatibilityUri(resolver);
        if (cursor != null) {
            return cursor;
        }
        return null;
    }

    /**
     * 由于在 android 4.4 开始，Google 开放了 Telephony 的 API，所以可以直接使用 API 提供的常量进行查询；
     * 而低版本的没有对应的 API，所以只能使用字符串字面量的形式查询。
     * （虽然值是一模一样的，但还是分开写好点。）
     *
     * @param resolver ContentResolver
     * @return Cursor
     */
    private static Cursor compatibilityUri(ContentResolver resolver) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return resolver.query(
                    Telephony.Sms.Inbox.CONTENT_URI,
                    new String[]{
                            Telephony.Sms.Inbox._ID,
                            Telephony.Sms.Inbox.PERSON,
                            Telephony.Sms.Inbox.ADDRESS,
                            Telephony.Sms.Inbox.BODY,
                            Telephony.Sms.Inbox.DATE,
                    },
                    null, null, Telephony.Sms.DEFAULT_SORT_ORDER);
        } else {
            return resolver.query(
                    Uri.parse("content://sms/inbox"),
                    new String[]{
                            "_id", "person", "address", "body", "date"
                    },
                    null, null, "date DESC");
        }
    }

    /**
     * 联系人
     *
     * @param context Context
     * @return Contact
     */
    public static Cursor getContact(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        if (cursor != null) {
            return cursor;
        }
        return null;
    }

    /**
     * 提取出单纯的手机号码（去掉号码中的其他字符，比如："+86"、'-'、空格 等等）。
     *
     * @param original 原始号码
     * @return 纯号码
     */
    public static String getPureNumber(String original) {
        if (original.startsWith("+86")) { // 去掉开头的国际区号
            original = original.substring(3);
        }
        int len = original.length();
        StringBuilder sb = new StringBuilder(len);
        char c;
        for (int i = 0; i < len; i++) {
            c = original.charAt(i);
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
