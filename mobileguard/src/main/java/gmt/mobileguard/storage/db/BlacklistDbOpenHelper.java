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

//    private static BlacklistDbOpenHelper instance;

    public BlacklistDbOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // blacklist table
        db.execSQL("CREATE TABLE blacklist (_id integer primary key autoincrement, number varchar(20), mode int, description varchar(20), count int, attribution varchar(20) );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

/*
    public static BlacklistDbOpenHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (BlacklistDbOpenHelper.class) {
                if (instance == null)
                    instance = new BlacklistDbOpenHelper(context);
            }
        }
        return instance;
    }
*/
}
