package gmt.mobileguard.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static gmt.mobileguard.BuildConfig.VERSION_CODE;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.storage.db
 * Created by Genment at 2015/11/20 18:26.
 */
public class BlacklistDbOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "blacklist.db";

    public BlacklistDbOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // blacklist table
        db.execSQL("CREATE TABLE blacklist (_id INTEGER PRIMARY KEY AUTOINCREMENT, number TEXT, mode INT, description TEXT, count INT, attribution TEXT)");
        // sms table
        db.execSQL("CREATE TABLE sms (_id INTEGER PRIMARY KEY AUTOINCREMENT, number TEXT, message TEXT, timestamp INTEGER)");
        // call table
        db.execSQL("CREATE TABLE call (_id INTEGER PRIMARY KEY AUTOINCREMENT, number TEXT, timestamp INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
