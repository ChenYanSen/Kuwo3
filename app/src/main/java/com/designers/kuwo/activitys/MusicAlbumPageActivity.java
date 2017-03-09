package com.designers.kuwo.activitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.designers.kuwo.R;
import com.designers.kuwo.biz.AlbumBiz;
import com.designers.kuwo.biz.bizimpl.AlbumBizImpl;
import com.designers.kuwo.utils.CustomApplication;
import com.designers.kuwo.utils.MusicPlayer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by PC-CWB on 2017/1/11.
 */
public class MusicAlbumPageActivity extends Activity {


    private ImageView imgAlbumPage;
    private TextView txtAlbumName;
    private TextView txtAlbumDetial;
    private byte[] albumImage;
    private ListView listViewAlbumSong;
    private Intent intent;
    private AlbumBiz albumBiz;
    private List<Map<String, Object>> songFindByAlbums;
    private Adapter adapter;
    private CustomApplication customApplication;
    //playBar的组件
    private ImageView playimg;
    private ImageView playlist_menu;//播放列表按钮
    private TextView playbar_songName;
    private TextView playbar_singer;
    private ImageView playbar_play;
    private ImageView playbar_next;
    private RelativeLayout mainPlay;
    private Dialog playListDialog;
    private SeekBar seekBar;
    private PlayListAdapter playListAdapter;
    private MusicPlayer musicPlayer;
    //toolbar的组件
    private ImageView back;
    private TextView toolbar_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_album_page);
        customApplication = (CustomApplication) getApplication();
        InitToolBar();
        InitView();
        InitPlayBar();
        intent = getIntent();
        String albums = intent.getStringExtra("tvalbums");
        albumImage = intent.getByteArrayExtra("albumImage");
        this.txtAlbumName.setText(albums);
        this.toolbar_txt.setText("音乐专辑");
        Bitmap bm = BitmapFactory.decodeByteArray(albumImage, 0, albumImage.length);
        imgAlbumPage.setImageBitmap(bm);
        albumBiz = new AlbumBizImpl();
        songFindByAlbums = albumBiz.songFindAllByAlbums(MusicAlbumPageActivity.this, albums);
        Log.i("查询回来的歌手歌曲 ", "list Map 结构。。。" + songFindByAlbums.toString());
        adapter = new AlbumPageAdapter(MusicAlbumPageActivity.this);
        this.listViewAlbumSong.setAdapter((ListAdapter) adapter);

        this.listViewAlbumSong.setOnItemClickListener(new ItemClickEvent());

        //返回按钮
        this.back.setOnClickListener(new CilckEvent());
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

    private void InitView() {
        txtAlbumName = (TextView) findViewById(R.id.txtAlbumName);
        txtAlbumDetial = (TextView) findViewById(R.id.txtAlbumDetial);
        imgAlbumPage = (ImageView) findViewById(R.id.imgAlbumPage);
        listViewAlbumSong = (ListView) findViewById(R.id.listViewAlbumSong);
    }

    private void InitToolBar(){
        back= (ImageView) findViewById(R.id.back);
        toolbar_txt= (TextView) findViewById(R.id.toolbar_txt);
    }

    private class CilckEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    /*intent = new Intent(MusicAlbumPageActivity.this, MusicSortActivity.class);
                    startActivity(intent);*/
                    finish();
                    break;
                case R.id.playlist_menu:
                    playListDialog();
                    break;
                case R.id.playbar_next:
                    if(customApplication.getPlayList().size() > 0) {
                        musicPlayer = new MusicPlayer(MusicAlbumPageActivity.this, customApplication, playimg, playbar_play,
                                playbar_songName, playbar_singer);
                        musicPlayer.next();
                    }
                    break;
                case R.id.playbar_play:
                    if(customApplication.getPlayList().size() > 0) {
                        musicPlayer = new MusicPlayer(MusicAlbumPageActivity.this, customApplication, playimg, playbar_play, playbar_songName, playbar_singer);
                        musicPlayer.play();
                    }
                    break;
                //点击播放栏
                case R.id.mainPlay:
                    if (customApplication.getPlayList().size() > 0) {
                        Intent intent = new Intent(MusicAlbumPageActivity.this, KuMuiscActivity.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
    }

    class ItemClickEvent implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Map<String, Object> song = songFindByAlbums.get(position);
            Log.i("song", "song==:" + song.get("songName").toString());
            byte[] in = (byte[]) song.get("songImage");
            Bitmap bm = BitmapFactory.decodeByteArray(in, 0, in.length);
            playimg.setImageBitmap(bm);
            playbar_songName.setText(song.get("songName").toString());
            playbar_singer.setText(song.get("singer").toString());
            customApplication.setPlayingSong(song);
            customApplication.setPlayList(songFindByAlbums);

            musicPlayer = new MusicPlayer(getApplicationContext(), customApplication, playimg, playbar_play,
                    playbar_songName, playbar_singer);
            try {
                CustomApplication.mediaPlayer.reset();
                CustomApplication.mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(song.get("songUri").toString()));
                CustomApplication.mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            musicPlayer.play();
        }
    }


    private class AlbumPageAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;

        public AlbumPageAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return songFindByAlbums.size();
        }

        @Override
        public Object getItem(int position) {
            return songFindByAlbums.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (null == convertView) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_singersong, null);
                holder.linearLayoutItemSinger = (LinearLayout) convertView.findViewById(R.id.LinearLayoutItemSinger);
                holder.txtItemMainSong = (TextView) convertView.findViewById(R.id.txtItemMainSong);
                holder.txtItemSingerSong = (TextView) convertView.findViewById(R.id.txtItemSingerSong);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //赋值
            if (position % 2 == 1) {
                holder.linearLayoutItemSinger.setBackgroundColor(getResources().getColor(R.color.musicform_listitem_color1));
            } else {
                holder.linearLayoutItemSinger.setBackgroundColor(getResources().getColor(R.color.musicform_listitem_color2));
            }
            holder.txtItemMainSong.setText(songFindByAlbums.get(position).get("singer").toString());
            holder.txtItemSingerSong.setText(songFindByAlbums.get(position).get("songName").toString());
            return convertView;
        }

        private class ViewHolder {
            private TextView txtItemSingerSong, txtItemMainSong;
            private LinearLayout linearLayoutItemSinger;
        }
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
           /* int currentPosition = CustomApplication.mediaPlayer.getCurrentPosition();
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
