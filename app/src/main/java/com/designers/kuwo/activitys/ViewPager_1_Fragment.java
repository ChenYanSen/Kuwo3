package com.designers.kuwo.activitys;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.designers.kuwo.R;
import com.designers.kuwo.utils.CustomApplication;

import java.util.Map;

/**
 * Created by Zg on 2017/1/10.
 */
@SuppressWarnings("deprecation")
public class ViewPager_1_Fragment extends Fragment {
    private View view;
    private ImageView imageView;
    private View disView;



    public ViewPager_1_Fragment( ) {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setView(inflater.inflate(R.layout.activity_view_pager_1, container, false));
        setImageView((ImageView) getView().findViewById(R.id.needle));

        handler.post(runnable);

     /*   Map<String, Object> playingSong = ((CustomApplication) getActivity().getApplication()).getPlayingSong();
        byte[] in = (byte[]) playingSong.get("songImage");
         bm = BitmapFactory.decodeByteArray(in, 0, in.length);
        //外层透明色
        OvalShape ovalShape1 = new OvalShape();
        ShapeDrawable shapeDrawable1 = new ShapeDrawable(ovalShape1);

        shapeDrawable1.getPaint().setColor(0x10000000);//设置画笔的颜色

        shapeDrawable1.getPaint().setStyle(Paint.Style.FILL);//设置填充
        shapeDrawable1.getPaint().setAntiAlias(true);//设置抗锯齿
//黑色的唱片边框
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.play_border));
        roundedBitmapDrawable.setCircular(true);
        roundedBitmapDrawable.setAntiAlias(true);
//内层黑色边线
        OvalShape ovalShape2 = new OvalShape();
        ShapeDrawable shapeDrawable2 = new ShapeDrawable(ovalShape2);
        shapeDrawable2.getPaint().setColor(Color.BLACK);
        shapeDrawable2.getPaint().setAntiAlias(true);
        shapeDrawable2.getPaint().setStyle(Paint.Style.FILL);
        //内层图片
        RoundedBitmapDrawable roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(getResources(),bm);
        roundedBitmapDrawable1.setCircular(true);
        roundedBitmapDrawable.setAntiAlias(true);


        Drawable[] drawable = new Drawable[4];
        drawable[0] = shapeDrawable1;
        drawable[1] = roundedBitmapDrawable;
        drawable[2] = shapeDrawable2;
        drawable[3] = roundedBitmapDrawable1;
        LayerDrawable layerDrawable = new LayerDrawable(drawable);
        int width = 7;

        layerDrawable.setLayerInset(1, width * 1, width * 1, width * 1, width * 1);
        layerDrawable.setLayerInset(2, width * 11, width * 11, width * 11, width * 11);
        layerDrawable.setLayerInset(3, width * 12, width * 12, width * 12, width * 12);

        setDisView(getView().findViewById(R.id.myView));
        getDisView().setBackground(layerDrawable);*/

        return getView();
    }

    public View getDisView() {
        return disView;
    }

    public void setDisView(View disView) {
        this.disView = disView;
    }

    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
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
            Map<String, Object> playingSong = ((CustomApplication) getActivity().getApplication()).getPlayingSong();
            byte[] in = (byte[]) playingSong.get("songImage");
            Bitmap bm = BitmapFactory.decodeByteArray(in, 0, in.length);
            //外层透明色
            OvalShape ovalShape1 = new OvalShape();
            ShapeDrawable shapeDrawable1 = new ShapeDrawable(ovalShape1);

            shapeDrawable1.getPaint().setColor(0x10000000);//设置画笔的颜色

            shapeDrawable1.getPaint().setStyle(Paint.Style.FILL);//设置填充
            shapeDrawable1.getPaint().setAntiAlias(true);//设置抗锯齿
//黑色的唱片边框
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.play_border));
            roundedBitmapDrawable.setCircular(true);
            roundedBitmapDrawable.setAntiAlias(true);
//内层黑色边线
            OvalShape ovalShape2 = new OvalShape();
            ShapeDrawable shapeDrawable2 = new ShapeDrawable(ovalShape2);
            shapeDrawable2.getPaint().setColor(Color.BLACK);
            shapeDrawable2.getPaint().setAntiAlias(true);
            shapeDrawable2.getPaint().setStyle(Paint.Style.FILL);
            //内层图片
            RoundedBitmapDrawable roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(getResources(),bm);
            roundedBitmapDrawable1.setCircular(true);
            roundedBitmapDrawable.setAntiAlias(true);


            Drawable[] drawable = new Drawable[4];
            drawable[0] = shapeDrawable1;
            drawable[1] = roundedBitmapDrawable;
            drawable[2] = shapeDrawable2;
            drawable[3] = roundedBitmapDrawable1;
            LayerDrawable layerDrawable = new LayerDrawable(drawable);
            int width = 7;

            layerDrawable.setLayerInset(1, width * 1, width * 1, width * 1, width * 1);
            layerDrawable.setLayerInset(2, width * 11, width * 11, width * 11, width * 11);
            layerDrawable.setLayerInset(3, width * 12, width * 12, width * 12, width * 12);

            setDisView(getView().findViewById(R.id.myView));
            getDisView().setBackground(layerDrawable);
            /*handler.postDelayed(runnable,200 );*/



        }
    };
}
