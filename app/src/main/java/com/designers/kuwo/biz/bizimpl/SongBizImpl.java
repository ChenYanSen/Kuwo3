package com.designers.kuwo.biz.bizimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.designers.kuwo.biz.SongBiz;
import com.designers.kuwo.dao.SongDao;
import com.designers.kuwo.dao.daoimpl.SongDaoImpl;
import com.designers.kuwo.eneity.Song;
import com.designers.kuwo.sqlite.SQLiteDatabaseManager;
import com.designers.kuwo.sqlite.TransactionManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/20.
 */
public class SongBizImpl implements SongBiz {

    private SongDao songDao = null;

    public SongBizImpl() {
        this.songDao = new SongDaoImpl();
    }

    @Override
    public void insert(Context context, Song song) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByWrite(context);
        //开启事务
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);

        try {
            this.songDao.insert(sqLiteDatabase, song);
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
            return this.songDao.select(sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public List<Map<String, Object>> findFolder(Context context) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);
        try {
            return this.songDao.selectFolder(sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public List<Map<String, Integer>> findSongNum(Context context) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);
        try {
            return this.songDao.selectSongNum(sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }


    @Override
    public List<Song> findSongByFolder(Context context, String folder) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);
        try {
            return this.songDao.selectSongByFolder(sqLiteDatabase, folder);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public List<Map<String, Object>> findByName(Context context, String songListName, String userName) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);

        try {
            return this.songDao.selectByName(sqLiteDatabase, songListName, userName);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public void updateRank(Context context, String songName, String singer, int rank) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByWrite(context);
        //开启事务
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);

        try {
            this.songDao.updateRank(sqLiteDatabase, songName, singer, rank);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            transactionManager.commitTransaction(sqLiteDatabase);
            transactionManager.endTransaction(sqLiteDatabase);
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public int selectRank(Context context, String songName, String singer) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);

        try {
            return this.songDao.selectRank(sqLiteDatabase, songName, singer);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public List<Map<String, Object>> selectByRank(Context context) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);

        try {
            return this.songDao.selectByRank(sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public void removeSong(Context context, String songName, String singer) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByWrite(context);
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);
        try {
            this.songDao.deleteSong(sqLiteDatabase, songName, singer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transactionManager.commitTransaction(sqLiteDatabase);
            transactionManager.endTransaction(sqLiteDatabase);
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public List<Map<String, Object>> similarSong(Context context, String str) {
        SQLiteDatabaseManager sqLiteDatabaseManager=new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase=sqLiteDatabaseManager.getDatabaseByRead(context);
        try{
            return this.songDao.fuzzySelectSong(sqLiteDatabase,str);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public int songNum(Context context) {
        SQLiteDatabaseManager sqLiteDatabaseManager = new SQLiteDatabaseManager();
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);
        try {
            return this.songDao.selectAllSongNum(sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }


}
