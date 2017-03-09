package com.designers.kuwo.activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.designers.kuwo.R;
import com.designers.kuwo.utils.CustomApplication;
import com.designers.kuwo.utils.MusicPlayer;
import com.designers.kuwo.utils.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MusicSortActivity extends FragmentActivity {

    //返回键
    private ImageView back;
    //中间字
    private TextView toolbar_txt;

    //单曲
    private TextView txtSingle;
    //歌手
    private TextView txtSinger;
    //专辑
    private TextView txtAlbum;
    //文件夹
    private TextView txtFolder;
    //实现Tab滑动效果
    private ViewPager vPager;
    //动画图片
    private ImageView cursor;

    //动画图片偏移量
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int position_three;

    //动画图片宽度
    private int bmpW;

    //当前页卡编号
    private int currIndex = 0;

    //存放Fragment
    private ArrayList<Fragment> fragmentArrayList;

    //管理Fragment
    private FragmentManager fragmentManager;

    public Context context;

    //playbar中的一些组件
    private ImageView playimg;
    private ImageView playlist_menu;//播放列表按钮
    private TextView playbar_songName;
    private TextView playbar_singer;
    private ImageView playbar_play;
    private ImageView playbar_next;
    private RelativeLayout mainPlay;

    private SeekBar seekBar;

    private Dialog playListDialog;

    private PlayListAdapter playListAdapter;

    private CustomApplication customApplication;

    private MusicPlayer musicPlayer;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_sort);
        context = this;

        //设置全屏
        StatusBarCompat.compat(this);

        //初始化TextView
        InitTextView();

        //初始化playBar
        InitPlayBar();

        //初始化ImageView
        InitImageView();

        //初始化Fragment
        InitFragment();

        //初始化ViewPager
        InitViewPager();
        //初始化标题栏
        InitToolBar();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onResume() {

        updatabar.post(updatarun);

        if (CustomApplication.mediaPlayer.isPlaying()) {
            this.playbar_play.setImageResource(R.drawable.stop);
        } else {
            this.playbar_play.setImageResource(R.drawable.play);
        }
        super.onResume();
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {
        //single
        txtSingle = (TextView) findViewById(R.id.txtSingle);
        //singer
        txtSinger = (TextView) findViewById(R.id.txtSinger);
        //album
        txtAlbum = (TextView) findViewById(R.id.txtAlbum);
        //folder
        txtFolder = (TextView) findViewById(R.id.txtFolder);
        //click event
        txtSingle.setOnClickListener(new MFOnClickListener(0));
        txtSinger.setOnClickListener(new MFOnClickListener(1));
        txtAlbum.setOnClickListener(new MFOnClickListener(2));
        txtFolder.setOnClickListener(new MFOnClickListener(3));
    }

    /**
     * 初始化playbar
     */
    private void InitPlayBar() {
        playimg = (ImageView) findViewById(R.id.playimg);
        playbar_songName = (TextView) findViewById(R.id.playbar_songName);
        playbar_singer = (TextView) findViewById(R.id.playbar_singer);
        playbar_play = (ImageView) findViewById(R.id.playbar_play);
        playbar_next = (ImageView) findViewById(R.id.playbar_next);
        playlist_menu = (ImageView) findViewById(R.id.playlist_menu);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        mainPlay = (RelativeLayout) findViewById(R.id.mainPlay);

        mainPlay.setOnClickListener(new CilckEvent());
        playlist_menu.setOnClickListener(new CilckEvent());
        playbar_play.setOnClickListener(new CilckEvent());
        playbar_next.setOnClickListener(new CilckEvent());
    }

    /**
     * 初始化toolBar
     */
    private void InitToolBar() {
        back = (ImageView) findViewById(R.id.back);
        toolbar_txt = (TextView) findViewById(R.id.toolbar_txt);
        toolbar_txt.setText("本地音乐");
        back.setOnClickListener(new CilckEvent());
    }

    /**
     * 点击事件
     */
    private class CilckEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    intent = new Intent(MusicSortActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.playlist_menu:
                    playListDialog();
                    break;
                case R.id.playbar_next:
                    if (customApplication.getPlayList().size() > 0) {
                        musicPlayer = new MusicPlayer(MusicSortActivity.this, customApplication, playimg, playbar_play, playbar_songName, playbar_singer);
                        musicPlayer.next();
                    }
                    break;
                case R.id.playbar_play:
                    if (customApplication.getPlayList().size() > 0) {
                        musicPlayer = new MusicPlayer(MusicSortActivity.this, customApplication, playimg, playbar_play, playbar_songName, playbar_singer);
                        musicPlayer.play();
                    }
                    break;
                //点击播放栏
                case R.id.mainPlay:
                    if (customApplication.getPlayList().size() > 0) {
                        Intent intent = new Intent(MusicSortActivity.this, KuMuiscActivity.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
    }

    /**
     * init pagerTab content
     */
    private void InitViewPager() {

        vPager = (ViewPager) findViewById(R.id.vPager);
        //*********************vPager绑定适配器***********************//
        vPager.setAdapter(new MusicFragmentAdapter(fragmentManager, fragmentArrayList));

        ////让ViewPager缓存3个页面
        vPager.setOffscreenPageLimit(1);

        //设置当前它默认显示第一页
        vPager.setCurrentItem(0);

        //将顶部文字恢复默认值
        resetTextViewTextColor();

        txtSingle.setTextColor(getResources().getColor(R.color.colorPrimary));

        //设置viewpager页面滑动监听事件
        vPager.setOnPageChangeListener(new MFOnPageChangeListener());

    }

    /**
     * 初始化动画
     */

    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // 获取分辨率宽度
        int screenW = dm.widthPixels;  //屏幕宽度

        bmpW = (int) (screenW / 4);

        //设置动画图片宽度
        setBmpW(cursor, bmpW);
        offset = 0;

        //动画图片偏移量赋值
        position_one = (int) (screenW / 4.0);
        position_two = position_one * 2;
        position_three = position_one * 3;
    }

    /**
     * 初始化Fragment，并添加到ArrayList中
     */

    private void InitFragment() {

        fragmentArrayList = new ArrayList<Fragment>();
        fragmentArrayList.add(new SingleFragment());
        fragmentArrayList.add(new SingerFragment());
        fragmentArrayList.add(new AlbumFragment());
        fragmentArrayList.add(new FolderFragment());

        fragmentManager = getSupportFragmentManager();

    }

    /**
     * 头标点击监听
     */
    public class MFOnClickListener implements View.OnClickListener {

        private int index;

        public MFOnClickListener(int i) {
            this.index = i;
        }

        @Override
        public void onClick(View v) {
            vPager.setCurrentItem(index);
        }
    }

    /**
     * 页卡切换监听
     */
    public class MFOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position) {
                //当前为页卡1
                case 0:
                    //  page2 -> page1
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                        resetTextViewTextColor();
                        txtSingle.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else if (currIndex == 2) {
                        //  page3 -> page1
                        animation = new TranslateAnimation(position_two, 0, 0, 0);
                        resetTextViewTextColor();
                        txtSingle.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else if (currIndex == 3) {
                        //  page4 -> page1
                        animation = new TranslateAnimation(position_three, 0, 0, 0);
                        resetTextViewTextColor();
                        txtSingle.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    break;
                //当前为页卡2
                case 1:
                    //  page1 -> page2
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0, 0);
                        resetTextViewTextColor();
                        txtSinger.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else if (currIndex == 2) {
                        //  page3 -> page2
                        animation = new TranslateAnimation(position_two, position_one, 0, 0);
                        resetTextViewTextColor();
                        txtSinger.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else if (currIndex == 3) {
                        //  page4 -> page2
                        animation = new TranslateAnimation(position_three, position_one, 0, 0);
                        resetTextViewTextColor();
                        txtSinger.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    break;
                //当前为页卡3
                case 2:
                    //  page1 -> page3
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_two, 0, 0);
                        resetTextViewTextColor();
                        txtAlbum.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else if (currIndex == 1) {
                        //  page2 -> page3
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                        resetTextViewTextColor();
                        txtAlbum.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else if (currIndex == 3) {
                        //  page4 -> page3
                        animation = new TranslateAnimation(position_three, position_two, 0, 0);
                        resetTextViewTextColor();
                        txtAlbum.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    break;
                //当前为页卡4
                case 3:
                    //   page1 -> page4
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_three, 0, 0);
                        resetTextViewTextColor();
                        txtFolder.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else if (currIndex == 1) {
                        //   page2 -> page4
                        animation = new TranslateAnimation(position_one, position_three, 0, 0);
                        resetTextViewTextColor();
                        txtFolder.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else if (currIndex == 2) {
                        //   page3 -> page4
                        animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        resetTextViewTextColor();
                        txtFolder.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    break;

            }
            currIndex = position;

            animation.setFillAfter(true);//true:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    /**
     * 设置动画图片宽度
     */
    private void setBmpW(ImageView imageView, int mWidth) {
        ViewGroup.LayoutParams para;
        para = imageView.getLayoutParams();
        para.width = mWidth;
        imageView.setLayoutParams(para);
    }

    /**
     * 将顶部文字恢复默认值ֵ
     */
    private void resetTextViewTextColor() {

        txtSingle.setTextColor(getResources().getColor(R.color.music_sort_top_tab_color));
        txtSinger.setTextColor(getResources().getColor(R.color.music_sort_top_tab_color));
        txtAlbum.setTextColor(getResources().getColor(R.color.music_sort_top_tab_color));
        txtFolder.setTextColor(getResources().getColor(R.color.music_sort_top_tab_color));
    }

    Handler updatabar = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updatabar.post(updatarun);
        }
    };

    Runnable updatarun = new Runnable() {
        @Override
        public void run() {


            double currentPosition = CustomApplication.mediaPlayer.getCurrentPosition();
            double total = CustomApplication.mediaPlayer.getDuration();
            if (currentPosition == total) {
                seekBar.setProgress(0);
            } else {
                seekBar.setProgress((int) (currentPosition / total * 100));
            }

            customApplication = (CustomApplication) getApplication();
            /*int currentPosition = CustomApplication.mediaPlayer.getCurrentPosition();
            int total = CustomApplication.mediaPlayer.getDuration();
            seekBar.setMax(total);
            seekBar.setProgress(currentPosition);*/
            updatabar.postDelayed(updatarun, 1000);

            Map<String, Object> playingSong = customApplication.getPlayingSong();
            if (null != playingSong) {
                byte[] in = (byte[]) playingSong.get("songImage");
                Bitmap bm = BitmapFactory.decodeByteArray(in, 0, in.length);
                playimg.setImageBitmap(bm);
                playbar_songName.setText(playingSong.get("songName").toString());
                playbar_singer.setText(playingSong.get("singer").toString());
                if (CustomApplication.mediaPlayer.isPlaying()) {
                    playbar_play.setImageResource(R.drawable.stop);
                } else {
                    playbar_play.setImageResource(R.drawable.play);
                }
            }
        }
    };

    //播放列表适配器
    public class PlayListAdapter extends BaseAdapter {
        private Context context;
        private List<Map<String, Object>> listItems;
        private LayoutInflater inflater;

        public PlayListAdapter(Context context, List<Map<String, Object>> listItems) {
            this.context = context;
            this.listItems = listItems;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public Object getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.play_dialog_item, null);
                viewHolder.play_dialog_songName = (TextView) convertView.findViewById(R.id.play_dialog_songName);
                viewHolder.play_dialog_singer = (TextView) convertView.findViewById(R.id.play_dialog_singer);
                viewHolder.play_dialog_delete = (ImageButton) convertView.findViewById(R.id.play_dialog_delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.play_dialog_songName.setText(listItems.get(position).get("songName").toString());
            viewHolder.play_dialog_singer.setText(listItems.get(position).get("singer").toString());

            //按钮绑定点击事件

            return convertView;
        }

        private class ViewHolder {
            public TextView play_dialog_songName;
            public TextView play_dialog_singer;
            public ImageButton play_dialog_delete;
        }

    }

    //点击播放列表按钮弹出的对话框
    public void playListDialog() {
        playListDialog = new Dialog(this, R.style.BottomAddDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.play_dialog, null);
        ListView play_dialog_playlist = (ListView) root.findViewById(R.id.play_dialog_playlist);
        //customApplication.setPlayList(listItems);
        //MusicUtil.setObjectToShare(this, listItems, "playList");
        playListAdapter = new PlayListAdapter(this, customApplication.getPlayList());
        play_dialog_playlist.setAdapter(playListAdapter);
        play_dialog_playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playListDialog.dismiss();
            }
        });
        playListDialog.setContentView(root);
        Window dialogWindow = playListDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = 750;
        dialogWindow.setAttributes(lp);
        playListDialog.show();
    }

}
