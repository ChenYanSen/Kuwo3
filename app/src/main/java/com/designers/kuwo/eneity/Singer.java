package com.designers.kuwo.eneity;

import com.designers.kuwo.utils.PinYinUtils;

/**
 * Created by PC-CWB on 2017/2/21.
 */
public class Singer {

    private String singer = null;           //歌手名
    private byte [] singerUri = null;       //歌手图片
    private String songName = null;         //歌曲名
    private String songUri = null;          //歌曲路径
    private byte [] songImage = null;
    private int songNum = 0;
    private String pinyinName = null;    //用于索引

    public Singer() {
        super();
    }

    public Singer(String singer) {
        super();
        this.singer = singer;
    }

    public Singer(String singer, int songNum, byte [] singerUri) {
        super();
        this.singer = singer;
        this.songNum = songNum;
        this.singerUri = singerUri;
    }

    public Singer(String singer, String pinyinName, int songNum, byte [] singerUri) {
        super();
        this.singer = singer;
        this.pinyinName = pinyinName;
        this.songNum = songNum;
        this.singerUri = singerUri;

    }

    public String getSinger() {
        return singer;
    }

    public byte [] getSingerUri() {
        return singerUri;
    }

    public String getSongName() {
        return songName;
    }

    public String getSongUri() {
        return songUri;
    }

    public byte [] getSongImage() {
        return songImage;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setSingerUri(byte [] singerUri) {
        this.singerUri = singerUri;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setSongUri(String songUri) {
        this.songUri = songUri;
    }

    public void setSongImage(byte [] songImage) {
        this.songImage = songImage;
    }

    public void setSongNum(int songNum) {
        this.songNum = songNum;
    }

    public int getSongNum() {
        return songNum;
    }

    //用于索引
    public void setPinyinName(String pinyinName) {
        this.pinyinName = pinyinName;
    }

    public String getPinyinName() {
        return PinYinUtils.getPinYin(singer);
    }



}

