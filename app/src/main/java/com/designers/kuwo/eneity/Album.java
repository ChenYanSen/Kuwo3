package com.designers.kuwo.eneity;

/**
 * Created by PC-CWB on 2017/2/21.
 */
public class Album {

    private String albumName = null;
    private String songName = null;
    private byte [] albumUri = null;
    private String singer = null;
    private int songNum = 0;

    public Album() {
        super();
    }

    public Album(String albumName, String songName) {
        super();
        this.albumName = albumName;
        this.songName = songName;
    }

    public Album(String albumName, String songName, byte [] albumUri) {
        this.albumName = albumName;
        this.songName = songName;
        this.albumUri = albumUri;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }


    public String getAlbumName() {
        return albumName;
    }

    public String getSongName() {
        return songName;
    }


    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSinger() {
        return singer;
    }

    public void setSongNum(int songNum) {
        this.songNum = songNum;
    }

    public int getSongNum() {
        return songNum;
    }

    public void setAlbumUri(byte [] albumUri) {
        this.albumUri = albumUri;
    }

    public byte [] getAlbumUri() {
        return albumUri;
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumName='" + albumName + '\'' +
                ", songName='" + songName + '\'' +
                ", singer='" + singer + '\'' +
                '}';
    }
}
