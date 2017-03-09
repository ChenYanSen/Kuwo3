package com.designers.kuwo.dao;

import android.database.sqlite.SQLiteDatabase;

import com.designers.kuwo.eneity.SongList;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/21.
 */
public interface SongListDao {
    public abstract void insert(final SQLiteDatabase sqLiteDatabase, final SongList songList) throws SQLException;

    public abstract List<Map<String, Object>> selectNameAndSize(final SQLiteDatabase sqLiteDatabase, final String userName) throws SQLException;

    public abstract void update(final SQLiteDatabase sqLiteDatabase, final String oldName, final String newName, final String userName) throws SQLException;

    public abstract void delete(final SQLiteDatabase sqLiteDatabase, final String songListName, final String userName) throws SQLException;

    public abstract List<Map<String, Object>> select(final SQLiteDatabase sqLiteDatabase) throws SQLException;

    public abstract void deleteSong(final SQLiteDatabase sqLiteDatabase, final String songListName, final String songName, final String singer, final String userName) throws SQLException;

    public abstract  boolean selectByName(final SQLiteDatabase sqLiteDatabase, final String songName, final String singer,
                                          final String songListName, final String userName) throws SQLException;
}
