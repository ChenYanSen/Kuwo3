package com.designers.kuwo.biz.bizimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.designers.kuwo.biz.CollectionBiz;
import com.designers.kuwo.dao.CollectionDao;
import com.designers.kuwo.dao.daoimpl.CollectionDaoImpl;
import com.designers.kuwo.eneity.ICollection;
import com.designers.kuwo.sqlite.SQLiteDatabaseManager;
import com.designers.kuwo.sqlite.TransactionManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by 跃 on 2017/2/22.
 */
public class CollectionBizImp implements CollectionBiz {

    private SQLiteDatabaseManager sqLiteDatabaseManager;
    private CollectionDao collectionDao;

    public CollectionBizImp() {
        this.sqLiteDatabaseManager = new SQLiteDatabaseManager();
        this.collectionDao = new CollectionDaoImpl();
    }
    /**
     * 添加收藏
     *
     * @param iCollection
     * @param context
     * @return
     */
    @Override
    public void alterCollection(ICollection iCollection, Context context) {

        //获取数据库连接
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);
        //开启事务
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);
        try {
           this.collectionDao.insertCollection(iCollection, sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            transactionManager.commitTransaction(sqLiteDatabase);
            transactionManager.endTransaction(sqLiteDatabase);
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }
    /**
     * 删除收藏
     *
     * @param song
     * @param singer
     * @param account
     * @param context
     * @return
     */
    @Override
    public void removeCollection(String song, String singer, String account, Context context) {

        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);
        try {
            this.collectionDao.deleteCollection(song, singer, account, sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            transactionManager.commitTransaction(sqLiteDatabase);
            transactionManager.endTransaction(sqLiteDatabase);
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    /**
     * 查询是否收藏
     *
     * @param song
     * @param singer
     * @param account
     * @param context
     * @return
     */
    @Override
    public boolean findCollection(String song, String singer, String account, Context context) {
        boolean flag=false;
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);
        try {
            flag = collectionDao.selectCollection(song, singer, account, sqLiteDatabase);
            Log.i("数据库操作：", "biz层执行了查询是否收藏的操作。。。。flag=" + flag);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
        return flag;
    }

    @Override
    public List<ICollection> findCollectionAllSongs(Context context) {
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);
        try {
            Log.i("数据库操作：", "biz层执行了查询收藏所有歌曲的操作    selectCollectionAllSongs");
            return this.collectionDao.selectCollectionAllSongs(sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

    /**
     * 查询收藏的歌曲
     * @param context
     * @return
     */
    @Override
    public List<Map<String, Object>> findAllCollectionsong(Context context) {
        SQLiteDatabase sqLiteDatabase = sqLiteDatabaseManager.getDatabaseByRead(context);
        try {
            Log.i("数据库操作：", "biz层执行了查询收藏所有歌曲的操作 返回listMap结构    selectAllCollectionSong");
            return this.collectionDao.selectAllCollectionSong(sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
    }

}
