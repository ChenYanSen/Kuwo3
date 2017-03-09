package com.designers.kuwo.dao;

import android.database.sqlite.SQLiteDatabase;

import com.designers.kuwo.eneity.Album;
import com.designers.kuwo.eneity.Song;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by PC-CWB on 2017/2/22.
 */
public interface AlbumDao {

    public abstract void insert(final SQLiteDatabase sqLiteDatabase, Album album) throws SQLException;

    public abstract List<Album> selectAlbum(SQLiteDatabase sqLiteDatabase) throws SQLException;

    public abstract List<Album> selectSongByAlbum(SQLiteDatabase sqLiteDatabase, String albums) throws SQLException;

    public abstract List<Song> selectSongAllByAlbum(SQLiteDatabase sqLiteDatabase, String album) throws SQLException;

    public abstract List<Map<String,Object>> selectAllSongByAlbums(SQLiteDatabase sqLiteDatabase, String album) throws SQLException;
}
