package com.designers.kuwo.sqlite;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLManager {
	
	private static final String TAG = "SQLManager";

	// 实现写入模式
	public boolean execWrite(final SQLiteDatabase sqLiteDatabase,
			final String sql, final String... bindArgs) {
		try {
			sqLiteDatabase.execSQL(sql, bindArgs);
			Log.i(TAG, "执行sql:"+sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 实现读取模式
	public Cursor execRead(final SQLiteDatabase sqLiteDatabase,
			final String sql, final String... selectionArgs) {
		try {
			Cursor cursor = sqLiteDatabase.rawQuery(sql, selectionArgs);
			Log.i(TAG, "执行sql:"+sql);
			return cursor;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
