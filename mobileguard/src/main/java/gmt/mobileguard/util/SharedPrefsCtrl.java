package gmt.mobileguard.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

import gmt.mobileguard.BuildConfig;

/**
 * Project: StudyDemo
 * Package: gmt.mobileguard.util
 * Created by Genment at 2015/11/7 14:46.
 */
public class SharedPrefsCtrl {
    private static Context mContext;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;

    /**
     * 私有构造方法，防止实例化
     */
    private SharedPrefsCtrl() {
    }

    public static void init(Context context) {
        if (context == null) {
            throw new NullPointerException("context is null.");
        }
        if (mContext != null || mSharedPreferences != null || mEditor != null) {
            return;
        }
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(Constant.FILE_NALE, Constant.MODE);
        mEditor = mSharedPreferences.edit();
    }

    private static void check() {
        if (mContext == null || mSharedPreferences == null) {
            throw new NullPointerException("必须先调用init()来初始化。");
        }
    }

    // put

    public static void putBoolean(String key, boolean value) {
        check();
        mEditor.putBoolean(key, value).apply();
    }

    public static void putFloat(String key, float value) {
        check();
        mEditor.putFloat(key, value).apply();
    }

    public static void putInt(String key, int value) {
        check();
        mEditor.putInt(key, value).apply();
    }

    public static void putLong(String key, long value) {
        check();
        mEditor.putLong(key, value).apply();
    }

    public static void putString(String key, String value) {
        check();
        mEditor.putString(key, value).apply();
    }

    public static void putStringSet(String key, Set<String> values) {
        check();
        mEditor.putStringSet(key, values).apply();
    }

    /**
     * 一次性地设置一系列属性
     *
     * @param map <code>key</code> String 类型的 key
     *            <code>value</code> 就是 value，可以是任意 SharedPreferences 支持的类型。
     */
    public static void putMap(Map<String, ?> map) {
        if (map == null || map.size() < 1)
            return;
        check();
        Set<String> keys = map.keySet();
        Object value;
        for (String key : keys) {
            value = map.get(key);
            if (value == null || value instanceof String) {
                mEditor.putString(key, (String) value);
            } else if (value instanceof Boolean) {
                mEditor.putBoolean(key, (boolean) value);
            } else if (value instanceof Integer) {
                mEditor.putInt(key, (int) value);
            } else if (value instanceof Float) {
                mEditor.putFloat(key, (float) value);
            } else if (value instanceof Double) {
                mEditor.putFloat(key, ((Double) value).floatValue());
            } else if (value instanceof Long) {
                mEditor.putLong(key, (long) value);
            } else if (value instanceof Set) {
                //noinspection unchecked
                mEditor.putStringSet(key, (Set<String>) value);
            } else {
                mEditor.putString(key, value.toString());
            }
        }
        mEditor.apply();
    }

    // get

    public static boolean getBoolean(String key, boolean defValue) {
        check();
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public static float getFloat(String key, float defValue) {
        check();
        return mSharedPreferences.getFloat(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        check();
        return mSharedPreferences.getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        check();
        return mSharedPreferences.getLong(key, defValue);
    }

    public static String getString(String key, String defValue) {
        check();
        return mSharedPreferences.getString(key, defValue);
    }

    public static Set<String> getStringSet(String key, Set<String> defValues) {
        check();
        return mSharedPreferences.getStringSet(key, defValues);
    }

    public static Map<String, ?> getAll() {
        check();
        return mSharedPreferences.getAll();
    }

    // check

    public static boolean contains(String key) {
        check();
        return mSharedPreferences.contains(key);
    }

    // delete

    public static void remove(String key) {
        check();
        mEditor.remove(key).apply();
    }

    public static void clear() {
        check();
        mEditor.clear().apply();
    }

    /**
     * 常量类
     */
    public static class Constant {
        /**
         * SharedPreference 文件名
         */
        public static final String FILE_NALE = BuildConfig.APPLICATION_ID;
        /**
         * SharedPreference 读写模式 - MODE_PRIVATE
         */
        public static final int MODE = Context.MODE_PRIVATE;

        /**
         * 密码
         */
        public static final String SJFD_PWD = "11";
        /**
         * 是否已配置
         */
        public static final String SJFD_CONFIG = "12";
        /**
         * 绑定SIM卡状态
         */
        public static final String SJFD_BIND_SIM = "13";
        /**
         * SIM卡（手机号）
         */
        public static final String SJFD_SIM = "14";
        /**
         * 安全号码（手机号）
         */
        public static final String SJFD_SECURITY_PHONE = "15";
        /**
         * 手机防盗状态
         */
        public static final String SJFD_SECURITY_STATUS = "16";
    }
}
