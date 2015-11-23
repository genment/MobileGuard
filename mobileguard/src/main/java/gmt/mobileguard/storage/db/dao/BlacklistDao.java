package gmt.mobileguard.storage.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import gmt.mobileguard.storage.db.BlacklistDbOpenHelper;
import gmt.mobileguard.storage.db.entity.BlackEntity;

/**
 * Project: MobileGuard
 * Package: gmt.mobileguard.storage.db.dao
 * Created by Genment at 2015/11/21 18:20.
 */
public class BlacklistDao {
    private BlacklistDbOpenHelper mHelper;

    public BlacklistDao(Context context) {
        mHelper = new BlacklistDbOpenHelper(context);
    }

    /**
     * 关闭数据库资源
     */
    public void close() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }

    /**
     * 添加
     */
    public void addBlack(BlackEntity blackEntity) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("INSERT INTO blacklist(number, mode, name, count, attribution) VALUES( ?, ?, ?, 0, ?)",
                new Object[]{
                        blackEntity.getNumber(),
                        blackEntity.getMode(),
                        blackEntity.getName(),
                        blackEntity.getAttribution()
                });
        db.close();
    }

    /**
     * 更新
     */
    public void updateBlack(BlackEntity blackEntity) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("UPDATE blacklist SET number=?, mode=?, name=?, attribution=? WHERE _id = ?",
                new Object[]{
                        blackEntity.getNumber(),
                        blackEntity.getMode(),
                        blackEntity.getName(),
                        blackEntity.getAttribution(),
                        blackEntity.getId()
                });
        db.close();
    }

    /**
     * 删除
     */
    public void removeBlack(BlackEntity blackEntity) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("DELETE FROM blacklist WHERE _id = ?",
                new Integer[]{
                        blackEntity.getId()
                });
        db.close();
    }

    /**
     * 清空
     */
    public void clearAllBlack() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("DELETE FROM blacklist");
        db.close();
    }

    /**
     * 更新屏蔽次数
     */
    public void updateCount(int id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("UPDATE blacklist SET count=count+1 WHERE _id = ?", new Integer[]{id});
        db.close();
    }

    /**
     * 更改模式
     */
    public void changeMode(BlackEntity blackEntity) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("UPDATE blacklist SET mode=? WHERE _id = ?", new Integer[]{blackEntity.getMode(), blackEntity.getId()});
        db.close();
    }

    /**
     * 获取全部黑名单
     */
    public List<BlackEntity> getAll() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM blacklist order by _id DESC", null);
        List<BlackEntity> datas = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            BlackEntity blackEntity;
            while (cursor.moveToNext()) {
                blackEntity = new BlackEntity();
                blackEntity.setId(cursor.getInt(0));
                blackEntity.setNumber(cursor.getString(1));
                blackEntity.setMode(cursor.getInt(2));
                blackEntity.setName(cursor.getString(3));
                blackEntity.setCount(cursor.getInt(4));
                blackEntity.setAttribution(cursor.getString(5));
                datas.add(blackEntity);
            }
            cursor.close();
        }
        db.close();
        return datas;
    }

    /**
     * 是否黑名单
     */
    public boolean isBlack(String number) {
        boolean isBlack = false;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM blacklist WHERE number=?", new String[]{number});
        if (cursor != null && cursor.moveToFirst()) {
            isBlack = true;
            int id = cursor.getInt(0);
            updateCount(id);
            cursor.close();
        }
        db.close();
        return isBlack;
    }
}
