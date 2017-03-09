package com.designers.kuwo.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TransactionManager {
	
	private static final String TAG = "TransactionManager";

	// 开启一个事务
	public void beginTransaction(final SQLiteDatabase sqLiteDatabase) {
		if (sqLiteDatabase != null) {
			sqLiteDatabase.beginTransaction();
			Log.i(TAG, "开启一个事务");
		}
	}

	// 提交一个事务
	public void commitTransaction(final SQLiteDatabase sqLiteDatabase) {
		if (sqLiteDatabase != null) {
			sqLiteDatabase.setTransactionSuccessful();
			Log.i(TAG, "提交一个事务");
		}
	}

	// 结束一个事务
	public void endTransaction(final SQLiteDatabase sqLiteDatabase) {
		if (sqLiteDatabase != null) {
			sqLiteDatabase.endTransaction();
			Log.i(TAG, "结束一个事务");
		}
	}
}
