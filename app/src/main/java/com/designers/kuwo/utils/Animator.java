package com.designers.kuwo.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by Zg on 2017/1/11.
 */
public class Animator {
    private ObjectAnimator discObjectAnimator;
    private ObjectAnimator neddleobjectAnimator;
    private ImageView imageView;
    private View disview;


    public Animator(ImageView imageView, View disview) {
        this.imageView = imageView;
        this.disview = disview;


        discObjectAnimator = ObjectAnimator.ofFloat(disview, "rotation", 0,360);//设置动画的旋转开始 结束位置 和动画内容
        discObjectAnimator.setDuration(15000);
        discObjectAnimator.setInterpolator(new LinearInterpolator());//定义动画变化的速率，动画以均匀速率变化
        discObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        discObjectAnimator.setRepeatMode(ValueAnimator.INFINITE);



        neddleobjectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0, 21);
        imageView.setPivotX(0);
        imageView.setPivotY(0);
        neddleobjectAnimator.setDuration(600);
        neddleobjectAnimator.setInterpolator(new LinearInterpolator());


    }

    public void startMusicAnimation() {

        discObjectAnimator.start();
        neddleobjectAnimator.start();


    }

    public void pauseMusicAnimation() {

        discObjectAnimator.pause();
        Log.i("disc", "暂停啦aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");


        neddleobjectAnimator.reverse();
        Log.i("needle", "恢复原位啦aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa ");
    }
    public void resumeMusicAnimation() {

        discObjectAnimator.resume();
        Log.i("disc", "继续转啦bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");



        neddleobjectAnimator.start();
        Log.i("needle", "转啦bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb ");
    }
    public void stopMusicAnimation() {

        discObjectAnimator.cancel();
        Log.i("disc", "恢复原位啦");


        neddleobjectAnimator.reverse();
        Log.i("needle", "停止啦 ");
    }


}

