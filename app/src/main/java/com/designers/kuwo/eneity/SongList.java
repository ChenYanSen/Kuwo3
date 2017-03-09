package com.designers.kuwo.eneity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/13.
 */
public class SongList implements Serializable {

    private String songListName;
    private String songName;
    private String singer;
    private String account;

    public SongList() {
        super();
    }

    public SongList(String songListName, String songName, String singer, String account) {
        this.songListName = songListName;
        this.songName = songName;
        this.account = account;
        this.singer = singer;
    }

    public String getSongListName() {
        return songListName;
    }

    public void setSongListName(String songListName) {
        this.songListName = songListName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    @Override
    public String toString() {
        return "SongList{" +
                "songListName='" + songListName + '\'' +
                ", songName='" + songName + '\'' +
                ", singer='" + singer + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}

