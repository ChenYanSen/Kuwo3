package com.designers.kuwo.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * ；侧滑
 */
public class MyHorizontalScollView extends HorizontalScrollView {

    private LinearLayout mWrapper;//滚动条中的水平线性布局
    private ViewGroup mMenu;//左侧menu
    private ViewGroup mContent;//右侧布局
    private int mScreenWidth;//屏幕的宽度
    private int mMenuWidth;//menu的宽度
    private int mMenuRightPadding = 50;
    private boolean once;
    private float startY;//手指按下时Y轴的坐标
    private boolean isOpen;//初始时侧滑栏关闭

    public MyHorizontalScollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * 获取屏幕的宽度
         * 通过context拿到windowManager,在通过windowMnager拿到Metrics,用DisplayMetrics接
         */
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        //把dp转换成px
        mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
    }

    /**
     * 设置子View的宽和高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!once) {
            mWrapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWrapper.getChildAt(0);
            mContent = (ViewGroup) mWrapper.getChildAt(1);
            //menu的宽度等于屏幕的宽度减去menu离屏幕右侧的边距
            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            //右边的线性布局直接等于屏幕的宽度
            mContent.getLayoutParams().width = mScreenWidth;
            once = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 通过偏移量将menu隐藏
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        /**
         * 通过scrollTo(x,y)方法设置屏幕的偏移量，x为正
         */
        if (changed) {
            this.scrollTo(mMenuWidth, 0);
        }
    }

    /**
     * 因为HorizontalScrollView自己控制move和down事件
     * 所以还要判断up.如果当前的x偏移量大于menu的一半隐藏menu，否则显示
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        startY = ev.getY();
        //Log.i("test", "" + startY);
        switch (action) {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX >= mMenuWidth / 2) {
                    this.smoothScrollTo(mMenuWidth, 0);
                } else {
                    this.smoothScrollTo(0, 0);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (startY > 1070)
                    return false;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (isOpen)
            return;
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (isOpen) {
            this.smoothScrollTo(mMenuWidth, 0);
            isOpen = false;
        }
    }

    /**
     * 切换菜单状态
     */
    public void toggle() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    /**
     * 菜单视图和滑动视图动画效果
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / mMenuWidth;
        float leftScale = 1 - 0.3f * scale;
        float rightScale = 0.8f + scale * 0.2f;

        mMenu.setScaleX(leftScale);
        mMenu.setScaleY(leftScale);
        mMenu.setAlpha(0.6f + 0.4f * (1 - scale));
        mMenu.setTranslationX(mMenuWidth * scale * 0.6f);
        mContent.setPivotX(0);
        mContent.setPivotY(mContent.getHeight() / 2);
        mContent.setScaleX(rightScale);
        mContent.setScaleY(rightScale);
    }
}
