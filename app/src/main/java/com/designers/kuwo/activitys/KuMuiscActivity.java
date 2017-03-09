package com.designers.kuwo.activitys;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.designers.kuwo.R;
import com.designers.kuwo.eneity.ICollection;
import com.designers.kuwo.utils.Animator;
import com.designers.kuwo.utils.CustomApplication;
import com.designers.kuwo.utils.FastBlurUtil;
import com.designers.kuwo.utils.ILrcViewListener;
import com.designers.kuwo.utils.LrcProcess;
import com.designers.kuwo.utils.LrcRow;
import com.designers.kuwo.utils.LrcView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class KuMuiscActivity extends FragmentActivity {

    private int[] imgId = {R.drawable.l1, R.drawable.l2, R.drawable.l3};
    private String[] data = {"设置来电铃声", "设置闹钟铃声", "设置通知铃声"};

    private ImageView back;
    private TextView musicPlayBarName;
    private TextView musicPlayBarAuthor;
    private ViewPager viewPager;
    private View disView;//ViewPager_1中的中间部分
    private ImageView imageView;//ViewPager_1中的needle部分
    private Animator animator;//旋转动画
    private View viewPager1, viewPager2, viewPager3;
    private List<View> viewList;
    private LrcView lrcView;


    private ImageView previousMusic;//上一首歌曲
    private ImageView playMusic;//播放歌曲
    private ImageView nextMusic;//下一首歌曲
    private ImageView listMusic;//播放列表
    private ImageView state;//播放状态的按钮
    private ImageView loveImage;//收藏的按钮
    private ImageView setImage;//设置的按钮
    private SeekBar seekBar;//进度条
    private Dialog playListDialog;
    private PlayListAdapter playListAdapter;
    private CustomApplication customApplication;
    private Map<String, Object> playingSong;
    private Bitmap bm;
    private byte[] in;
    private Thread getLrctherd;

    public static int DestoryFlag = 0;
    private int loveImageFlag = 0;//为 收藏按钮设置初始值， 便于切换
    private int stateFlag = 0;//为播放状态设置按钮， 便于切换
    private int playFlag = 0;//为播放设置初始值，便于切换
    private int pauseFlag = 0;//为暂停设置初始值

    private List<Map<String, Object>> musicMessages;//用于装歌曲信息的List,与 下面的initData()中拿出来的，目前是用sdcard读所有歌曲，别忘了以后从数据库读时候，用initData（）方法啊；!!!
    private MusicMessage musicMsg;//别忘了啊 ！！！

    private int p;
    private TextView endTime;
    private TextView beginTime;
    private int maxMusic;
    private static int indexTime = 0;
    private int currentPosition;//当前时间
    private int duration;//总时间
    private LrcProcess lrcProcess;//用于歌词处理
    private List<LrcRow> lrcList = new ArrayList<>();//存放歌词立标对象
    private ICollection iCollection;
    private RelativeLayout kuMusicBackground;
    private Bitmap scaledBitmap;
    private Bitmap blurBitMap;
    private BitmapDrawable drawable;
    private byte[] bytes;

    // Activity传值到 Fragment中的方法：在Avtivity中 为值提供GetSet方法， 通过一定路径获得该值，在该Activity中set，  在Fragment中使用(activity)getActicity.get即可把值传过来。
    public List<LrcRow> getLrcList() {
        return lrcList;
    }

    public void setLrcList(List<LrcRow> lrcList) {
        this.lrcList = lrcList;
    }


    public static int getIndexTime() {
        return indexTime;
    }

    public static void setIndexTime(int indexTime) {
        KuMuiscActivity.indexTime = indexTime;
    }

    private static final int STARTPOSITION = 0;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ku_muisc);
        //初始化各个组件：
        back = (ImageView) findViewById(R.id.back);
        musicPlayBarName = (TextView) findViewById(R.id.musicPlayBarName);
        musicPlayBarAuthor = (TextView) findViewById(R.id.musicPlayBarAuthor);
        endTime = (TextView) findViewById(R.id.endTime);
        beginTime = (TextView) findViewById(R.id.begintime);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        previousMusic = (ImageView) findViewById(R.id.previousMusic);
        playMusic = (ImageView) findViewById(R.id.playMusic);
        nextMusic = (ImageView) findViewById(R.id.nextMusic);
        listMusic = (ImageView) findViewById(R.id.listMusic);
        state = (ImageView) findViewById(R.id.state);
        loveImage = (ImageView) findViewById(R.id.loveImg);
        setImage = (ImageView) findViewById(R.id.setImg);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        back = (ImageView) findViewById(R.id.back);
        kuMusicBackground = (RelativeLayout) findViewById(R.id.kuMusicBackground);
        customApplication = (CustomApplication) getApplication();

        //压缩图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play_border);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bytes = bos.toByteArray();

        LayoutInflater inflater = getLayoutInflater();
        viewPager1 = inflater.inflate(R.layout.activity_view_pager_1, null);
        viewPager2 = inflater.inflate(R.layout.activity_view_pager_2, null);
        lrcView = (LrcView) viewPager2.findViewById(R.id.lrcShowView);
        imageView = (ImageView) viewPager1.findViewById(R.id.needle);
        disView = viewPager1.findViewById(R.id.myView);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(viewPager1);
        viewList.add(viewPager2);

        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));


                return viewList.get(position);
            }

        };
        viewPager.setAdapter(pagerAdapter);

        musicMessages = customApplication.getPlayList();
        //进入页面时候加载第一首歌：
         /*   initLrc(musicMessages.get(0).getMusicPath());*/

        try {
            playingSong = customApplication.getPlayingSong();
            in = (byte[]) playingSong.get("songImage");
            bm = BitmapFactory.decodeByteArray(in, 0, in.length);

            scaledBitmap = Bitmap.createScaledBitmap(bm, bm.getWidth() / 20, bm.getHeight() / 20, false);
            blurBitMap = FastBlurUtil.doBlur(scaledBitmap, 8, true);
            drawable = new BitmapDrawable(blurBitMap);
            kuMusicBackground.setBackground(drawable);

            musicPlayBarAuthor.setText(playingSong.get("singer").toString());
            musicPlayBarName.setText(playingSong.get("songName").toString());
            viewPager1animator(bm);
            animator = new Animator(imageView, disView);
            if (CustomApplication.mediaPlayer.isPlaying()) {
                animator.startMusicAnimation();
                //改变播放按钮的图标
                playMusic.setImageResource(R.drawable.ic_media_pause_2);
                //改变playFlag状态值
                playFlag = 1;
            } else {
                playFlag = 0;
                playMusic.setImageResource(R.drawable.ic_media_play_3);

            }

            //拖动歌词 seekBar变动

            updatabar.post(updatarun);
            for (int i = 0; i < musicMessages.size(); i++) {
                if (musicMessages.get(i) == playingSong) {
                    p = i;
                }
            }
            String currentMusicPath = playingSong.get("songUri").toString();
            String currentMusicName = playingSong.get("songName").toString();
            String currenrMusicAuthor = playingSong.get("singer").toString();
            String currentMusicLrcPath = currentMusicPath.substring(0, currentMusicPath.lastIndexOf("/")) + "/" + currentMusicName + ".lrc";
            getLrctherd = new Thread(new GetLyricFromInternet(currentMusicName, currentMusicLrcPath, currenrMusicAuthor));
            File lrcfile = new File(currentMusicLrcPath);
            if (!lrcfile.exists()) {
                Log.i("aaa", "开始查询歌词");
                getLrctherd.start();

            } else {
                Log.i("aaa", "歌词已存在");
                initLrc(currentMusicLrcPath);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        lrcView.setListener(new ILrcViewListener() {
            //当歌词被用户上下拖动的时候回调该方法,从高亮的那一句歌词开始播放
            public void onLrcSeeked(int newPosition, LrcRow row) {
                if (CustomApplication.mediaPlayer != null) {
                    CustomApplication.mediaPlayer.seekTo((int) row.time);
                }
            }
        });

        //Musicd点击事件色设置,设置按钮，收藏按钮，状态按钮时间
        back.setOnClickListener(new MusicOcl());
        nextMusic.setOnClickListener(new MusicOcl());
        previousMusic.setOnClickListener(new MusicOcl());
        playMusic.setOnClickListener(new MusicOcl());
        loveImage.setOnClickListener(new MusicOcl());
        state.setOnClickListener(new MusicOcl());
        CustomApplication.mediaPlayer.setOnCompletionListener(new MusicOncomplete());
        setImage.setOnClickListener(new StateMusicOcL());
        //seekBar 点击事件设置
        seekBar.setOnSeekBarChangeListener(new SeekBarOcl());
        updatabar.post(updatarun);
        //listMusic音乐清单设置
        listMusic.setOnClickListener(new ListMusicOcl());
    }


    // SeekBar点击事件
    class SeekBarOcl implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                CustomApplication.mediaPlayer.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    // 播放暂停  上一首下一首设置
    class MusicOcl implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.back:
                    finish();
                    break;

                case R.id.playMusic:
                    if (playFlag == 0) {
                        //开始播放歌曲
                        CustomApplication.mediaPlayer.start();
                        //开始播放动画
                        if (pauseFlag == 1) {
                            animator.resumeMusicAnimation();
                        } else {
                            animator.startMusicAnimation();
                        }
                        //改变播放按钮的图标
                        playMusic.setImageResource(R.drawable.ic_media_pause_2);
                        //改变playFlag状态值
                        playFlag = 1;

                        //调用子线程，从而设置seekBar的进度和播放完成时的处理动作
                        updatabar.post(updatarun);
                    } else {
                        //暂停歌曲
                        CustomApplication.mediaPlayer.pause();
                        //暂停播放动画
                        animator.pauseMusicAnimation();
                        //改变playFlag=0,让其处于待播放状态
                        playFlag = 0;
                        pauseFlag = 1;
                        //设置播放按钮图标
                        playMusic.setImageResource(R.drawable.ic_media_play_3);
                    }

                    break;
                case R.id.nextMusic:
                    //当进入 APP时候，点击下一曲，并没有创建动画，在animator为Null时候，创建动画
                    animator.stopMusicAnimation();

                    if (p == musicMessages.size() - 1) {
                        p = 0;
                        CustomApplication.mediaPlayer.reset();
                        playMusicFunction(CustomApplication.mediaPlayer, p);
                        playMusic.setImageResource(R.drawable.ic_media_pause_2);
                    } else {
                        p++;
                        CustomApplication.mediaPlayer.reset();
                        playMusicFunction(CustomApplication.mediaPlayer, p);
                        playMusic.setImageResource(R.drawable.ic_media_pause_2);
                    }
                    in = (byte[]) musicMessages.get(p).get("songImage");

                    bm = BitmapFactory.decodeByteArray(in, 0, in.length);
                    viewPager1animator(bm);
                    scaledBitmap = Bitmap.createScaledBitmap(bm, bm.getWidth() / 20, bm.getHeight() / 20, false);
                    blurBitMap = FastBlurUtil.doBlur(scaledBitmap, 8, true);
                    drawable = new BitmapDrawable(blurBitMap);
                    kuMusicBackground.setBackground(drawable);
                    animator.startMusicAnimation();
                    updatabar.post(updatarun);
                    nextSearchLrc(p);
                    Toast.makeText(KuMuiscActivity.this, "下一曲", Toast.LENGTH_LONG).show();
                    playFlag = 1;
                    customApplication.setPlayingSong(musicMessages.get(p));
                    playingSong = customApplication.getPlayingSong();
                    break;
                case R.id.previousMusic:
                    //当进入 APP时候，点击上一曲，并没有创建动画，在animator为Null时候，创建动画
                    animator.stopMusicAnimation();
                    if (p == 0) {

                        p = musicMessages.size() - 1;
                        CustomApplication.mediaPlayer.reset();
                        playMusicFunction(CustomApplication.mediaPlayer, p);
                        playMusic.setImageResource(R.drawable.ic_media_pause_2);
                    } else {
                        p--;
                        CustomApplication.mediaPlayer.reset();
                        playMusicFunction(CustomApplication.mediaPlayer, p);
                        playMusic.setImageResource(R.drawable.ic_media_pause_2);
                    }
                    in = (byte[]) musicMessages.get(p).get("songImage");

                    bm = BitmapFactory.decodeByteArray(in, 0, in.length);
                    scaledBitmap = Bitmap.createScaledBitmap(bm, bm.getWidth() / 20, bm.getHeight() / 20, false);
                    blurBitMap = FastBlurUtil.doBlur(scaledBitmap, 8, true);
                    drawable = new BitmapDrawable(blurBitMap);
                    kuMusicBackground.setBackground(drawable);
                    viewPager1animator(bm);
                    animator.startMusicAnimation();
                    updatabar.post(updatarun);

                    Toast.makeText(KuMuiscActivity.this, "上一曲", Toast.LENGTH_LONG).show();
                    nextSearchLrc(p);
                    //playFlag改变为1，使处于待等待状态。
                    playFlag = 1;
                    customApplication.setPlayingSong(musicMessages.get(p));
                    break;
                case R.id.state:
                    if (stateFlag == 0) {
                        state.setImageResource(R.drawable.single_cycle_play_2);
                        Toast.makeText(KuMuiscActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();
                        customApplication.setPattern(1);
                        stateFlag = 1;
                    } else if (stateFlag == 1) {
                        state.setImageResource(R.drawable.radom_play_2);
                        Toast.makeText(KuMuiscActivity.this, "随机播放", Toast.LENGTH_SHORT).show();
                        stateFlag = 2;
                        customApplication.setPattern(2);

                    } else if (stateFlag == 2) {
                        state.setImageResource(R.drawable.ic_menu_rotate_2);
                        Toast.makeText(KuMuiscActivity.this, "列表循环", Toast.LENGTH_SHORT).show();
                        stateFlag = 0;
                        customApplication.setPattern(0);
                    }
                    break;
                case R.id.loveImg:
                    if (loveImageFlag == 0) {
                        //收藏
                        loveImage.setImageResource(R.drawable.btn_star_on_normal_holo_dark_1);
                        loveImageFlag = 1;
                       /* iCollection = new ICollection();
                        iCollection.setSinger(musicMessages.get(p).getMusicAuthor());
                        iCollection.setSongName(musicMessages.get(p).getMusicName());
                        iCollection.setSongUri(musicMessages.get(p).getMusicPath());
                        iCollection.setAccount("aaa");
                        CollectionBiz collectionBiz = new CollectionBizImp();
                        collectionBiz.alterCollection(iCollection, KuMuiscActivity.this);
                        Log.i("aaa", "收藏成功");
*/

                    } else {
                        //取消收藏
                        loveImage.setImageResource(R.drawable.btn_star_off_disabled_holo_dark_2);
                        loveImageFlag = 0;
                       /* CollectionBiz collectionBiz = new CollectionBizImp();
                        collectionBiz.removeCollection(musicMessages.get(p).getMusicName(), musicMessages.get(p).getMusicAuthor(), "aaa", KuMuiscActivity.this);*/
                    }

                    break;


            }


        }

    }


    class ListMusicOcl implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            playListDialog();


        }
    }

    class StateMusicOcL implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            LayoutInflater inflater = (LayoutInflater) KuMuiscActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_list_music_state, null);
            ListView listView_state = (ListView) layout.findViewById(R.id.listState);

            SimpleAdapter simpleAdapter = new SimpleAdapter(KuMuiscActivity.this, getData(), R.layout.stateitem, new String[]{"imgId", "data"}, new int[]{R.id.stateItemImage, R.id.stateItemgText});
            listView_state.setAdapter(simpleAdapter);
            listView_state.setOnItemClickListener(new StateItem());
            Dialog dialog = new Dialog(KuMuiscActivity.this, R.style.BottomDialog);
            dialog.setContentView(layout);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.x = 0;
            layoutParams.y = 0;
            layoutParams.width = getResources().getDisplayMetrics().widthPixels;
            layout.measure(0, 0);
            layoutParams.height = 300;
            dialogWindow.setAttributes(layoutParams);
            dialog.show();

        }
    }

    private ArrayList<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < imgId.length; i++) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("imgId", imgId[i]);
            hashMap.put("data", data[i]);
            arrayList.add(hashMap);
        }
        return arrayList;

    }


    public void playMusicFunction(MediaPlayer mediaPlayer, int i) {
        try {
            mediaPlayer.setDataSource(KuMuiscActivity.this, Uri.parse(String.valueOf(musicMessages.get(i).get("songUri").toString())));
            mediaPlayer.prepare();
            maxMusic = mediaPlayer.getDuration();
            Date date = new Date(maxMusic);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

            endTime.setText(simpleDateFormat.format(date));
            musicPlayBarAuthor.setText(musicMessages.get(i).get("singer").toString());
            musicPlayBarName.setText(musicMessages.get(i).get("songName").toString());
            mediaPlayer.start();

            seekBar.setProgress(STARTPOSITION);
            seekBar.setMax(maxMusic);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    class StateItem implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    setRing(KuMuiscActivity.this, RingtoneManager.TYPE_RINGTONE, musicMessages.get(p).get("songUri").toString());
                    break;
                case 1:
                    setRing(KuMuiscActivity.this, RingtoneManager.TYPE_ALARM, musicMessages.get(p).get("songUri").toString());
                    break;
                case 2:
                    setRing(KuMuiscActivity.this, RingtoneManager.TYPE_NOTIFICATION, musicMessages.get(p).get("songUri").toString());
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    //更新seekbar
    Handler updatabar = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            updatabar.post(updatarun);

        }
    };
    Runnable updatarun = new Runnable() {
        @Override
        public void run() {

            currentPosition = CustomApplication.mediaPlayer.getCurrentPosition();
            Date date = new Date(currentPosition);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
            beginTime.setText(simpleDateFormat.format(date));
            duration = CustomApplication.mediaPlayer.getDuration();
            date = new Date(duration);
            simpleDateFormat = new SimpleDateFormat("mm:ss");
            endTime.setText(simpleDateFormat.format(date));
            //Log.i("KuMusicAcTIVITY", currentPosition + "");
            seekBar.setMax(duration);
            lrcView.setLrc(getLrcList());
            lrcView.seekLrcToTime(currentPosition);
            lrcView.invalidate();

            seekBar.setProgress(currentPosition);
            updatabar.postDelayed(updatarun, 50);
            //通过seekBar的当前进度与最大值比较，当相等时说明歌曲播放完，这时候停止动画 设置播放按钮的图标
            if (currentPosition == maxMusic) {

                while (CustomApplication.mediaPlayer.isPlaying()) {
                /*updatabar.removeCallbacks(updatarun);*/
                    //改变playFlag=0,让其处于待播放状态
                    playFlag = 0;
                    //设置播放按钮图标
                    playMusic.setImageResource(R.drawable.ic_media_play_3);

                    animator.stopMusicAnimation();
                }


            }

        }
    };


    class MusicOncomplete implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            switch (stateFlag) {
                case 1:
                    mp.reset();
                    playMusicFunction(mp, p);
                    playMusic.setImageResource(R.drawable.ic_media_pause_2);
                    customApplication.setPlayingSong(musicMessages.get(p));
                    updatabar.post(updatarun);
                    nextSearchLrc(p);
                    in = (byte[]) musicMessages.get(p).get("songImage");
                    bm = BitmapFactory.decodeByteArray(in, 0, in.length);
                    scaledBitmap = Bitmap.createScaledBitmap(bm, bm.getWidth() / 20, bm.getHeight() / 20, false);
                    blurBitMap = FastBlurUtil.doBlur(scaledBitmap, 8, true);
                    drawable = new BitmapDrawable(blurBitMap);
                    kuMusicBackground.setBackground(drawable);
                    viewPager1animator(bm);
                    animator.startMusicAnimation();
                    playFlag = 1;

                    break;
                case 2:
                    Random random = new Random();
                    p = random.nextInt(musicMessages.size());
                    mp.reset();
                    playMusicFunction(mp, p);
                    playMusic.setImageResource(R.drawable.ic_media_pause_2);
                    updatabar.post(updatarun);

                    nextSearchLrc(p);
                    customApplication.setPlayingSong(musicMessages.get(p));
                    in = (byte[]) musicMessages.get(p).get("songImage");
                    bm = BitmapFactory.decodeByteArray(in, 0, in.length);
                    scaledBitmap = Bitmap.createScaledBitmap(bm, bm.getWidth() / 20, bm.getHeight() / 20, false);
                    blurBitMap = FastBlurUtil.doBlur(scaledBitmap, 8, true);
                    drawable = new BitmapDrawable(blurBitMap);
                    kuMusicBackground.setBackground(drawable);
                    viewPager1animator(bm);
                    animator.startMusicAnimation();
                    playFlag = 1;
                    break;
                case 0:
                    if (p == musicMessages.size() - 1) {
                        p = 1;
                        CustomApplication.mediaPlayer.reset();
                        playMusicFunction(CustomApplication.mediaPlayer, p);
                        playMusic.setImageResource(R.drawable.ic_media_pause_2);
                    } else {
                        p++;
                        CustomApplication.mediaPlayer.reset();
                        playMusicFunction(CustomApplication.mediaPlayer, p);
                        playMusic.setImageResource(R.drawable.ic_media_pause_2);
                    }
                    in = (byte[]) musicMessages.get(p).get("songImage");

                    bm = BitmapFactory.decodeByteArray(in, 0, in.length);
                    viewPager1animator(bm);
                    scaledBitmap = Bitmap.createScaledBitmap(bm, bm.getWidth() / 20, bm.getHeight() / 20, false);
                    blurBitMap = FastBlurUtil.doBlur(scaledBitmap, 8, true);
                    drawable = new BitmapDrawable(blurBitMap);
                    kuMusicBackground.setBackground(drawable);
                    animator.startMusicAnimation();
                    updatabar.post(updatarun);
                    nextSearchLrc(p);
                    //Toast.makeText(KuMuiscActivity.this, "下一曲", Toast.LENGTH_LONG).show();
                    playFlag = 1;
                    customApplication.setPlayingSong(musicMessages.get(p));
                    playingSong = customApplication.getPlayingSong();

                    break;
            }

        }
    }


    public void initLrc(String path) throws IOException {
        LrcProcess lrcProcess = new LrcProcess();
        lrcProcess.readLrc(path);
        lrcProcess.getLrcList();

        //将得到的歌词集合传给mLrcView用来展示
        setLrcList(lrcProcess.getLrcList());


    }

    public static void setRing(Context context, int type, String path) {

        Uri oldRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE); //系统当前  通知铃声
        Uri oldNotification = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION); //系统当前  通知铃声
        Uri oldAlarm = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM); //系统当前  闹钟铃声

        File sdfile = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, sdfile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, sdfile.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.IS_ALARM, true);
        values.put(MediaStore.Audio.Media.IS_MUSIC, true);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(sdfile.getAbsolutePath());
        Uri newUri = null;
        String deleteId = "";
        try {
            Cursor cursor = context.getContentResolver().query(uri, null, MediaStore.MediaColumns.DATA + "=?", new String[]{path}, null);
            if (cursor.moveToFirst()) {
                deleteId = cursor.getString(cursor.getColumnIndex("_id"));
            }


            context.getContentResolver().delete(uri,
                    MediaStore.MediaColumns.DATA + "=\"" + sdfile.getAbsolutePath() + "\"", null);
            newUri = context.getContentResolver().insert(uri, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (newUri != null) {

            String ringStoneId = "";
            String notificationId = "";
            String alarmId = "";
            if (null != oldRingtoneUri) {
                ringStoneId = oldRingtoneUri.getLastPathSegment();
            }

            if (null != oldNotification) {
                notificationId = oldNotification.getLastPathSegment();
            }

            if (null != oldAlarm) {
                alarmId = oldAlarm.getLastPathSegment();
            }

            Uri setRingStoneUri;
            Uri setNotificationUri;
            Uri setAlarmUri;

            if (type == RingtoneManager.TYPE_RINGTONE || ringStoneId.equals(deleteId)) {
                setRingStoneUri = newUri;

            } else {
                setRingStoneUri = oldRingtoneUri;
            }

            if (type == RingtoneManager.TYPE_NOTIFICATION || notificationId.equals(deleteId)) {
                setNotificationUri = newUri;

            } else {
                setNotificationUri = oldNotification;
            }

            if (type == RingtoneManager.TYPE_ALARM || alarmId.equals(deleteId)) {
                setAlarmUri = newUri;

            } else {
                setAlarmUri = oldAlarm;
            }

            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, setRingStoneUri);
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION, setNotificationUri);
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM, setAlarmUri);

            switch (type) {
                case RingtoneManager.TYPE_RINGTONE:
                    Toast.makeText(context.getApplicationContext(), "设置来电铃声成功！", Toast.LENGTH_SHORT).show();
                    break;
                case RingtoneManager.TYPE_NOTIFICATION:
                    Toast.makeText(context.getApplicationContext(), "设置通知铃声成功！", Toast.LENGTH_SHORT).show();
                    break;
                case RingtoneManager.TYPE_ALARM:
                    Toast.makeText(context.getApplicationContext(), "设置闹钟铃声成功！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


    /**
     * 从assets目录下读取歌词文件内容
     *
     * @param fileName
     * @return
     */
    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String result = "";
            while ((line = bufReader.readLine()) != null) {
                if (line.trim().equals(""))
                    continue;
                result += line + "\r\n";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DestoryFlag = 1;

    }

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
        playListAdapter = new PlayListAdapter(KuMuiscActivity.this, customApplication.getPlayList());
        play_dialog_playlist.setAdapter(playListAdapter);
        play_dialog_playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                p = position;
                if (CustomApplication.mediaPlayer.isPlaying()) {
                    animator.stopMusicAnimation();
                    CustomApplication.mediaPlayer.reset();
                    playMusicFunction(CustomApplication.mediaPlayer, p);
                    playMusic.setImageResource(R.drawable.ic_media_pause_2);
                    playFlag = 1;
                } else {

                    CustomApplication.mediaPlayer.reset();
                    playMusicFunction(CustomApplication.mediaPlayer, p);
                    playMusic.setImageResource(R.drawable.ic_media_pause_2);
                    playFlag = 1;
                }
                updatabar.post(updatarun);
                nextSearchLrc(p);

                customApplication.setPlayingSong(musicMessages.get(p));
                in = (byte[]) musicMessages.get(p).get("songImage");

                bm = BitmapFactory.decodeByteArray(in, 0, in.length);
                scaledBitmap = Bitmap.createScaledBitmap(bm, bm.getWidth() / 20, bm.getHeight() / 20, false);
                blurBitMap = FastBlurUtil.doBlur(scaledBitmap, 8, true);
                drawable = new BitmapDrawable(blurBitMap);
                kuMusicBackground.setBackground(drawable);
                viewPager1animator(bm);
                animator.startMusicAnimation();
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

    class GetLyricFromInternet implements Runnable {
        private String music_lyricid = null;
        private String music_title;
        private String music_path;
        private String music_author;

        public GetLyricFromInternet(String musicName, String musicpath, String musicAuthor) {
            this.music_title = musicName;
            this.music_path = musicpath;
            this.music_author = musicAuthor;


        }

        @Override
        public void run() {

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.search.catalogSug&query=" + music_title).build();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                music_lyricid = parseXMLWithpull(responseData, music_title, music_author);

                OkHttpClient clientlyric = new OkHttpClient();
                Request requestlyric = new Request.Builder().url("http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.song.lry&songid=" + music_lyricid).build();
                Response responselyric = clientlyric.newCall(requestlyric).execute();
                String responselyricdata = responselyric.body().string();
                ReaderToFile(responselyricdata, music_path, music_title);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String parseXMLWithpull(String xmlData, String music_title, String music_author) {
        String datalrc = null;
        Log.i("text", xmlData);
        try {
            JSONObject jsonObject = new JSONObject(xmlData);
            String jsondata = jsonObject.getString("song");
            JSONArray jsonArray = new JSONArray(jsondata);
            for (int i = 0; i < jsonArray.length(); i++) {
                org.json.JSONObject raw = jsonArray.getJSONObject(i);
                if (music_title.equals(raw.getString("songname")) && music_author.equals(raw.getString("artistname"))) {
                    datalrc = raw.getString("songid");
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return datalrc;
    }

    public void ReaderToFile(String jsondata, String musicPath, String musicName) {
        try {
            JSONObject jsonArray = new JSONObject(jsondata);
            String content = jsonArray.getString("lrcContent");
            WriteToLoad(content, musicPath, musicName);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void WriteToLoad(String context, String musicPath, String musicName) {
        String filename = musicPath.substring(0, musicPath.lastIndexOf("/"));
        File filelyric = new File(filename + "/" + musicName + ".lrc");
        if (!filelyric.exists()) {
            try {
                filelyric.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                FileOutputStream fs = new FileOutputStream(filelyric);
                byte[] bytes = context.getBytes();
                fs.write(bytes);
                fs.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            String message = "";
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(filelyric);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] by = new byte[1024];
            int count = 0;
            try {
                while ((count = fis.read(by)) > 0) {
                    message = new String(by, 0, count);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (message.equals("")) {

                try {
                    FileOutputStream fs = new FileOutputStream(filelyric);
                    byte[] bytes = context.getBytes();
                    fs.write(bytes);
                    fs.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    public void nextSearchLrc(int position) {
        String currentMusicPath = musicMessages.get(position).get("songUri").toString();
        String currentMusicName = musicMessages.get(position).get("songName").toString();
        String currenrMusicAuthor = musicMessages.get(position).get("singer").toString().toString();
        Log.i("currentMusicLrcPath", currentMusicPath);
        String currentMusicLrcPath = currentMusicPath.substring(0, currentMusicPath.lastIndexOf("/")) + "/" + currentMusicName + ".lrc";
        Log.i("currentMusicLrcPath", currentMusicLrcPath);
        getLrctherd = new Thread(new GetLyricFromInternet(currentMusicName, currentMusicLrcPath, currenrMusicAuthor));

        File lrcfile = new File(currentMusicLrcPath);
        if (!lrcfile.exists()) {
            Log.i("aaa", "开始查询歌词");
            getLrctherd.start();

        } else {
            Log.i("aaa", "歌词已存在");
            try {
                initLrc(currentMusicLrcPath);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    public void viewPager1animator(Bitmap bm) {

        //外层透明色
        OvalShape ovalShape1 = new OvalShape();
        ShapeDrawable shapeDrawable1 = new ShapeDrawable(ovalShape1);

        shapeDrawable1.getPaint().setColor(0x10000000);//设置画笔的颜色

        shapeDrawable1.getPaint().setStyle(Paint.Style.FILL);//设置填充
        shapeDrawable1.getPaint().setAntiAlias(true);//设置抗锯齿
//黑色的唱片边框
        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play_border);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bytes = bos.toByteArray();*/
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        roundedBitmapDrawable.setCircular(true);
        roundedBitmapDrawable.setAntiAlias(true);
//内层黑色边线
        OvalShape ovalShape2 = new OvalShape();
        ShapeDrawable shapeDrawable2 = new ShapeDrawable(ovalShape2);
        shapeDrawable2.getPaint().setColor(Color.BLACK);
        shapeDrawable2.getPaint().setAntiAlias(true);
        shapeDrawable2.getPaint().setStyle(Paint.Style.FILL);
        //内层图片
        RoundedBitmapDrawable roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(getResources(), bm);
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

        disView.setBackground(layerDrawable);


    }


}



