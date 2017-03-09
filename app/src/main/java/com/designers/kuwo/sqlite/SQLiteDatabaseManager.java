package com.designers.kuwo.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLiteDatabaseManager {
	
	private static final String  TAG = "SQLiteDatabaseManager";

	// 用于获取一个写入模式的连接对象
	public SQLiteDatabase getDatabaseByWrite(final Context context) {
		SqLiteDatabaseHelper sqLiteDatabaseHelper = new SqLiteDatabaseHelper(
				context);
		SQLiteDatabase sqLiteDatabase = sqLiteDatabaseHelper
				.getWritableDatabase();
		Log.i(TAG, "获取写入连接成功");
		return sqLiteDatabase;
	}

	// 获取一个读取模式的连接对象
	public SQLiteDatabase getDatabaseByRead(final Context context) {
		SqLiteDatabaseHelper sqLiteDatabaseHelper = new SqLiteDatabaseHelper(
				context);
		SQLiteDatabase sqLiteDatabase = sqLiteDatabaseHelper
				.getReadableDatabase();
		Log.i(TAG, "获取读取连接成功");
		return sqLiteDatabase;
	}

	// 关闭连接
	public void closeSQLiteDatabase(final SQLiteDatabase sqLiteDatabase) {
		if (sqLiteDatabase != null) {
			sqLiteDatabase.close();
			Log.i(TAG, "连接关闭");
		}
	}

}
