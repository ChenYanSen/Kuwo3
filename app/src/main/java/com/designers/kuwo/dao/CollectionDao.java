package com.designers.kuwo.dao;

import android.database.sqlite.SQLiteDatabase;

import com.designers.kuwo.eneity.ICollection;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by 跃 on 2017/2/22.
 */
public interface CollectionDao {
    /**
     * 添加收藏
     *
     * @param iCollection
     * @param sqLiteDatabase
     * @return
     * @throws SQLException
     */
    public void insertCollection(final ICollection iCollection, final SQLiteDatabase sqLiteDatabase) throws SQLException;

    /**
     * 删除收藏
     *
     * @param song
     * @param singer
     * @param account
     * @param sqLiteDatabase
     * @return
     * @throws SQLException
     */
    public void deleteCollection(final String song, final String singer, final String account, final SQLiteDatabase sqLiteDatabase) throws SQLException;

    /**
     * 查询是否收藏过
     *
     * @param song
     * @param singer
     * @param account
     * @param sqLiteDatabase
     * @return
     * @throws SQLException
     */
    public boolean selectCollection(final String song, final String singer, final String account, final SQLiteDatabase sqLiteDatabase) throws SQLException;


    /**
     * 单表查询收藏表中的所有歌曲
     */
    public List<ICollection> selectCollectionAllSongs(final SQLiteDatabase sqLiteDatabase) throws SQLException;


    public List<Map<String, Object>> selectAllCollectionSong(final SQLiteDatabase sqLiteDatabase) throws SQLException;

}
