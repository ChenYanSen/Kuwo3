package com.designers.kuwo.biz;

import android.content.Context;

import com.designers.kuwo.eneity.SongList;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/21.
 */
public interface SongListBiz {
    public abstract void insert(final Context context, final SongList songList);

    public abstract List<Map<String, Object>> findNameAndSize(final Context context, final String userName);

    public abstract void update(final Context context, final String oldName, final String newName, final String userName);

    public abstract void delete(final Context context, final String songListName, final String userName);

    public abstract List<Map<String, Object>> findAll(final Context context);

    public abstract void deleteSong(final Context context, final String songListName, final String songName, final String singer, final String userName);

    public abstract boolean selectByName(final Context context, final String songName, final String singer,
                                         final String songListName, final String userName);
}
