package com.designers.kuwo.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by PC-CWB on 2017/2/28.
 */
public class IndexView extends View {

    /**
     * 每条的宽和高
     */
    private int itemWidth;
    private int itemHeight;

    private Paint paint;

    private String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setTextSize(25);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true); // 抗锯齿，字体圆滑
        paint.setTypeface(Typeface.DEFAULT_BOLD);//设置粗体字
    }

    /**
     * 测量方法
     * 测量包裹字母的条目的宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getMeasuredWidth();
        itemHeight = getMeasuredHeight() / words.length;
    }

    /**
     * 在画布上 绘制字母
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < words.length; i++) {

            // 根据onTouchEvent 监听手势滑动位置，设置当前滑动的字母的下标 来设置字母颜色 A2
            if (touchIndex == i) {
                // 设置灰色
                paint.setColor(Color.GRAY);
            } else {
                // 设置白色
                paint.setColor(Color.WHITE);
            }

            String word = words[i];// A

            Rect rect = new Rect();
            // 画笔
            // 0,1的取一个字母
            paint.getTextBounds(word, 0, 1, rect);

            // 字母的高和宽
            int wordWidth = rect.width();
            int wordHeight = rect.height();

            // 计算每个字母在视图上的坐标位置
            float wordX = itemWidth / 2 - wordWidth / 2;
            float wordY = itemHeight / 2 + wordHeight / 2 + i * itemHeight;

            canvas.drawText(word, wordX, wordY, paint);
        }
    }

    /**
     * 字母的下标位置
     */
    private int touchIndex = -1;

    /**
     * 监听手势滑动位置，设置当前滑动的字母的下标 A1
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float Y = event.getY();
                int index = (int) (Y / itemHeight); // 字母索引
                if (index != touchIndex) {

                    touchIndex = index;
                    invalidate();//强制绘制onDraw();
                    if (onIndexChangeListener != null && touchIndex > -1 && touchIndex < words.length) {
                        onIndexChangeListener.onIndexChange(words[touchIndex]); // 回传触摸的字母,告诉MainActivity
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                touchIndex = -1;
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 字母下标索引变化的监听器
     * 字母改变 回调接口 B1
     */
    public interface OnIndexChangeListener {
        /**
         * 当字母下标位置发生变化的时候回调
         *
         * @param word 字母（A~Z）
         */
        void onIndexChange(String word); // 回传一个字母
    }

    private OnIndexChangeListener onIndexChangeListener;

    /**
     * 设置字母下标索引变化的监听
     *
     * @param onIndexChangeListener
     */
    public void setOnIndexChangeListener(OnIndexChangeListener onIndexChangeListener) {
        this.onIndexChangeListener = onIndexChangeListener;
    }
}
