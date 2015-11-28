package gmt.mobileguard.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.List;

import gmt.mobileguard.fragment.BaseNumberPickerFragment.Data;

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
    public static List<Data> getDataFromCallLog(Context context) {
        List<Data> datas = new ArrayList<>();
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
            Data data;
            while (cursor.moveToNext()) {
                data = new Data();
                data.id = cursor.getLong(0);
                data.name = cursor.getString(1);
                data.number = cursor.getString(2);
                data.lastTimeStamp = cursor.getLong(3);
                datas.add(data);
            }
            cursor.close();
        }
        return datas;
    }

    /**
     * 短信
     *
     * @param context Context
     * @return SMS
     */
    public static List<Data> getDataFromSms(Context context) {
        List<Data> datas = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = compatiblityUri(resolver);
        if (cursor != null) {
            Data data;
            while (cursor.moveToNext()) {
                data = new Data();
                data.id = cursor.getLong(0);
                data.number = cursor.getString(1);
                data.message = cursor.getString(2);
                data.lastTimeStamp = cursor.getLong(3);
                datas.add(data);
            }
            cursor.close();
        }
        return datas;
    }

    /**
     * 由于在 android 4.4 开始，Google 开放了 Telephony 的 API，所以可以直接使用 API 提供的常量进行查询；
     * 而低版本的没有对应的 API，所以只能使用字符串字面量的形式查询。
     * （虽然值是一模一样的，但还是分开写好点。）
     *
     * @param resolver ContentResolver
     * @return Cursor
     */
    private static Cursor compatiblityUri(ContentResolver resolver) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return resolver.query(
                    Telephony.Sms.Inbox.CONTENT_URI,
                    new String[]{
                            Telephony.Sms.Inbox._ID,
                            Telephony.Sms.Inbox.ADDRESS,
                            Telephony.Sms.Inbox.BODY,
                            Telephony.Sms.Inbox.DATE,
                    },
                    null, null, Telephony.Sms.DEFAULT_SORT_ORDER);
        } else {
            return resolver.query(
                    Uri.parse("content://sms/inbox"),
                    new String[]{
                            "_id", "address", "body", "date"
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
    public static List<Data> getDataFromContact(Context context) {
        List<Data> datas = new ArrayList<>();
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
            Data data;
            while (cursor.moveToNext()) {
                data = new Data();
                data.id = cursor.getLong(0);
                data.name = cursor.getString(1);
                data.number = cursor.getString(2);
                datas.add(data);
            }
            cursor.close();
        }
        return datas;
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
        StringBuilder sb = new StringBuilder(original);
        char c;
        for (int i = 0; i < sb.length(); i++) {
            c = sb.charAt(i);
            if (!Character.isDigit(c)) {
                sb.deleteCharAt(i);
                --i;
            }
        }
        return sb.toString();
    }
}
