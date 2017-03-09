package com.designers.kuwo.dao;

import android.database.sqlite.SQLiteDatabase;

import com.designers.kuwo.eneity.Singer;
import com.designers.kuwo.eneity.Song;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by PC-CWB on 2017/2/21.
 */
public interface SingerDao {

    /**
     * select singer info for singer of basic
     * @param sqLiteDatabase
     * @return
     */
    public List<Singer> selectSinger(SQLiteDatabase sqLiteDatabase)throws SQLException;

    /**
     * select song info by singer
     */
    public List<Singer> selectSongBySinger(SQLiteDatabase sqLiteDatabase, String singer)throws SQLException;

    /**
     * select song all message by singer
     */
    public List<Song> selectSongAllMsgBySinger(SQLiteDatabase sqLiteDatabase, String singer)throws SQLException;

    public List<Map<String,Object>> selectSongAllMsgBySingers(SQLiteDatabase sqLiteDatabase, String singer)throws SQLException;

}
