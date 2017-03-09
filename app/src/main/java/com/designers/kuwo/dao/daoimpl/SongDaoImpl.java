package com.designers.kuwo.dao.daoimpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.designers.kuwo.dao.SongDao;
import com.designers.kuwo.eneity.Song;
import com.designers.kuwo.sqlite.SQLManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/20.
 */
public class SongDaoImpl implements SongDao {

    private SQLManager sqlManager;

    public SongDaoImpl() {
        this.sqlManager = new SQLManager();
    }

    /**
     * 插入歌曲信息
     *
     * @param sqLiteDatabase
     * @param song
     */
    @Override
    public void insert(SQLiteDatabase sqLiteDatabase, Song song) throws SQLException {
        String songName = song.getSongName();
        String singer = song.getSinger();
        String songUri = song.getSongUri();
        byte[] songImage = song.getSongImage();
        String singLyrics = song.getSingLyrics();
        String information = song.getInformation();
        String time = song.getTime();
        byte [] singerUri=song.getSingerUri();
        String folder=song.getFolder();
        String rank = song.getRank();

        ContentValues values = new ContentValues();
        values.put("songName", songName);
        values.put("singer", singer);
        values.put("songUri", songUri);
        values.put("songImage", songImage);
        values.put("singerUri",singerUri);
        values.put("singLyrics", singLyrics);
        values.put("information", information);
        values.put("time", time);
        values.put("rank", rank);
        values.put("folder",folder);
        sqLiteDatabase.insert("songs", null, values);
    }

