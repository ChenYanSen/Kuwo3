package com.designers.kuwo.eneity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/1/12.
 */
public class Song implements Serializable {

    private String songName = null;
    private String singer = null;
    private String songUri = null;
    private byte[] songImage = null;
    private String singLyrics = null;
    private String information = null;
    private String time = null;
    private String rank=null;
    private byte[] singerUri = null;
    private String folder = null;

    public Song() {
    }

    public Song(String songName, String singer, String songUri, byte [] songImage, String singLyrics, String information, String time) {
        this.songName = songName;
        this.singer = singer;
        this.songUri = songUri;
        this.songImage = songImage;
        this.singLyrics = singLyrics;
        this.information = information;
        this.time = time;
    }

    public Song(String songName, String singer, String songUri, byte [] songImage, String singLyrics, String information, String time,
                byte [] singerUri) {
        this.songName = songName;
        this.singer = singer;
        this.songUri = songUri;
        this.songImage = songImage;
        this.singLyrics = singLyrics;
        this.information = information;
        this.time = time;
        this.singerUri = singerUri;

    }

    public Song(String songName, String singer, String songUri, byte [] songImage, String singLyrics, String information, String time,
                byte [] singerUri , String folder) {
        this.songName = songName;
        this.singer = singer;
        this.songUri = songUri;
        this.songImage = songImage;
        this.singLyrics = singLyrics;
        this.information = information;
        this.time = time;
        this.singerUri = singerUri;
        this.folder = folder;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSongUri() {
        return songUri;
    }

    public void setSongUri(String songUri) {
        this.songUri = songUri;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setSongImage(byte[] songImage) {
        this.songImage = songImage;
    }

    public byte[] getSongImage() {
        return songImage;
    }

    public String getRank() {
        return rank;
    }

    public String getSingLyrics() {
        return singLyrics;
    }

    public void setSingLyrics(String singLyrics) {
        this.singLyrics = singLyrics;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSingerUri(byte [] singerUri) {
        this.singerUri = singerUri;
    }


    public byte [] getSingerUri() {
        return singerUri;
    }


    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songName='" + songName + '\'' +
                ", singer='" + singer + '\'' +
                ", songUri='" + songUri + '\'' +
                ", songImage=" + Arrays.toString(songImage) +
                ", singLyrics='" + singLyrics + '\'' +
                ", information='" + information + '\'' +
                ", time='" + time + '\'' +
                ", rank='" + rank + '\'' +
                ", singerUri=" + Arrays.toString(singerUri) +
                ", folder='" + folder + '\'' +
                '}';
    }
}
