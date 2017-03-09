package com.designers.kuwo.biz.bizimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.designers.kuwo.biz.AlbumBiz;
import com.designers.kuwo.dao.AlbumDao;
import com.designers.kuwo.dao.daoimpl.AlbumDaoImpl;
import com.designers.kuwo.eneity.Album;
import com.designers.kuwo.eneity.Song;
import com.designers.kuwo.sqlite.SQLiteDatabaseManager;
import com.designers.kuwo.sqlite.TransactionManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by PC-CWB on 2017/2/22.
 */
public class AlbumBizImpl implements AlbumBiz {

    private AlbumDao albumDao=null;
    private SQLiteDatabaseManager sqLiteDatabaseManager;

    public AlbumBizImpl(){
        this.albumDao=new AlbumDaoImpl();
        this.sqLiteDatabaseManager=new SQLiteDatabaseManager();
    }
    @Override
    public void insert(Context context, Album album) {
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByWrite(context);
        //开启事务
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);

        try {
            this.albumDao.insert(sqLiteDatabase, album);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            transactionManager.commitTransaction(sqLiteDatabase);
            transactionManager.endTransaction(sqLiteDatabase);
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    @Override
    public List<Album> AlbumFind(Context context) {
        SQLiteDatabase sqLiteDatabase= sqLiteDatabaseManager.getDatabaseByRead(context);
        Log.i("数据库操作：", "获得数据库连接。。。。。");
        try {
            Log.i("数据库操作：","查询Album 。。。。。");
            return albumDao.selectAlbum(sqLiteDatabase);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
            Log.i("数据库操作：","数据库关闭");
        }
    }

    @Override
    public List<Album> songFindByAlbum(Context context, String albums) {
        SQLiteDatabase sqLiteDatabase=sqLiteDatabaseManager.getDatabaseByRead(context);
        Log.i("数据库操作：", "获得数据库连接。。。。。");
        try {
            Log.i("数据库操作：","查询song by album 。。。。。");
            return albumDao.selectSongByAlbum(sqLiteDatabase,albums);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
            Log.i("数据库操作：","数据库关闭");
        }
    }

    @Override
    public List<Song> songAllFindByAlbum(Context context, String album) {
        SQLiteDatabase sqLiteDatabase=sqLiteDatabaseManager.getDatabaseByRead(context);
        Log.i("数据库操作：", "获得数据库连接。。。。。");
        try {
            Log.i("数据库操作：","查询song by album 。。。。。");
            return albumDao.selectSongAllByAlbum(sqLiteDatabase,album);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
            Log.i("数据库操作：","数据库关闭");
        }
    }

    @Override
    public List<Map<String, Object>> songFindAllByAlbums(Context context, String album) {
        SQLiteDatabase sqLiteDatabase=sqLiteDatabaseManager.getDatabaseByRead(context);
        Log.i("数据库操作：", "获得数据库连接。。。。。");
        try{
            Log.i("数据库操作：","查询song by album 。。。。。 返回list Map  结构");
            return this.albumDao.selectAllSongByAlbums(sqLiteDatabase,album);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
            Log.i("数据库操作：","数据库关闭。。。。。。。。。。。");
        }
    }
}
