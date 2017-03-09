package com.designers.kuwo.dao.daoimpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.designers.kuwo.dao.AlbumDao;
import com.designers.kuwo.eneity.Album;
import com.designers.kuwo.eneity.Song;
import com.designers.kuwo.sqlite.SQLManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PC-CWB on 2017/2/22.
 */
public class AlbumDaoImpl implements AlbumDao {

    private SQLManager sqlManager;

    public AlbumDaoImpl() {
        this.sqlManager = new SQLManager();
    }

    /**
     * 插入专辑信息
     * @param sqLiteDatabase
     * @param album
     * @throws SQLException
     */
    @Override
    public void insert(SQLiteDatabase sqLiteDatabase, Album album) throws SQLException {
        String albumName = album.getAlbumName();
        String songName = album.getSongName();
        byte[] albumUri = album.getAlbumUri();
        String singer = album.getSinger();

        ContentValues values = new ContentValues();
        values.put("albumName", albumName);
        values.put("songName", songName);
        values.put("albumUri", albumUri);
        values.put("singer", singer);
        sqLiteDatabase.insert("album", null, values);
    }

    /**
     * 查询出专辑所需新的数据添加到专辑对象中
     *
     * @param sqLiteDatabase
     * @return
     */

    @Override
    public List<Album> selectAlbum(SQLiteDatabase sqLiteDatabase) {
        List<Album> albumList = new ArrayList<Album>();
        String sql = "select distinct albumName,albumUri,count(distinct songName) from album group by albumName";
        String[] selectionArgs = new String[]{};
        Cursor cursor = sqlManager.execRead(sqLiteDatabase, sql, selectionArgs);
        Log.i("数据库执行sql:", sql);
        while (cursor.moveToNext()) {
            Album album = new Album();
            album.setAlbumName(cursor.getString(0));
            album.setAlbumUri(cursor.getBlob(1));
            album.setSongNum(cursor.getInt(2));
            albumList.add(album);
        }
        return albumList;
    }

    /**
     * 通过条件查询将所属专辑的歌曲查找出来
     * @param sqLiteDatabase
     * @param albums
     * @return
     */

    @Override
    public List<Album> selectSongByAlbum(SQLiteDatabase sqLiteDatabase, String albums) {
        List<Album> songListByAlbum = new ArrayList<Album>();
        String sql = "select distinct albumName,songName from album where albumName = ?";
        String[] selectionArgs = new String[]{albums};
        Cursor cursor = sqlManager.execRead(sqLiteDatabase, sql, selectionArgs);
        while (cursor.moveToNext()) {
            Album album = new Album();
            album.setAlbumName(cursor.getString(0));
            album.setSongName(cursor.getString(1));
            songListByAlbum.add(album);
        }
        return songListByAlbum;
    }

    @Override
    public List<Song> selectSongAllByAlbum(SQLiteDatabase sqLiteDatabase, String album) throws SQLException {
        List<Song> songAllByAlbumList = new ArrayList<Song>();
        String sql = "select songName, singer, songUri, songImage, singlyrics, time, information,rank from songs where albumName=? ";
        String[] selectionArgs = new String[]{album};
        Cursor cursor = sqlManager.execRead(sqLiteDatabase, sql, selectionArgs);
        while (cursor.moveToNext()) {
            Song song=new Song();
            song.setSongName(cursor.getString(0));
            song.setSinger(cursor.getString(1));
            song.setSongUri(cursor.getString(2));
            song.setSongImage(cursor.getBlob(3));
            song.setSingLyrics(cursor.getString(4));
            song.setTime(cursor.getString(5));
            song.setInformation(cursor.getString(6));
            song.setRank(cursor.getString(7));
            songAllByAlbumList.add(song);
        }
        return songAllByAlbumList;
    }

    //专辑中有歌手名，专辑名，歌曲名，根据这几项，关联查询到当前songs表中的所有符合的数据
    @Override
    public List<Map<String, Object>> selectAllSongByAlbums(SQLiteDatabase sqLiteDatabase, String album) throws SQLException {
        List<Map<String,Object>> songAllByAlbums=new ArrayList<Map<String,Object>>();
        String sql="SELECT distinct album.singer,album.albumName,album.songName,songs.songUri,songs.songImage,songs.singLyrics,songs.information,songs.time,songs.rank FROM songs INNER JOIN album ON album.songName=songs.songName AND album.singer=songs.singer WHERE album.albumName=?";
        String [] selectionArgs=new String[]{album};
        Cursor cursor=sqlManager.execRead(sqLiteDatabase,sql,selectionArgs);
        int i = 1;
        while (cursor.moveToNext()){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("singer", cursor.getString(0));
            map.put("albumName",cursor.getString(1));
            map.put("songName", cursor.getString(2));
            map.put("songUri", cursor.getString(3));
            map.put("songImage", cursor.getBlob(4));
            map.put("singLyrics", cursor.getString(5));
            map.put("information", cursor.getString(6));
            map.put("time", cursor.getString(7));
            map.put("rank", i++);
            songAllByAlbums.add(map);
        }
        return songAllByAlbums;
    }
}
