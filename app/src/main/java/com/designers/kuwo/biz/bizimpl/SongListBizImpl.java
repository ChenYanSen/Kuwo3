package com.designers.kuwo.biz.bizimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.designers.kuwo.biz.SongListBiz;
import com.designers.kuwo.dao.SongListDao;
import com.designers.kuwo.dao.daoimpl.SongListDaoImpl;
import com.designers.kuwo.eneity.SongList;
import com.designers.kuwo.sqlite.SQLiteDatabaseManager;
import com.designers.kuwo.sqlite.TransactionManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/21.
 */
public class SongListBizImpl implements SongListBiz {

    private SongListDao songListDao;

    public SongListBizImpl() {
        this.songListDao = new SongListDaoImpl();
    }

    @Override
    public void insert(Context context, SongList songList) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByWrite(context);
        //开启事务
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);

        try {
            this.songListDao.insert(sqLiteDatabase, songList);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            transactionManager.commitTransaction(sqLiteDatabase);
            transactionManager.endTransaction(sqLiteDatabase);
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public List<Map<String, Object>> findNameAndSize(Context context, final String userName) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);

        try {
            return this.songListDao.selectNameAndSize(sqLiteDatabase, userName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public void update(Context context, String oldName, String newName, String userName) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByWrite(context);
        //开启事务
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);

        try {
            this.songListDao.update(sqLiteDatabase, oldName, newName, userName);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            transactionManager.commitTransaction(sqLiteDatabase);
            transactionManager.endTransaction(sqLiteDatabase);
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public void delete(Context context, String songListName, String userName) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByWrite(context);
        //开启事务
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);

        try {
            this.songListDao.delete(sqLiteDatabase, songListName, userName);
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
            return this.songListDao.select(sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public void deleteSong(Context context, String songListName, String songName,
                           String singer, String userName) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByWrite(context);
        //开启事务
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);

        try {
            this.songListDao.deleteSong(sqLiteDatabase, songListName, songName, singer, userName);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            transactionManager.commitTransaction(sqLiteDatabase);
            transactionManager.endTransaction(sqLiteDatabase);
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public boolean selectByName(Context context, String songName, String singer, String songListName, String userName) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);

        try {
            return this.songListDao.selectByName(sqLiteDatabase, songName, singer, songListName, userName);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }
}
