package com.designers.kuwo.biz;

import android.content.Context;

import com.designers.kuwo.eneity.Singer;
import com.designers.kuwo.eneity.Song;

import java.util.List;
import java.util.Map;

/**
 * Created by PC-CWB on 2017/2/21.
 */
public interface SingerBiz {

    //用于歌手页面的数据赋值
    public List<Singer> SingerFind(final Context context);

    //通过歌手来查询该歌手的所有歌曲
    public List<Singer> FindSongBySinger(final Context context, final String singer);

    //通过歌手查询到该歌手的所有歌曲信息

    public List<Song> FindSongAllMsgBySinger(final Context context, final String singer);

    //返回list<Map<String,Object>>结构的通过查找歌手找到歌手的相应的歌曲

    public List<Map<String,Object>> FindSongAllMsgBySingers(final Context context, final String singer);
}
