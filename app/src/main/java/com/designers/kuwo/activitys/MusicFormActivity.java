package com.designers.kuwo.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.designers.kuwo.R;
import com.designers.kuwo.biz.SongListBiz;
import com.designers.kuwo.biz.bizimpl.SongListBizImpl;
import com.designers.kuwo.eneity.SongList;
import com.designers.kuwo.utils.CustomApplication;
import com.designers.kuwo.utils.MusicPlayer;
import com.designers.kuwo.utils.StatusBarCompat;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 我的歌单列表
 */
public class MusicFormActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private List<Map<String, Object>> listItems;
    private ListView musicform_listview;
    private MusicFromListViewAdapter adapter;
    private PlayListAdapter playListAdapter;
    private ImageView showlist_btn;//点击不显示ListView
    private LinearLayout buildform;//点击新建歌单按钮
    private TextView musicform_num;//歌单的数量
    private ImageView back;
    private ImageView playimg;
    private ImageView playlist_menu;//播放列表按钮
    private TextView playbar_songName;
    private TextView playbar_singer;
    private ImageView playbar_play;
    private ImageView playbar_next;
    private SeekBar seekBar;
    private Dialog playListDialog;
    private int oldPosition = -1;//用来记录上一次所点按钮的位置
    private int totalHight = 0;//用来保存李listview的总高度
    private boolean once = false;
    private RelativeLayout mainPlay;

    private CustomApplication customApplication;
    private String userName;
    private SongListBiz songListBiz = new SongListBizImpl();
    private Map<String, Object> songList;
    private MusicPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicform);

        customApplication = (CustomApplication) getApplication();
        userName = customApplication.getUserName();
        back = (ImageView) findViewById(R.id.back);
        musicform_listview = (ListView) findViewById(R.id.musicform_listview);
        showlist_btn = (ImageView) findViewById(R.id.showlist_btn);
        buildform = (LinearLayout) findViewById(R.id.buildform);
        musicform_num = (TextView) findViewById(R.id.musicform_num);
        playimg = (ImageView) findViewById(R.id.playimg);
        playlist_menu = (ImageView) findViewById(R.id.playlist_menu);
        playbar_songName = (TextView) findViewById(R.id.playbar_songName);
        playbar_singer = (TextView) findViewById(R.id.playbar_singer);
        playbar_play = (ImageView) findViewById(R.id.playbar_play);
        playbar_next = (ImageView) findViewById(R.id.playbar_next);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        mainPlay = (RelativeLayout) findViewById(R.id.mainPlay);

        mainPlay.setOnClickListener(this);
        back.setOnClickListener(this);
        showlist_btn.setOnClickListener(this);
        buildform.setOnClickListener(this);
        playlist_menu.setOnClickListener(this);
        playbar_play.setOnClickListener(this);
        playbar_next.setOnClickListener(this);
        musicform_listview.setOnItemClickListener(this);

        //全屏
        StatusBarCompat.compat(this);
    }

    public List<Map<String, Object>> init() {
        List<Map<String, Object>> items = songListBiz.findNameAndSize(this, userName);
        return items;
    }

    //歌单item点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        songList = listItems.get(position);
        Intent intent = new Intent();
        intent.putExtra("songListName", songList.get("name").toString());
        intent.setClass(this, FromMusicActivity.class);
        startActivity(intent);
    }

    //页面按钮点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.playlist_menu:
                playListDialog();
                break;

            //点击新建歌单
            case R.id.buildform:
                addDialog();
                break;

            //点击不显示listview
            case R.id.showlist_btn:
                if (!once) {
                    musicform_listview.setVisibility(View.GONE);
                    showlist_btn.setImageResource(R.drawable.more);
                    once = true;
                } else {
                    musicform_listview.setVisibility(View.VISIBLE);
                    showlist_btn.setImageResource(R.drawable.less);
                    once = false;
                }
                break;

            case R.id.playbar_play:
                if (customApplication.getPlayList().size() > 0) {
                    musicPlayer = new MusicPlayer(this, customApplication, playimg, playbar_play,
                            playbar_songName, playbar_singer);
                    musicPlayer.play();
                }
                break;

            case R.id.playbar_next:
                if (customApplication.getPlayList().size() > 0) {
                    musicPlayer = new MusicPlayer(this, customApplication, playimg, playbar_play,
                            playbar_songName, playbar_singer);
                    musicPlayer.next();
                }
                break;

            case R.id.mainPlay:
                if (customApplication.getPlayList().size() > 0) {
                    Intent intent = new Intent(this, KuMuiscActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    //歌单列表的适配器
    public class MusicFromListViewAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        public MusicFromListViewAdapter(Context context) {
            this.context = context;
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
            Map map = listItems.get(position);
            ViewHolder viewHolder = null;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.musicform_item_layout, null);
                viewHolder.itemMusicForm_name_txt = (TextView) convertView.findViewById(R.id.itemMusicForm_name_txt);
                viewHolder.itemMusicForm_num_txt = (TextView) convertView.findViewById(R.id.itemMusicForm_num_txt);
                viewHolder.itemMusicForm_btn = (ImageButton) convertView.findViewById(R.id.itemMusicForm_btn);
                viewHolder.expend = (LinearLayout) convertView.findViewById(R.id.expend);
                viewHolder.musicform_expend_edit = (LinearLayout) convertView.findViewById(R.id.musicform_expend_edit);
                viewHolder.musicform_expend_delete = (LinearLayout) convertView.findViewById(R.id.musicform_expend_delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据expend值判断是否展开
            if ((boolean) map.get("expend")) {
                viewHolder.expend.setVisibility(View.VISIBLE);
            } else {
                viewHolder.expend.setVisibility(View.GONE);
            }
            viewHolder.itemMusicForm_name_txt.setText(map.get("name").toString());
            viewHolder.itemMusicForm_num_txt.setText(map.get("size").toString());
            //按钮绑定点击事件
            viewHolder.itemMusicForm_btn.setOnClickListener(new ViewOCL(position));
            viewHolder.musicform_expend_edit.setOnClickListener(new ViewOCL(position));
            viewHolder.musicform_expend_delete.setOnClickListener(new ViewOCL(position));
            return convertView;
        }

        private class ViewOCL implements View.OnClickListener {
            //记录所点button的位置
            private int position;
            private Map map;
            private boolean expend;

            public ViewOCL(int position) {
                this.position = position;
            }

            public ViewOCL() {
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    //歌单item上btn的点击事件
                    case R.id.itemMusicForm_btn:

                        songList = listItems.get(position);

                        map = listItems.get(position);
                        expend = (boolean) map.get("expend");
                        if (oldPosition == position) {
                            if (expend) {
                                oldPosition = -1;
                            }
                            map.put("expend", !expend);
                        } else {
                            if (oldPosition >= 0) {
                                listItems.get(oldPosition).put("expend", false);
                            }
                            oldPosition = position;
                            map.put("expend", true);
                        }
                        for (int i = 0; i < adapter.getCount(); i++) {
                            View viewItem = adapter.getView(i, null, musicform_listview);
                            viewItem.measure(0, 0);
                            totalHight += viewItem.getMeasuredHeight();
                        }
                        ViewGroup.LayoutParams params = musicform_listview.getLayoutParams();
                        params.height = totalHight + (musicform_listview.getDividerHeight() * (musicform_listview.getCount() - 1));
                        musicform_listview.setLayoutParams(params);
                        adapter.notifyDataSetChanged();
                        break;

                    //点击btn之后，显示的编辑按钮事件
                    case R.id.musicform_expend_edit:
                        //Toast.makeText(MusicFormActivity.this, "编辑", Toast.LENGTH_SHORT).show();
                        updateDialog();
                        listItems.get(position).put("expend", false);
                        adapter.notifyDataSetChanged();
                        break;

                    //点击btn之后，显示的删除按钮事件
                    case R.id.musicform_expend_delete:
                        Toast.makeText(MusicFormActivity.this, "删除", Toast.LENGTH_SHORT).show();
                        deleteDialog();
                        listItems.get(position).put("expend", false);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        }

        private class ViewHolder {
            public TextView itemMusicForm_name_txt;
            public TextView itemMusicForm_num_txt;
            public ImageButton itemMusicForm_btn;
            public LinearLayout expend;
            public LinearLayout musicform_expend_edit;
            public LinearLayout musicform_expend_delete;
        }
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
            viewHolder.play_dialog_delete.setOnClickListener(new ViewOCL(position));
            //按钮绑定点击事件

            return convertView;
        }

        public class ViewOCL implements View.OnClickListener {

            public int position;

            public ViewOCL(int position) {
                this.position = position;
            }

            @Override
            public void onClick(View v) {
                List<Map<String, Object>> playList = customApplication.getPlayList();
                Map<String, Object> temp = playList.get(position);
                Map<String, Object> playingSong = customApplication.getPlayingSong();
                if (!temp.get("songName").toString().equals(playingSong.get("songName").toString())
                        && !temp.get("singer").toString().equals(playingSong.get("singer").toString())) {
                    playList.remove(position);
                }
                customApplication.setPlayList(playList);
                playListAdapter.notifyDataSetChanged();
                musicPlayer = new MusicPlayer(MusicFormActivity.this, customApplication, playimg, playbar_play,
                        playbar_songName, playbar_singer);
            }
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
        playListAdapter = new PlayListAdapter(MusicFormActivity.this, customApplication.getPlayList());
        play_dialog_playlist.setAdapter(playListAdapter);
        play_dialog_playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //设置播放栏的内容并播放
                Map<String, Object> song = customApplication.getPlayList().get(position);
                byte[] in = (byte[]) song.get("songImage");
                Bitmap bm = BitmapFactory.decodeByteArray(in, 0, in.length);
                playimg.setImageBitmap(bm);
                playbar_songName.setText(song.get("songName").toString());
                playbar_singer.setText(song.get("singer").toString());
                customApplication.setPlayingSong(song);

                musicPlayer = new MusicPlayer(MusicFormActivity.this, customApplication, playimg, playbar_play,
                        playbar_songName, playbar_singer);
                try {
                    CustomApplication.mediaPlayer.reset();
                    CustomApplication.mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(song.get("songUri").toString()));
                    CustomApplication.mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                musicPlayer.play();
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

    //点击新建弹出的对话框
    public void addDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(MusicFormActivity.this).inflate(R.layout.activity_personal_nake_set, null);
        final EditText personal_nake_input = (EditText) view.findViewById(R.id.personal_nake_input);
        final TextView personal_title_set = (TextView) view.findViewById(R.id.personal_title_set);
        personal_title_set.setText("新建歌单");
        personal_nake_input.setHint("我的歌单");

        builder.setView(view);
        builder.setPositiveButton("确定", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                songListBiz.insert(MusicFormActivity.this, new SongList(personal_nake_input.getText().toString(),
                        null, null, userName));
                listItems = init();
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        builder.show();
    }

    //点击编辑弹出的对话框
    public void updateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(MusicFormActivity.this).inflate(R.layout.activity_personal_nake_set, null);
        final EditText personal_nake_input = (EditText) view.findViewById(R.id.personal_nake_input);
        final TextView personal_title_set = (TextView) view.findViewById(R.id.personal_title_set);
        personal_title_set.setText("新的歌单名");
        personal_nake_input.setHint("我的歌单");

        builder.setView(view);
        builder.setPositiveButton("确定", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                songListBiz.update(MusicFormActivity.this, songList.get("name").toString(),
                        personal_nake_input.getText().toString(), userName);
                listItems = init();
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        builder.show();
    }

    //点击删除弹出的对话框
    public void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(MusicFormActivity.this).inflate(R.layout.delete_dialog, null);
        builder.setView(view);
        builder.setPositiveButton("确定", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String name = songList.get("name").toString();
                songListBiz.delete(MusicFormActivity.this, name, userName);
                listItems = init();
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog ad = builder.create();
        builder.show();
    }

    @Override
    protected void onResume() {
        if (CustomApplication.mediaPlayer.isPlaying()) {
            playbar_play.setImageResource(R.drawable.stop);
        } else {
            playbar_play.setImageResource(R.drawable.play);
        }
        listItems = init();
        adapter = new MusicFromListViewAdapter(this);
        musicform_listview.setAdapter(adapter);
        Map<String, Object> playingSong = customApplication.getPlayingSong();
        if (playingSong != null) {
            byte[] in = (byte[]) playingSong.get("songImage");
            Bitmap bm = BitmapFactory.decodeByteArray(in, 0, in.length);
            playimg.setImageBitmap(bm);
            playbar_songName.setText(playingSong.get("songName").toString());
            playbar_singer.setText(playingSong.get("singer").toString());
        }
        musicPlayer = new MusicPlayer(this, customApplication, playimg, playbar_play,
                playbar_songName, playbar_singer);
        updatabar.post(updatarun);
        super.onResume();
    }

    //seekBar同步音乐播放线程
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
            updatabar.postDelayed(updatarun, 1000);
           /* //通过seekBar的当前进度与最大值比较，当相等时说明歌曲播放完，这时候停止动画 设置播放按钮的图标
            if (currentPosition == 100) {
                while (CustomApplication.mediaPlayer.isPlaying()) {
                    //改变playFlag=0,让其处于待播放状态
                    playFlag = 0;
                    //设置播放按钮图标
                    playMusic.setImageResource(R.drawable.ic_media_play_3);
                }
            }*/
        }
    };
}
