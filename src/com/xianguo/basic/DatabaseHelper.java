package com.xianguo.basic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xianguo.constant.XianguoConstant;

public class DatabaseHelper extends SQLiteOpenHelper {

	/** db的名字 */
	private static final String DB_NAME = "xianguo.db";

	/** 数据库版本 */
	private static final int DB_VERSION = 1;

	private static final String TABLE_NAME = "t_favorite";

	private static final String C_ID = "id";

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + C_ID
				+ " VARCHAR not null);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	/**
	 * 收藏一个id
	 * 
	 * @param phoneId
	 * @return
	 */
	public void saveFavorite(String phoneId) {
		SQLiteDatabase db = null;
		String sql = "insert into " + TABLE_NAME + " (" + C_ID + ") values('"
				+ phoneId + "');";
		try {
			db = this.getWritableDatabase();
			db.execSQL(sql);
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	/**
	 * 删除其中的一条数据
	 */
	public void deleteFavorite(String phoneId) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.delete(TABLE_NAME, C_ID + " = '" + phoneId + "'", null);
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	/**
	 * 判断商品是否存在
	 * 
	 * @param phoneId
	 * @return
	 */
	public boolean isExist(String phoneId) {
		boolean result = false;
		SQLiteDatabase db = null;
		try {
			db = this.getReadableDatabase();
			Cursor mCursor = db.query(TABLE_NAME, null, C_ID + "=" + phoneId,
					null, null, null, null, null);
			if (mCursor != null) {
				int count = mCursor.getCount();
				result = count > 0;
			}
		} catch (Exception e) {
			Log.e("xxxx", e.getMessage(), e);
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return result;
	}

	/**
	 * 获取所有的收藏
	 * 
	 * @return
	 */
	public List<String> getAllFavorite() {
		List<String> ids = new ArrayList<String>();
		SQLiteDatabase db = null;
		try {
			db = this.getReadableDatabase();
			Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null,
					null);
			while (cursor.moveToNext()) {
				ids.add(cursor.getString(0));
			}
			cursor.close();
		} catch (Exception e) {
			Log.e(XianguoConstant.LOG_TAG, e.getMessage(), e);
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return ids;
	}

}
