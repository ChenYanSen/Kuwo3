package com.designers.kuwo.eneity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/24.
 */
public class Recent implements Serializable {

    private String songName;
    private String date;

    public Recent(String songName, String date) {
        this.songName = songName;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    @Override
    public String toString() {
        return "Recent{" +
                "songName='" + songName + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
