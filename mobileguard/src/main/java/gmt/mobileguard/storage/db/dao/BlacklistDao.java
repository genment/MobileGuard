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
        db.execSQL("INSERT INTO blacklist(number, mode, description, count, attribution) VALUES( ?, ?, ?, 0, ?)",
                new Object[]{
                        blackEntity.getNumber(),
                        blackEntity.getMode(),
                        blackEntity.getDescription(),
                        blackEntity.getAttribution()
                });
        db.close();
    }

    /**
     * 更新
     */
    public void updateBlack(BlackEntity blackEntity) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("UPDATE blacklist SET number=?, mode=?, description=?, attribution=? WHERE _id = ?",
                new Object[]{
                        blackEntity.getNumber(),
                        blackEntity.getMode(),
                        blackEntity.getDescription(),
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
     * 获取最新的一个黑名单
     */
    public BlackEntity getOne(int id) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM blacklist WHERE _id = ?",
                new String[]{String.valueOf(id)});
        BlackEntity blackEntity = null;
        if (cursor != null && cursor.moveToFirst()) {
            blackEntity = fillBlackEntity(cursor, true);
        }
        db.close();
        return blackEntity;
    }

    /**
     * 获取最新的一个黑名单
     */
    public BlackEntity getLastOne() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM blacklist order by _id DESC limit 1", null);
        BlackEntity blackEntity = null;
        if (cursor != null && cursor.moveToFirst()) {
            blackEntity = fillBlackEntity(cursor, true);
        }
        db.close();
        return blackEntity;
    }

    /**
     * 获取全部黑名单
     */
    public List<BlackEntity> getAll() {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM blacklist order by _id DESC", null);
        List<BlackEntity> datas = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                datas.add(fillBlackEntity(cursor, false));
            }
            cursor.close();
        }
        db.close();
        return datas;
    }

    /**
     * 是否黑名单
     */
    public boolean isBlack(String number, String type) {
        boolean isBlack = false;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, mode FROM blacklist WHERE number=? ", new String[]{number});
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int mode = cursor.getInt(1);
            if ("call".equals(type)) {
                isBlack = (mode & 1) == 1;
            } else if ("sms".equals(type)) {
                isBlack = (mode & 2) == 2;
            }
            if (isBlack) {
                updateCount(id);
            }
            cursor.close();
        }
        db.close();
        return isBlack;
    }

    /**
     * 保存被拦截的短信
     */
    public void saveSms(String[] addressAndBody) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("INSERT INTO sms(number, message, timestamp) VALUES( ?, ?, ?)",
                new Object[]{
                        addressAndBody[0],
                        addressAndBody[1],
                        System.currentTimeMillis()
                });
        db.close();
    }

    /**
     * 保存电话的拦截记录
     */
    public void saveCall(String number) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("INSERT INTO call(number, timestamp) VALUES( ?, ?)",
                new Object[]{
                        number,
                        System.currentTimeMillis()
                });
        db.close();
    }

    private BlackEntity fillBlackEntity(Cursor cursor, boolean closeCursor) {
        BlackEntity entity = new BlackEntity();
        entity.setId(cursor.getInt(0));
        entity.setNumber(cursor.getString(1));
        entity.setMode(cursor.getInt(2));
        entity.setDescription(cursor.getString(3));
        entity.setCount(cursor.getInt(4));
        entity.setAttribution(cursor.getString(5));
        if (closeCursor)
            cursor.close();
        return entity;
    }
}
