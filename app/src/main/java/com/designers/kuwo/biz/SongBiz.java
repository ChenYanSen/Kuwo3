package com.designers.kuwo.biz;

import android.content.Context;

import com.designers.kuwo.eneity.Song;

import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/2/20.
 */
public interface SongBiz {

    public abstract void insert(final Context context, final Song song);

    public abstract List<Map<String, Object>> findAll(final Context context);

    //扫描出歌曲的文件夹
    public abstract List<Map<String,Object>> findFolder(final Context context);

    //扫描出文件夹的歌曲数目
    public abstract List<Map<String,Integer>> findSongNum(final Context context);

    //根据文件扫描出歌曲
    public abstract List<Song> findSongByFolder(final Context context, final String folder);

    public abstract List<Map<String, Object>> findByName(final Context context, final String songListName, final String userName);

    public abstract void updateRank(final Context context, final String songName, final String singer, final int rank);

    public abstract int selectRank(final Context context, final String songName, final String singer);

    public abstract List<Map<String, Object>> selectByRank(final Context context);

    public abstract void  removeSong(final Context context, final String songName, final String singer);

    //模糊查询输入的歌曲
    public abstract List<Map<String,Object>> similarSong(final Context context,String str);

    public abstract int songNum(final Context context);
}
