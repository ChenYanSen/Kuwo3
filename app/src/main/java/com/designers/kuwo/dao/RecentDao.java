package com.designers.kuwo.dao;

import android.database.sqlite.SQLiteDatabase;

import com.designers.kuwo.eneity.Recent;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/24.
 */
public interface RecentDao {

    public abstract void insert(final SQLiteDatabase sqLiteDatabase, final Recent recent) throws SQLException;

    public abstract List<Map<String, Object>> select(final SQLiteDatabase sqLiteDatabase) throws SQLException;

    public abstract void update(final SQLiteDatabase sqLiteDatabase, final Recent recent) throws SQLException;

    public abstract boolean selectByName(final SQLiteDatabase sqLiteDatabase, final String songName) throws SQLException;

    public abstract void delete(final SQLiteDatabase sqLiteDatabase, final String songName) throws SQLException;

    public abstract void deleteAll(final SQLiteDatabase sqLiteDatabase) throws SQLException;
}
