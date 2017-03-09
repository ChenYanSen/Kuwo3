package com.designers.kuwo.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.designers.kuwo.R;
import com.designers.kuwo.utils.CustomApplication;
import com.designers.kuwo.utils.ILrcViewListener;
import com.designers.kuwo.utils.LrcRow;
import com.designers.kuwo.utils.LrcView;


/**
 * Created by Zg on 2017/1/10.
 */
public class ViewPager_2_Fragment extends Fragment {

    private LrcView lrcView;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setView(inflater.inflate(R.layout.activity_view_pager_2, container, false));
        setLrcView((LrcView) getView().findViewById(R.id.lrcShowView));
/*lrcView.setListener( new ILrcView());*/
        /*lrcView.setLrcContents(((KuMuiscActivity) getActivity()).getLrcList());*/
        handler.post(runnable);
        //设置自定义的LrcView上下拖动歌词时监听
        lrcView.setListener(new ILrcViewListener() {
            //当歌词被用户上下拖动的时候回调该方法,从高亮的那一句歌词开始播放
            public void onLrcSeeked(int newPosition, LrcRow row) {
                if (CustomApplication.mediaPlayer != null) {

                    CustomApplication.mediaPlayer.seekTo((int) row.time);
                }
            }
        });




        /*new Thread( new runable());*/

        return getView();
    }

    public LrcView getLrcView() {
        return lrcView;
    }

    public void setLrcView(LrcView lrcView) {
        this.lrcView = lrcView;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            handler.post(runnable);

        }

    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {


            if (KuMuiscActivity.DestoryFlag == 0) {
                lrcView.setLrc(((KuMuiscActivity) getActivity()).getLrcList());
                lrcView.seekLrcToTime(((KuMuiscActivity) getActivity()).getIndexTime());
                lrcView.invalidate();
                handler.postDelayed(runnable, 50);
            }else {
                return;
            }


        }
    };

 /*   @Override
    public boolean onTouchEvent(MotionEvent event) {
// TODO Auto-generated method stub
        System.out.println("bllll==="+blScrollView);
        float tt=event.getY();
        if(!blLrc){
//return super.onTouchEvent(event);
            return super.onTouchEvent(event);
        }
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchX=event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                touchY=tt-touchY;
                offsetY=offsetY+touchY;
                break;
            case MotionEvent.ACTION_UP:
                blScrollView=false;
                break;
        }
        touchY=tt;
        return true;
    }*/

}
