package com.designers.kuwo.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqLiteDatabaseHelper extends SQLiteOpenHelper {

    public SqLiteDatabaseHelper(Context context,
                                CursorFactory factory, int version) {
        super(context, "music", factory, version);
        // TODO Auto-generated constructor stub
    }

    public SqLiteDatabaseHelper(Context context) {
        super(context, "music", null, 1);
        // TODO Auto-generated constructor stub
    }

    public SqLiteDatabaseHelper(Context context, int version) {
        super(context, "music", null, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql_cascade = "PRAGMA foreign_keys=ON";
        sqLiteDatabase.execSQL(sql_cascade);

        /**
         * 创建user表，用户的信息 ID 账号，密码  爱好  昵称  个性签名 性别 地区 头像
         */
        String sql_user = "create table user(account varchar(20) primary key," +
                "password varchar(20)," +
                "hobby varchar(30)," +
                "nickname varchar(20)," +
                "signature txt," +
                "sex varchar(10)," +
                "area varchar(50)," +
                "avatarUri varchar(100))";
        sqLiteDatabase.execSQL(sql_user);

        /**
         * 创建歌单表 歌单名  歌名 账号（参照用户的账号）
         */
        String sql_songList = "create table songList(songListName varchar(20)," +
                "singer text,"+
                "songName varchar(30)," +
                "account varchar(20))";
        sqLiteDatabase.execSQL(sql_songList);

        /**
         * 专辑
         */
        String sql_album = "create table album(albumName varchar(20) ," +
                "songName varchar(20),"+
                "singer varchar(20)," +
                "albumUri blob," +
                "account varchar(20) references user(account))";
        sqLiteDatabase.execSQL(sql_album);

        /**
         * 创建音乐表， 歌名 歌手 存储路径 图片 歌词 详细信息
         */
        String sql_songs = "create table songs(songName varchar(20) ," +
                "singer varchar(20)," +
                "songUri varchar(100)," +
                "songImage blob," +
                "singerUri blob," +
                "singlyrics text," +
                "time varchar(20)," +
                "information varchar(50) ," +
                "folder varchar(50),"+
                "rank text," +
                "primary key(songName,singer))";
        sqLiteDatabase.execSQL(sql_songs);


        /**
         * 收藏
         */
        String sql_collection = "create table collection(songName varchar(20)," +
                "singer varchar(20),"+
                "songUri varchar(100)," +
                "songImage blob," +
                "singlyrics text," +
                "time varchar(20)," +
                "information varchar(50)," +
                "folder varchar(50),"+
                "rank text,"+
                "account varchar(20))";
        sqLiteDatabase.execSQL(sql_collection);

        /**
         * 创建最近播放列表  歌名 歌手
         */
        String sql_recentlyPlayed = "create table recent(songName varchar(30)," +
                "date text)";
        sqLiteDatabase.execSQL(sql_recentlyPlayed);

        /**
         * 创建音乐视频播放列表
         */
        String sql_videoPlay = "create table videoPlay(videoName varchar (20)," +
                "videoUri varchar(50)," +
                "primary key(videoName,videoUri))";
        sqLiteDatabase.execSQL(sql_videoPlay);

        Log.i("test", "表创建成功!");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}
