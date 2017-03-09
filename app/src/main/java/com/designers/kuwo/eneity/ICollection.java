package com.designers.kuwo.eneity;

public class ICollection {
    String songName;
    String singer;
    String songUri;
    byte [] songImage;
    String singlyrics;
    String time;
    String information;
    String folder;
    String rank;
    String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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


    public void setSongUri(String songUri) {
        this.songUri = songUri;
    }

    public void setSongImage(byte [] songImage) {
        this.songImage = songImage;
    }

    public void setSinglyrics(String singlyrics) {
        this.singlyrics = singlyrics;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }


    public String getSongUri() {
        return songUri;
    }

    public byte [] getSongImage() {
        return songImage;
    }

    public String getSinglyrics() {
        return singlyrics;
    }


    public String getTime() {
        return time;
    }

    public String getInformation() {
        return information;
    }

    public String getFolder() {
        return folder;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "ICollection{" +
                "songName='" + songName + '\'' +
                ", singer='" + singer + '\'' +
                ", songUri='" + songUri + '\'' +
                ", singlyrics='" + singlyrics + '\'' +
                ", time='" + time + '\'' +
                ", information='" + information + '\'' +
                ", folder='" + folder + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}
