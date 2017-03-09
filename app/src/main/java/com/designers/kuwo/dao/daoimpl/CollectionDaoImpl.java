package com.designers.kuwo.dao.daoimpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.designers.kuwo.dao.CollectionDao;
import com.designers.kuwo.eneity.ICollection;
import com.designers.kuwo.sqlite.SQLManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CollectionDaoImpl implements CollectionDao {

    private SQLManager sqlManager;

    public CollectionDaoImpl() {
        this.sqlManager = new SQLManager();
    }

    /**
     * 添加收藏
     *
     * @param iCollection
     * @param sqLiteDatabase
     * @return
     * @throws SQLException
     */
    @Override
    public void insertCollection(ICollection iCollection, SQLiteDatabase sqLiteDatabase) throws SQLException {

        String songName = iCollection.getSongName();
        String singer = iCollection.getSinger();
        String account = iCollection.getAccount();
        String songUri = iCollection.getSongUri();
        byte[] songImage = iCollection.getSongImage();
        String singlyrics = iCollection.getSinglyrics();
        String time = iCollection.getTime();
        String information = iCollection.getInformation();
        String folder = iCollection.getFolder();
        String rank = iCollection.getRank();

        ContentValues values = new ContentValues();
        values.put("songName", songName);
        values.put("singer", singer);
        values.put("songUri", songUri);
        values.put("songImage", songImage);
        values.put("singLyrics", singlyrics);
        values.put("information", information);
        values.put("time", time);
        values.put("rank", rank);
        values.put("folder", folder);
        values.put("account", account);
        long collection = sqLiteDatabase.insert("collection", null, values);
        Log.i("数据库操作执行添加收藏。。。。。", "insert=========:" + collection);
    }

    /**
     * 根据用户信息及歌曲，歌手删除收藏
     *
     * @param song
     * @param singer
     * @param account
     * @param sqLiteDatabase
     * @return
     * @throws SQLException
     */
    @Override
    public void deleteCollection(String song, String singer, String account, SQLiteDatabase sqLiteDatabase) throws SQLException {
        String sql = "delete from collection where songName=? and singer=? and account= ?";
        String[] bindArgs = new String[]{song, singer, account};
        sqlManager.execWrite(sqLiteDatabase, sql, bindArgs);
    }

    /**
     * 判断是否收藏
     *
     * @param song
     * @param singer
     * @param account
     * @param sqLiteDatabase
     * @return
     * @throws SQLException
     */
    @Override
    public boolean selectCollection(String song, String singer, String account, SQLiteDatabase sqLiteDatabase) throws SQLException {
        boolean flag = false;
        String sql = "select * from collection where songName=? and singer=? and account=?";
        String[] bindArgs = new String[]{song, singer, account};
        Cursor cursor = sqlManager.execRead(sqLiteDatabase, sql, bindArgs);
        if (cursor.moveToNext()) {
            flag = true;
        } else {
            flag = false;
        }
        Log.i("数据库操作：", "dao层执行了判断是否收藏的操作     selectCollection     。。。。flag  =  " + flag);
        return flag;
    }

    @Override
    public List<ICollection> selectCollectionAllSongs(SQLiteDatabase sqLiteDatabase) throws SQLException {
        List<ICollection> collectionAllList = new ArrayList<ICollection>();
        String sql = "select songName,singer,songUri,songImage,singLyrics,time,information,folder,rank from collection";
        Log.i("查询收藏列表中的歌曲。。。。。", "收藏:sql" + sql);
        String[] selectionArgs = new String[]{};
        Cursor cursor = sqlManager.execRead(sqLiteDatabase, sql, selectionArgs);
        while (cursor.moveToNext()) {
            ICollection collection = new ICollection();
            collection.setSongName(cursor.getString(0));
            collection.setSinger(cursor.getString(1));
            collection.setSongUri(cursor.getString(2));
            collection.setSongImage(cursor.getBlob(3));
            collection.setSinglyrics(cursor.getString(4));
            collection.setTime(cursor.getString(5));
            collection.setInformation(cursor.getString(6));
            collection.setFolder(cursor.getString(7));
            collection.setRank(cursor.getString(8));
            collectionAllList.add(collection);
        }
        Log.i("查询收藏列表中的歌曲。。。。", collectionAllList.toString());
        return collectionAllList;
    }

    @Override
    public List<Map<String, Object>> selectAllCollectionSong(SQLiteDatabase sqLiteDatabase) throws SQLException {
        List<Map<String, Object>> collectionAllSong = new ArrayList<Map<String, Object>>();
        String sql = "select songName,singer,songUri,songImage,singLyrics,time,information,folder,rank from collection";
        Log.i("查询收藏列表中的歌曲。。。。。", "收藏:sql" + sql);
        String[] selectionArgs = new String[]{};
        Cursor cursor = sqlManager.execRead(sqLiteDatabase, sql, selectionArgs);
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("songName", cursor.getString(0));
            map.put("singer", cursor.getString(1));
            map.put("songUri", cursor.getString(2));
            map.put("songImage", cursor.getBlob(3));
            map.put("singLyrics", cursor.getString(4));
            map.put("time", cursor.getString(5));
            map.put("information", cursor.getString(6));
            map.put("folder", cursor.getString(7));
            map.put("rank", cursor.getString(8));
            map.put("expend", false);
            map.put("checked", false);
            collectionAllSong.add(map);
        }
        return collectionAllSong;
    }

}
