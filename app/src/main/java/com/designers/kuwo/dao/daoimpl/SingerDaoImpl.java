package com.designers.kuwo.dao.daoimpl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.designers.kuwo.dao.SingerDao;
import com.designers.kuwo.eneity.Singer;
import com.designers.kuwo.eneity.Song;
import com.designers.kuwo.sqlite.SQLManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PC-CWB on 2017/2/21.
 */
public class SingerDaoImpl implements SingerDao {

    private SQLManager sqlManager = null;

    public SingerDaoImpl(){
        this.sqlManager = new SQLManager();
    }

    @Override
    public List<Singer> selectSinger(SQLiteDatabase sqLiteDatabase) {
        List<Singer> singerList =new ArrayList<Singer>();
        String sql="select singer,singerUri,count(1) from songs group by singer";
        String[] selectionArgs = new String[]{};
        Cursor cursor=sqlManager.execRead(sqLiteDatabase,sql,selectionArgs);
        Log.i("数据库执行sql:",sql);
        while (cursor.moveToNext()){
            Singer singer=new Singer();
            singer.setSinger(cursor.getString(0));
            singer.setSingerUri(cursor.getBlob(1));
            singer.setSongNum(cursor.getInt(2));
            singerList.add(singer);
        }
        return singerList;
    }

    @Override
    public List<Singer> selectSongBySinger(SQLiteDatabase sqLiteDatabase,String singer) {
        List<Singer> songList =new ArrayList<Singer>();
        String sql="select songName,songUri,singer from songs where singer=?";
        String[] selectionArgs = new String[]{singer};
        Cursor cursor=sqlManager.execRead(sqLiteDatabase,sql,selectionArgs);
        Log.i("数据库执行sql:",sql);
        while (cursor.moveToNext()){
            Singer singer1=new Singer();
            singer1.setSongName(cursor.getString(0));
            singer1.setSongUri(cursor.getString(1));
            singer1.setSinger(cursor.getString(2));
            songList.add(singer1);
        }
        return songList;
    }

    //通过歌手查出歌曲的所有信息，放到listview中遍历出
    @Override
    public List<Song> selectSongAllMsgBySinger(SQLiteDatabase sqLiteDatabase, String singer) throws SQLException {
        List<Song> songAllBySingerList=new ArrayList<Song>();
        String sql="select songName, singer, songUri, songImage, singlyrics, time, information,rank from songs where singer=? ";
        String [] selectionArgs=new String[]{singer};
        Cursor cursor=sqlManager.execRead(sqLiteDatabase,sql,selectionArgs);
        while (cursor.moveToNext()){
            Song song=new Song();
            song.setSongName(cursor.getString(0));
            song.setSinger(cursor.getString(1));
            song.setSongUri(cursor.getString(2));
            song.setSongImage(cursor.getBlob(3));
            song.setSingLyrics(cursor.getString(4));
            song.setTime(cursor.getString(5));
            song.setInformation(cursor.getString(6));
            song.setRank(cursor.getString(7));
            songAllBySingerList.add(song);
        }
        return songAllBySingerList;
    }

    @Override
    public List<Map<String, Object>> selectSongAllMsgBySingers(SQLiteDatabase sqLiteDatabase, String singer) throws SQLException {
        List<Map<String,Object>> songAllBySingerLists=new ArrayList<Map<String,Object>>();
        String sql="select songName, singer, songUri, songImage, singlyrics, time, information,rank from songs where singer=? ";
        String [] selectionArgs=new String[]{singer};
        Cursor cursor=sqlManager.execRead(sqLiteDatabase,sql,selectionArgs);
        while (cursor.moveToNext()){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("songName", cursor.getString(0));
            map.put("singer", cursor.getString(1));
            map.put("songUri", cursor.getString(2));
            map.put("songImage", cursor.getBlob(3));
            map.put("singLyrics", cursor.getString(4));
            map.put("time", cursor.getString(5));
            map.put("information", cursor.getString(6));
            map.put("rank", cursor.getString(7));
            songAllBySingerLists.add(map);
        }
        return songAllBySingerLists;
    }
}
