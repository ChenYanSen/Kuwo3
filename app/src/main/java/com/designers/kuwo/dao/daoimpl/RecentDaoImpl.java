package com.designers.kuwo.dao.daoimpl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.designers.kuwo.dao.RecentDao;
import com.designers.kuwo.eneity.Recent;
import com.designers.kuwo.sqlite.SQLManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/24.
 */
public class RecentDaoImpl implements RecentDao {

    private SQLManager sqlManager;

    public RecentDaoImpl() {
        this.sqlManager = new SQLManager();
    }

    @Override
    public void insert(SQLiteDatabase sqLiteDatabase, Recent recent) throws SQLException {
        String songName = recent.getSongName();
        String date = recent.getDate();
        String sql = "insert into recent(songName, date) values(?,?)";
        String[] bindArgs = new String[]{songName, date};
        this.sqlManager.execWrite(sqLiteDatabase, sql, bindArgs);
    }

    /**
     * 按时间排序查询
     *
     * @param sqLiteDatabase
     * @return
     * @throws SQLException
     */
    @Override
    public List<Map<String, Object>> select(SQLiteDatabase sqLiteDatabase) throws SQLException {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        String sql = "select songs.songName, singer, songUri, songImage, singlyrics, information, time " +
                "from songs inner join recent on songs.songName = recent.songName " +
                "order by date DESC limit 15 offset 0";
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
            map.put("expend", false);
            map.put("checked", false);
            listItems.add(map);
        }
        return listItems;
    }

    /**
     * 更新歌曲播放的时间
     *
     * @param sqLiteDatabase
     * @param recent
     * @throws SQLException
     */
    @Override
    public void update(SQLiteDatabase sqLiteDatabase, Recent recent) throws SQLException {
        String songName = recent.getSongName();
        String date = recent.getDate();
        String sql = "update recent set date = ? where songName = ?";
        String[] bindArgs = new String[]{date, songName};
        this.sqlManager.execWrite(sqLiteDatabase, sql, bindArgs);
    }

    /**
     * 查询歌曲是否存在
     *
     * @param sqLiteDatabase
     * @param songName
     * @return
     * @throws SQLException
     */
    @Override
    public boolean selectByName(SQLiteDatabase sqLiteDatabase, String songName) throws SQLException {
        String sql = "select songName from recent where songName = ?";
        String[] selectionArgs = new String[]{songName};
        Cursor cursor = this.sqlManager.execRead(sqLiteDatabase, sql, selectionArgs);
        boolean flag = false;
        if (cursor.moveToNext())
            flag = true;
        return flag;
    }

    /**
     * 根据歌曲删除
     *
     * @param sqLiteDatabase
     * @param songName
     * @throws SQLException
     */
    @Override
    public void delete(SQLiteDatabase sqLiteDatabase, String songName) throws SQLException {
        String sql = "delete from recent where songName = ?";
        String[] bindArgs = new String[]{songName};
        this.sqlManager.execWrite(sqLiteDatabase, sql, bindArgs);
    }

    /**
     * 清空
     *
     * @param sqLiteDatabase
     * @throws SQLException
     */
    @Override
    public void deleteAll(SQLiteDatabase sqLiteDatabase) throws SQLException {
        String sql = "delete from recent";
        String[] bindArgs = new String[]{};
        this.sqlManager.execWrite(sqLiteDatabase, sql, bindArgs);
    }
}
