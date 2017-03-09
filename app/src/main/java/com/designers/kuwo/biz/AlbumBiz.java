package com.designers.kuwo.biz;

import android.content.Context;

import com.designers.kuwo.eneity.Album;
import com.designers.kuwo.eneity.Song;

import java.util.List;
import java.util.Map;

/**
 * Created by PC-CWB on 2017/2/22.
 */
public interface AlbumBiz {

    /**
     * 将本地音乐扫描一下，把专辑中的所有歌曲
     * @param context
     * @param album
     */
    public abstract void insert(final Context context, final Album album);

    /**
     * 查询出专辑名，专辑头像uri,专辑歌曲数目
     */
    public abstract List<Album> AlbumFind(final Context context);

    public abstract List<Album> songFindByAlbum(final Context context, String albums);

    public abstract List<Song> songAllFindByAlbum(final Context context, String album);

    public abstract List<Map<String,Object>> songFindAllByAlbums(final Context context, String album);

}
