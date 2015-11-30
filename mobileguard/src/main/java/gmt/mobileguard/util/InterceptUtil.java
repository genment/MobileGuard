package gmt.mobileguard.util;

import android.content.Context;
import android.util.Log;

import gmt.mobileguard.storage.db.dao.BlacklistDao;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.util
 * Created by Genment at 2015/11/28 20:42.
 */
public class InterceptUtil {
    public static boolean checkIntercept(Context context, String[] ab, String type) {
        ab[0] = checkNumber(ab[0]);
        BlacklistDao blacklistDao = new BlacklistDao(context);
        boolean isBlack = blacklistDao.isBlack(ab[0], type);
        if (isBlack) {
            if ("sms".equals(type)) {
                blacklistDao.saveSms(ab);
                Log.i("Intercept", "number:" + ab[0] + "  message:" + ab[1]);
            } else if ("call".equals(type)) {
                blacklistDao.saveCall(ab[0]);
            }
        }
        blacklistDao.close();
        return isBlack;
    }

    private static String checkNumber(String number) {
        if (number.startsWith("+86"))
            return number.substring(3);
        return number;
    }
}