    /**
     * 查询所有歌曲信息
     *
     * @param sqLiteDatabase
     * @return
     * @throws SQLException
     */
    @Override
    public List<Map<String, Object>> select(SQLiteDatabase sqLiteDatabase) throws SQLException {
        List<Map<String, Object>> songList = new ArrayList<Map<String, Object>>();
        String sql = "select songName, singer, songUri, songImage, singlyrics, information, time, rank from songs";
        String[] selectionArgs = new String[]{};
        Cursor cursor = this.sqlManager.execRead(sqLiteDatabase, sql, selectionArgs);
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("songName", cursor.getString(0));
            map.put("singer", cursor.getString(1));
            map.put("songUri", cursor.getString(2));
            map.put("songImage", cursor.getBlob(3));
            map.put("singLyrics", cursor.getString(4));
            map.put("information", cursor.getString(5));
            map.put("time", cursor.getString(6));
            map.put("rank", cursor.getString(7));
            map.put("expend", false);
            map.put("checked", false);
            songList.add(map);
        }
        return songList;
    }

    @Override
    public List<Map<String,Object>> selectFolder(SQLiteDatabase sqLiteDatabase) throws SQLException {
        List<Map<String,Object>> folderlist=new ArrayList<Map<String, Object>>();
        String sql="select distinct folder from songs";
        String [] selectionArgs=new String[]{};
        Cursor cursor=this.sqlManager.execRead(sqLiteDatabase,sql,selectionArgs);
        Log.i("数据库操作：","查询音乐文件夹  folder " + sql);
        while (cursor.moveToNext()){
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("folder",cursor.getString(0));
            folderlist.add(map);
        }
        return folderlist;
    }

    @Override
    public List<Map<String,Integer>> selectSongNum(SQLiteDatabase sqLiteDatabase) throws SQLException {
        List<Map<String,Integer>> songnumList=new ArrayList<Map<String,Integer>>();
        String sql="select folder,count(1) from songs group by folder";
        String [] selectionArgs=new String[]{};
        Cursor cursor=this.sqlManager.execRead(sqLiteDatabase,sql,selectionArgs);
        while (cursor.moveToNext()){
            Map<String,Integer> map=new HashMap<String,Integer>();
            map.put("songNum",cursor.getInt(1));
            Log.i("SongImplDao","songNum"+cursor.getInt(1));
            songnumList.add(map);
        }
        return songnumList;
    }

    @Override
    public List<Song> selectSongByFolder(SQLiteDatabase sqLiteDatabase,String folder) throws SQLException {
        List<Song> songByFolderList=new ArrayList<Song>();
        String sql="select songName, singer, songUri, songImage, singlyrics, information, time from songs where folder=? ";
        String selectionArgs[]=new String[]{folder};
        Cursor cursor=sqlManager.execRead(sqLiteDatabase,sql,selectionArgs);
        while (cursor.moveToNext()){
            Song song=new Song();
            song.setSongName(cursor.getString(0));
            song.setSinger(cursor.getString(1));
            song.setSongUri(cursor.getString(2));
            song.setSongImage(cursor.getBlob(3));
            song.setSingLyrics(cursor.getString(4));
            song.setInformation(cursor.getString(5));
            song.setTime(cursor.getString(6));
            songByFolderList.add(song);
        }
        return songByFolderList;
    }

    @Override
    public List<Map<String, Object>> selectByName(SQLiteDatabase sqLiteDatabase, String songListName, String userName) throws SQLException {
        List<Map<String, Object>> songList = new ArrayList<Map<String, Object>>();
        String sql = "select songName, singer, songUri, songImage, singlyrics, information, time, rank from songs where songName in " +
                "(select songName from songList where songListName = ? and account = ?)";
        String[] selectionArgs = new String[]{songListName, userName};
        Cursor cursor = this.sqlManager.execRead(sqLiteDatabase, sql, selectionArgs);
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("songName", cursor.getString(0));
            map.put("singer", cursor.getString(1));
            map.put("songUri", cursor.getString(2));
            map.put("songImage", cursor.getBlob(3));
            map.put("singLyrics", cursor.getString(4));
            map.put("information", cursor.getString(5));
            map.put("time", cursor.getString(6));
            map.put("rank", cursor.getString(7));
            map.put("expend", false);
            map.put("checked", false);
            songList.add(map);
        }
        return songList;
    }

    @Override
    public void updateRank(SQLiteDatabase sqLiteDatabase, String songName, String singer, int rank) throws SQLException {
        String sql = "update songs set rank = ? where songName = ? and singer = ?";
        String[] bindArgs = new String[]{rank + "", songName, singer};
        this.sqlManager.execWrite(sqLiteDatabase, sql, bindArgs);
    }

    @Override
    public int selectRank(SQLiteDatabase sqLiteDatabase, String songName, String singer) throws SQLException {
        String sql = "select rank from songs where songName = ? and singer = ?";
        String[] selectionArgs = new String[]{songName, singer};
        Cursor cursor = this.sqlManager.execRead(sqLiteDatabase, sql, selectionArgs);
        int rank = 0;
        if (cursor.moveToNext()) {
            rank = Integer.parseInt(cursor.getString(0));
        }
        return rank;
    }

    @Override
    public List<Map<String, Object>> selectByRank(SQLiteDatabase sqLiteDatabase) throws SQLException {
        List<Map<String, Object>> songList = new ArrayList<Map<String, Object>>();
        String sql = "select songName, singer, songUri, songImage, singlyrics, information, time, rank from songs " +
                "order by rank DESC limit 3 offset 0";
        String[] selectionArgs = new String[]{};
        Cursor cursor = this.sqlManager.execRead(sqLiteDatabase, sql, selectionArgs);
        int i = 1;
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("songName", cursor.getString(0));
            map.put("singer", cursor.getString(1));
            map.put("songUri", cursor.getString(2));
            map.put("songImage", cursor.getBlob(3));
            map.put("singLyrics", cursor.getString(4));
            map.put("information", cursor.getString(5));
            map.put("time", cursor.getString(6));
            map.put("rank", i++);
            map.put("expend", false);
            map.put("checked", false);
            songList.add(map);
        }
        return songList;
    }

    //删除本地音乐中的歌曲
    @Override
    public void deleteSong(SQLiteDatabase sqLiteDatabase, String songName, String singer) {
            String sql="delete from songs where songName=? and singer=?";
            String [] bindArgs=new String[]{songName,singer};
            this.sqlManager.execWrite(sqLiteDatabase,sql,bindArgs);
    }

    @Override
    public List<Map<String, Object>> fuzzySelectSong(SQLiteDatabase sqLiteDatabase, String str) throws SQLException {
        List<Map<String, Object>> fuzzySongList = new ArrayList<Map<String, Object>>();
        String sql = "select songName, singer, songUri, songImage, singlyrics, information, time, " +
                "rank from songs where songName like ? or singer like ?";
        String[] selectionArgs = new String[]{"%"+str+"%", "%"+str+"%"};
        Cursor cursor = this.sqlManager.execRead(sqLiteDatabase, sql, selectionArgs);
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("songName", cursor.getString(0));
            map.put("singer", cursor.getString(1));
            map.put("songUri", cursor.getString(2));
            map.put("songImage", cursor.getBlob(3));
            map.put("singLyrics", cursor.getString(4));
            map.put("information", cursor.getString(5));
            map.put("time", cursor.getString(6));
            map.put("rank", cursor.getString(7));
            map.put("expend", false);
            map.put("checked", false);
            fuzzySongList.add(map);
        }
        return fuzzySongList;
    }

    @Override
    public int selectAllSongNum(SQLiteDatabase sqLiteDatabase) throws SQLException {
        int num = 0;
        String sql = "select count(songName) from songs";
        Cursor cursor = sqlManager.execRead(sqLiteDatabase, sql, null);
        while (cursor.moveToNext()){
            num=cursor.getInt(0);
        }
        return num;
    }

}
