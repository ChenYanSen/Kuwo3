package com.designers.kuwo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import com.designers.kuwo.R;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2017/3/1.
 */
public class MusicPlayer implements MediaPlayer.OnCompletionListener {

    private ImageView playimg;
    private ImageView play;
    private TextView songname;
    private TextView singer;
    private Context context;
    private CustomApplication customApplication;
    private Map<String, Object> playingSong;
    private List<Map<String, Object>> playList;
    private int pattern;
    private int currentIndex = -1;

    public MusicPlayer(Context context, CustomApplication customApplication,
                       ImageView playimg, ImageView play, TextView songname, TextView singer) {
        this.playimg = playimg;
        this.play = play;
        this.songname = songname;
        this.singer = singer;
        this.context = context;
        this.customApplication = customApplication;
        playingSong = this.customApplication.getPlayingSong();
        playList = this.customApplication.getPlayList();
        this.pattern = this.customApplication.getPattern();
        currentIndex = getCurrentIndex();
        CustomApplication.mediaPlayer.setOnCompletionListener(this);
    }

    //点击播放和停止
    public void play() {
      /*  if (!CustomApplication.mediaPlayer.isPlaying() && customApplication.getPosition() == -1) {
            if (currentIndex != -1) {
                create(playList.get(currentIndex).get("songUri").toString());
                CustomApplication.mediaPlayer.start();
                play.setImageResource(R.drawable.stop);
            }
        } else */
        if (CustomApplication.mediaPlayer.isPlaying()) {
            CustomApplication.mediaPlayer.pause();
            play.setImageResource(R.drawable.play);
        } else {
            CustomApplication.mediaPlayer.start();
            play.setImageResource(R.drawable.stop);
        }

    }

    //点击下一首
    public void next() {
        //列表循环
        if (currentIndex < playList.size() - 1) {
            currentIndex++;
        } else {
            currentIndex = 0;
        }
        create(playList.get(currentIndex).get("songUri").toString());
        CustomApplication.mediaPlayer.start();
        byte[] in = (byte[]) playList.get(currentIndex).get("songImage");
        Bitmap bm = BitmapFactory.decodeByteArray(in, 0, in.length);
        playimg.setImageBitmap(bm);
        songname.setText(playList.get(currentIndex).get("songName").toString());
        singer.setText(playList.get(currentIndex).get("singer").toString());
        play.setImageResource(R.drawable.stop);
        customApplication.setPlayingSong(playList.get(currentIndex));
    }

    //播放完成
    @Override
    public void onCompletion(MediaPlayer mp) {
        switch (pattern) {
            //列表循环
            case 0:
                if (currentIndex < playList.size() - 1 && !CustomApplication.mediaPlayer.isPlaying()) {
                    currentIndex++;
                    create(playList.get(currentIndex).get("songUri").toString());
                    CustomApplication.mediaPlayer.start();
                    byte[] in = (byte[]) playList.get(currentIndex).get("songImage");
                    Bitmap bm = BitmapFactory.decodeByteArray(in, 0, in.length);
                    playimg.setImageBitmap(bm);
                    songname.setText(playList.get(currentIndex).get("songName").toString());
                    singer.setText(playList.get(currentIndex).get("singer").toString());
                    customApplication.setPlayingSong(playList.get(currentIndex));
                } else {
                    currentIndex = -1;
                }
                break;
            //单曲循环
            case 1:
                if (!CustomApplication.mediaPlayer.isPlaying() && currentIndex != -1) {
                    create(playList.get(currentIndex).get("songUri").toString());
                    CustomApplication.mediaPlayer.start();
                    byte[] in = (byte[]) playList.get(currentIndex).get("songImage");
                    Bitmap bm = BitmapFactory.decodeByteArray(in, 0, in.length);
                    playimg.setImageBitmap(bm);
                    songname.setText(playList.get(currentIndex).get("songName").toString());
                    singer.setText(playList.get(currentIndex).get("singer").toString());
                    customApplication.setPlayingSong(playList.get(currentIndex));
                }
                break;
            //随机播放
            case 2:
                if (!CustomApplication.mediaPlayer.isPlaying() && currentIndex != -1) {
                    Random random = new Random();
                    currentIndex = random.nextInt(playList.size() + 1);
                    create(playList.get(currentIndex).get("songUri").toString());
                    CustomApplication.mediaPlayer.start();
                    byte[] in1 = (byte[]) playList.get(currentIndex).get("songImage");
                    Bitmap bm1 = BitmapFactory.decodeByteArray(in1, 0, in1.length);
                    playimg.setImageBitmap(bm1);
                    songname.setText(playList.get(currentIndex).get("songName").toString());
                    singer.setText(playList.get(currentIndex).get("singer").toString());
                    customApplication.setPlayingSong(playList.get(currentIndex));
                }
                break;
        }
    }

    private void create(String path) {
        try {
            CustomApplication.mediaPlayer.reset();
            CustomApplication.mediaPlayer.setDataSource(context, Uri.parse(path));
            CustomApplication.mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getCurrentIndex() {
        int i = 0;
        for (Map map : playList) {
            if (map.get("songName").toString().equals(playingSong.get("songName").toString())
                    && map.get("singer").toString().equals(playingSong.get("singer").toString())) {
                return i;
            } else {
                i++;
            }
        }
        return -1;
    }
}
