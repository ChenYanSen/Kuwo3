package com.designers.kuwo.utils;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;

import com.designers.kuwo.biz.SongListBiz;
import com.designers.kuwo.biz.bizimpl.SongListBizImpl;
import com.designers.kuwo.sqlite.SqLiteDatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/11.
 */
public class CustomApplication extends Application {

    public static MediaPlayer mediaPlayer = new MediaPlayer();
    private String userName = "张三";
    private List<Map<String, Object>> playList = new ArrayList<>();
    private Map<String, Object> playingSong;
    private int pattern = 0;//1：列表循环 2：单曲循环 3：随机播放
    private int progress = 0;//当前播放歌曲的进度;
    private SongListBiz songListBiz = new SongListBizImpl();
    private Uri avatarUri;
    private int time;

    @Override
    public void onCreate() {
        SqLiteDatabaseHelper sqLiteDatabaseHelper = new SqLiteDatabaseHelper(getApplicationContext());
        //获取链接
        SQLiteDatabase sqliteDatabase = sqLiteDatabaseHelper.getReadableDatabase();
        //关闭连接
        sqliteDatabase.close();

        if (MusicUtil.getObjectFromShare(getApplicationContext(), "playList") != null) {
            playList = (List<Map<String, Object>>) MusicUtil.getObjectFromShare(getApplicationContext(), "playList");
        }

        if (MusicUtil.getObjectFromShare(getApplicationContext(), "progress") != null) {
            progress = (int) MusicUtil.getObjectFromShare(getApplicationContext(), "progress");
        }

        if ((MusicUtil.getObjectFromShare(getApplicationContext(), "pattern") != null)) {
            pattern = (int) MusicUtil.getObjectFromShare(getApplicationContext(), "pattern");
        }

        if (MusicUtil.getObjectFromShare(getApplicationContext(), "playingSong") != null) {
            playingSong = (Map<String, Object>) MusicUtil.getObjectFromShare(getApplicationContext(), "playingSong");
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(playingSong.get("songUri").toString()));
                mediaPlayer.prepare();
                mediaPlayer.seekTo(progress);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if ((MusicUtil.getObjectFromShare(getApplicationContext(), "time") == null) ){
            MusicUtil.initMusic(getApplicationContext());
            time++;
        }
        super.onCreate();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Map<String, Object>> getPlayList() {
        return playList;
    }

    public void setPlayList(List<Map<String, Object>> playList) {
        this.playList = playList;
    }

    public Map<String, Object> getPlayingSong() {
        return playingSong;
    }

    public void setPlayingSong(Map<String, Object> playingSong) {
        this.playingSong = playingSong;
    }

    public int getPattern() {
        return pattern;
    }

    public void setPattern(int pattern) {
        this.pattern = pattern;
    }

    public Uri getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(Uri avatarUri) {
        this.avatarUri = avatarUri;
    }

    @Override
    public void onTrimMemory(int level) {
        MusicUtil.setObjectToShare(this, playList, "playList");
        MusicUtil.setObjectToShare(this, playingSong, "playingSong");
        MusicUtil.setObjectToShare(this, mediaPlayer.getCurrentPosition(), "progress");
        MusicUtil.setObjectToShare(this, pattern, "pattern");
        MusicUtil.setObjectToShare(this, time, "time");
        super.onTrimMemory(level);
    }
}
