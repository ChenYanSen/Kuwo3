package com.designers.kuwo.dao;

import android.database.sqlite.SQLiteDatabase;

import com.designers.kuwo.eneity.Song;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/20.
 */
public interface SongDao {
    public abstract void insert(final SQLiteDatabase sqLiteDatabase, Song song) throws SQLException;

    public abstract List<Map<String, Object>> select(final SQLiteDatabase sqLiteDatabase) throws SQLException;

    public abstract List<Map<String, Object>> selectFolder(final SQLiteDatabase sqLiteDatabase) throws SQLException;

    public abstract List<Map<String, Integer>> selectSongNum(final SQLiteDatabase sqLiteDatabase) throws SQLException;

    public abstract List<Song> selectSongByFolder(final SQLiteDatabase sqLiteDatabase, String folder) throws SQLException;

    public abstract List<Map<String, Object>> selectByName(final SQLiteDatabase sqLiteDatabase, final String songListName, final String userName) throws SQLException;

    public abstract void updateRank(final SQLiteDatabase sqLiteDatabase, final String songName, final String singer, final int rank) throws SQLException;

    public abstract int selectRank(final SQLiteDatabase sqLiteDatabase, final String songName, final String singer) throws SQLException;

    public abstract List<Map<String, Object>> selectByRank(final SQLiteDatabase sqLiteDatabase) throws SQLException;

    public abstract void deleteSong(final SQLiteDatabase sqLiteDatabase, final String songName, final String singer);

    public abstract List<Map<String,Object>> fuzzySelectSong(final SQLiteDatabase sqLiteDatabase,String str)throws SQLException;

    public abstract int selectAllSongNum(SQLiteDatabase sqLiteDatabase)throws SQLException;
}
