package com.designers.kuwo.biz.bizimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.designers.kuwo.biz.RecentBiz;
import com.designers.kuwo.dao.RecentDao;
import com.designers.kuwo.dao.daoimpl.RecentDaoImpl;
import com.designers.kuwo.eneity.Recent;
import com.designers.kuwo.sqlite.SQLiteDatabaseManager;
import com.designers.kuwo.sqlite.TransactionManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/24.
 */
public class RecentBizImpl implements RecentBiz {

    private RecentDao recentDao;

    public RecentBizImpl() {
        this.recentDao = new RecentDaoImpl();
    }

    @Override
    public void insert(Context context, Recent recent) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByWrite(context);
        //开启事务
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);

        try {
            this.recentDao.insert(sqLiteDatabase, recent);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            transactionManager.commitTransaction(sqLiteDatabase);
            transactionManager.endTransaction(sqLiteDatabase);
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public List<Map<String, Object>> findAll(Context context) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);

        try {
            return this.recentDao.select(sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public void update(Context context, Recent recent) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByWrite(context);
        //开启事务
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);

        try {
            this.recentDao.update(sqLiteDatabase, recent);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            transactionManager.commitTransaction(sqLiteDatabase);
            transactionManager.endTransaction(sqLiteDatabase);
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public boolean songExist(Context context, String songName) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);

        try {
            return this.recentDao.selectByName(sqLiteDatabase, songName);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public void deleteByName(Context context, String songName) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByWrite(context);
        //开启事务
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);

        try {
            this.recentDao.delete(sqLiteDatabase, songName);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            transactionManager.commitTransaction(sqLiteDatabase);
            transactionManager.endTransaction(sqLiteDatabase);
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public void deleteAll(Context context) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByWrite(context);
        //开启事务
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);

        try {
            this.recentDao.deleteAll(sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            transactionManager.commitTransaction(sqLiteDatabase);
            transactionManager.endTransaction(sqLiteDatabase);
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }
}
