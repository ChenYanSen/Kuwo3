package com.designers.kuwo.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.designers.kuwo.R;
import com.designers.kuwo.biz.RecentBiz;
import com.designers.kuwo.biz.SongBiz;
import com.designers.kuwo.biz.SongListBiz;
import com.designers.kuwo.biz.bizimpl.RecentBizImpl;
import com.designers.kuwo.biz.bizimpl.SongBizImpl;
import com.designers.kuwo.biz.bizimpl.SongListBizImpl;
import com.designers.kuwo.eneity.Recent;
import com.designers.kuwo.eneity.SongList;
import com.designers.kuwo.utils.CustomApplication;
import com.designers.kuwo.utils.MusicFromListViewAdapter;
import com.designers.kuwo.utils.MusicPlayer;
import com.designers.kuwo.utils.StatusBarCompat;
import com.designers.kuwo.utils.SwipeBackLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/6.
 */
public class RecentMusicActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private List<Map<String, Object>> listItems;
    private List<Map<String, Object>> formListItems;
    private ListView music_listview;
    private ImageView back;
    private TextView toolbar_txt;
    private TextView select_txt;
    private LinearLayout music_dialog_add;//对话框添加按钮
    private LinearLayout music_dialog_selectall;//对话框全选按钮
    private LinearLayout music_dialog_delete;//对话框删除按钮
    private LinearLayout music_dialog_cancle;//对话框取消按钮
    private ImageView recent_more;//更多按钮
    private ImageView playimg;
    private ImageView playlist_menu;
    private TextView playbar_songName;
    private TextView playbar_singer;
    private ImageView playbar_play;
    private ImageView playbar_next;
    private SeekBar seekBar;
    private SwipeBackLayout swipeLayout;
    private MusicListViewAdapter adapter;
    private MusicSelectListViewAdapter selectAdapter;
    private MusicFromListViewAdapter musicFromListViewAdapter;
    private PlayListAdapter playListAdapter;
    private Dialog dialog;//长按item底部弹出的对话框
    private Dialog addDialog;
    private Dialog infoDialog;
    private Dialog playListDialog;
    private int oldPosition = -1;//用来记录上一次所点按钮的位置
    private int totalHight = 0;
    private boolean flag = false;//是否处于多选
    private boolean once;
    private RelativeLayout mainPlay;

    private CustomApplication customApplication;
    private String userName;
    private List<Integer> selectList = new ArrayList<>();
    private RecentBiz recentBiz = new RecentBizImpl();
    private Map<String, Object> song = new HashMap<>();
    private SongListBiz songListBiz = new SongListBizImpl();
    private SongBiz songBiz = new SongBizImpl();
    private MusicPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        customApplication = (CustomApplication) getApplication();
        userName = customApplication.getUserName();
        listItems = init();
        back = (ImageView) findViewById(R.id.back);
        toolbar_txt = (TextView) findViewById(R.id.toolbar_txt);
        swipeLayout = (SwipeBackLayout) findViewById(R.id.swipeLayout);
        music_listview = (ListView) findViewById(R.id.music_listview);
        recent_more = (ImageView) findViewById(R.id.rencent_more);
        playimg = (ImageView) findViewById(R.id.playimg);
        playlist_menu = (ImageView) findViewById(R.id.playlist_menu);
        playbar_songName = (TextView) findViewById(R.id.playbar_songName);
        playbar_singer = (TextView) findViewById(R.id.playbar_singer);
        playbar_play = (ImageView) findViewById(R.id.playbar_play);
        playbar_next = (ImageView) findViewById(R.id.playbar_next);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        mainPlay = (RelativeLayout) findViewById(R.id.mainPlay);

        toolbar_txt.setText("最近");
        if (CustomApplication.mediaPlayer.isPlaying()) {
            playbar_play.setImageResource(R.drawable.stop);
        }


        swipeLayout.setCallBack(new SwipeBackLayout.CallBack() {
            @Override
            public void onFinish() {
                finish();
            }
        });
        adapter = new MusicListViewAdapter(this);
        selectAdapter = new MusicSelectListViewAdapter(this);
        music_listview.setAdapter(adapter);

        mainPlay.setOnClickListener(this);
        back.setOnClickListener(this);
        recent_more.setOnClickListener(this);
        music_listview.setOnItemClickListener(this);
        music_listview.setOnItemLongClickListener(this);
        playlist_menu.setOnClickListener(this);
        playbar_play.setOnClickListener(this);
        playbar_next.setOnClickListener(this);

        //全屏
        StatusBarCompat.compat(this);
        updatabar.post(updatarun);
    }

    public List<Map<String, Object>> init() {

        List<Map<String, Object>> items = recentBiz.findAll(this);
        return items;
    }

    public List<Map<String, Object>> initSongForm() {
        List<Map<String, Object>> items = songListBiz.findNameAndSize(this, userName);
        return items;
    }

    //歌曲item点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!flag) {
            //Toast.makeText(this, position + "", Toast.LENGTH_SHORT).show();
            String songName = listItems.get(position).get("songName").toString();
            String singer = listItems.get(position).get("singer").toString();
            int i = songBiz.selectRank(this, songName, singer);
            //Log.i("test", i + "");
            songBiz.updateRank(this, songName, singer, i + 1);
            if (recentBiz.songExist(this, songName)) {
                recentBiz.update(this, new Recent(songName, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())));
            } else {
                recentBiz.insert(this, new Recent(songName, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())));
            }
            //加入播放列表
            if (!once) {
                customApplication.setPlayList(listItems);
                once = true;
            }

            //设置播放栏的内容
            Map<String, Object> song = listItems.get(position);
            byte[] in = (byte[]) song.get("songImage");
            Bitmap bm = BitmapFactory.decodeByteArray(in, 0, in.length);
            playimg.setImageBitmap(bm);
            playbar_songName.setText(song.get("songName").toString());
            playbar_singer.setText(song.get("singer").toString());
            customApplication.setPlayingSong(song);

            musicPlayer = new MusicPlayer(this, customApplication, playimg, playbar_play,
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

    //歌曲item长点击事件
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (!flag) {
            music_listview.setAdapter(selectAdapter);
            setDialog();
            flag = true;
        }
        return false;
    }

    //按钮点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.playlist_menu:
                playListDialog();
                break;

            case R.id.rencent_more:
                MorePopWindow morePopWindow = new MorePopWindow(this);
                morePopWindow.showPopupWindow(recent_more);
                break;

            case R.id.music_dilog_add:
                //Toast.makeText(this, "添加", Toast.LENGTH_SHORT).show();
                if (selectList.size() != 0) {
                    dialog.dismiss();
                    music_listview.setAdapter(adapter);
                    listItems = init();
                    addDialog();
                }
                flag = false;
                break;

            case R.id.music_dilog_selectall:
                if ("全选".equals(select_txt.getText().toString())) {
                    //Toast.makeText(this, "全选", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < listItems.size(); i++) {
                        listItems.get(i).put("checked", true);
                        if (!selectList.contains(i))
                            selectList.add(i);
                    }
                    select_txt.setText("取消全选");
                } else {
                    for (int i = 0; i < listItems.size(); i++) {
                        listItems.get(i).put("checked", false);
                    }
                    selectList.clear();
                    select_txt.setText("全选");
                }
                selectAdapter.notifyDataSetChanged();
                flag = false;
                break;

            case R.id.music_dilog_delete:
                //Toast.makeText(this, "删除" + selectList.toString(), Toast.LENGTH_SHORT).show();
                if (selectList.size() != 0) {
                    deleteDialog();
                    music_listview.setAdapter(adapter);
                    listItems = init();
                    dialog.dismiss();
                }
                flag = false;
                break;

            case R.id.music_dilog_cancle:
                // Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                music_listview.setAdapter(adapter);
                listItems = init();
                selectList.clear();
                dialog.dismiss();
                flag = false;
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

            //点击播放栏
            case R.id.mainPlay:
                if (customApplication.getPlayList().size() > 0) {
                    Intent intent = new Intent(this, KuMuiscActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    //歌曲列表的适配器
    public class MusicListViewAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        public MusicListViewAdapter(Context context) {
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
                convertView = inflater.inflate(R.layout.musicform_music_item_layout, null);
                viewHolder.itemMusic_name_txt = (TextView) convertView.findViewById(R.id.itemMusic_name_txt);
                viewHolder.itemMusic_songer_txt = (TextView) convertView.findViewById(R.id.itemMusic_songer_txt);
                viewHolder.itemMusic_btn = (ImageButton) convertView.findViewById(R.id.itemMusic_btn);
                viewHolder.expend = (LinearLayout) convertView.findViewById(R.id.expend);
                viewHolder.music_expend_add = (LinearLayout) convertView.findViewById(R.id.music_expend_add);
                viewHolder.music_expend_info = (LinearLayout) convertView.findViewById(R.id.music_expend_info);
                viewHolder.music_expend_delete = (LinearLayout) convertView.findViewById(R.id.music_expend_delete);
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
            viewHolder.itemMusic_name_txt.setText(map.get("songName").toString());
            viewHolder.itemMusic_songer_txt.setText(map.get("singer").toString());
            //按钮绑定点击事件
            viewHolder.itemMusic_btn.setOnClickListener(new ViewOCL(position));
            viewHolder.music_expend_add.setOnClickListener(new ViewOCL(position));
            viewHolder.music_expend_info.setOnClickListener(new ViewOCL(position));
            viewHolder.music_expend_delete.setOnClickListener(new ViewOCL(position));
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
                    case R.id.itemMusic_btn:

                        song = listItems.get(position);

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
                            View viewItem = adapter.getView(i, null, music_listview);
                            viewItem.measure(0, 0);
                            totalHight += viewItem.getMeasuredHeight();
                        }
                        ViewGroup.LayoutParams params = music_listview.getLayoutParams();
                        params.height = totalHight + (music_listview.getDividerHeight() * (music_listview.getCount() - 1));
                        music_listview.setLayoutParams(params);
                        adapter.notifyDataSetChanged();
                        break;

                    case R.id.music_expend_add:
                        //Toast.makeText(RecentMusicActivity.this, "添加", Toast.LENGTH_SHORT).show();
                        addDialog();
                        listItems.get(position).put("expend", false);
                        adapter.notifyDataSetChanged();
                        break;

                    case R.id.music_expend_info:
                        //Toast.makeText(RecentMusicActivity.this, "信息", Toast.LENGTH_SHORT).show();
                        infoDialog();
                        listItems.get(position).put("expend", false);
                        adapter.notifyDataSetChanged();
                        break;

                    case R.id.music_expend_delete:
                        //Toast.makeText(RecentMusicActivity.this, "删除", Toast.LENGTH_SHORT).show();
                        deleteDialog();
                        listItems.get(position).put("expend", false);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        }

        private class ViewHolder {
            public TextView itemMusic_name_txt;
            public TextView itemMusic_songer_txt;
            public ImageButton itemMusic_btn;
            public LinearLayout expend;
            public LinearLayout music_expend_add;
            public LinearLayout music_expend_info;
            public LinearLayout music_expend_delete;
        }
    }

    //歌曲多选适配器
    public class MusicSelectListViewAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        public MusicSelectListViewAdapter(Context context) {
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
                Log.i("test", position + "为空");
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.musicform_music_item__select_layout, null);
                viewHolder.itemMusic_name_txt = (TextView) convertView.findViewById(R.id.itemMusic_name_txt);
                viewHolder.itemMusic_songer_txt = (TextView) convertView.findViewById(R.id.itemMusic_songer_txt);
                viewHolder.check = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(viewHolder);
            } else {
                Log.i("test", position + "不为空");
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((boolean) map.get("checked")) {
                        map.put("checked", false);
                        selectList.remove(selectList.indexOf(position));
                    } else {
                        map.put("checked", true);
                        selectList.add(position);
                    }
                }
            });
            viewHolder.check.setChecked((boolean) map.get("checked"));
            viewHolder.itemMusic_name_txt.setText(map.get("songName").toString());
            viewHolder.itemMusic_songer_txt.setText(map.get("singer").toString());
            return convertView;
        }

        private class ViewHolder {
            public TextView itemMusic_name_txt;
            public TextView itemMusic_songer_txt;
            private CheckBox check;
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
                musicPlayer = new MusicPlayer(RecentMusicActivity.this, customApplication, playimg, playbar_play,
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
        playListAdapter = new PlayListAdapter(RecentMusicActivity.this, customApplication.getPlayList());
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

                musicPlayer = new MusicPlayer(RecentMusicActivity.this, customApplication, playimg, playbar_play,
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

    //点击多选按钮之后弹出的对话框
    public void setDialog() {
        dialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.music_bottom_dialog, null);
        music_dialog_add = (LinearLayout) root.findViewById(R.id.music_dilog_add);
        music_dialog_selectall = (LinearLayout) root.findViewById(R.id.music_dilog_selectall);
        music_dialog_delete = (LinearLayout) root.findViewById(R.id.music_dilog_delete);
        music_dialog_cancle = (LinearLayout) root.findViewById(R.id.music_dilog_cancle);
        select_txt = (TextView) root.findViewById(R.id.select_txt);
        music_dialog_add.setOnClickListener(this);
        music_dialog_selectall.setOnClickListener(this);
        music_dialog_delete.setOnClickListener(this);
        music_dialog_cancle.setOnClickListener(this);
        dialog.setContentView(root);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //不获取焦点，即弹出对话框菜单之后可继续操作界面
        dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        //lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    //点击添加按钮弹出的对话框
    public void addDialog() {
        addDialog = new Dialog(this, R.style.BottomAddDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.add_dialog, null);
        ListView add_dialog_musicform = (ListView) root.findViewById(R.id.add_dialog_musicform);
        formListItems = initSongForm();
        musicFromListViewAdapter = new MusicFromListViewAdapter(RecentMusicActivity.this, formListItems);
        add_dialog_musicform.setAdapter(musicFromListViewAdapter);
        add_dialog_musicform.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> songList = formListItems.get(position);
                String songListName = songList.get("name").toString();
                if (selectList.size() == 0) {
                    String songName = song.get("songName").toString();
                    String singer = song.get("singer").toString();
                    //Log.i("test", songListName + songName);
                    if (!songListBiz.selectByName(RecentMusicActivity.this, songName, singer, songListName, userName))
                        songListBiz.insert(RecentMusicActivity.this, new SongList(songListName, songName, singer, userName));
                } else {
                    for (int index : selectList) {
                        String songName = listItems.get(index).get("songName").toString();
                        String singer = listItems.get(index).get("singer").toString();
                        if (!songListBiz.selectByName(RecentMusicActivity.this, songName, singer, songListName, userName))
                            songListBiz.insert(RecentMusicActivity.this, new SongList(songListName, songName, singer, userName));
                    }
                }
                selectList.clear();
                addDialog.dismiss();
            }
        });
        addDialog.setContentView(root);
        Window dialogWindow = addDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = 600;
        dialogWindow.setAttributes(lp);
        addDialog.show();
    }

    //点击信息弹出的对话框
    public void infoDialog() {
        infoDialog = new Dialog(this, R.style.BottomAddDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.info_dialog, null);
        TextView songName = (TextView) root.findViewById(R.id.songName);
        TextView singer = (TextView) root.findViewById(R.id.singer);
        TextView songUri = (TextView) root.findViewById(R.id.songUri);
        TextView time = (TextView) root.findViewById(R.id.time);
        songName.setText(song.get("songName").toString());
        singer.setText(song.get("singer").toString());
        songUri.setText(song.get("songUri").toString());
        time.setText(new SimpleDateFormat("mm:ss").format(new Date(Integer.parseInt(song.get("time").toString()))));
        infoDialog.setContentView(root);
        Window dialogWindow = infoDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = 550;
        dialogWindow.setAttributes(lp);
        infoDialog.show();
    }

    //点击删除弹出的对话框
    public void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(RecentMusicActivity.this).inflate(R.layout.delete_dialog, null);
        builder.setView(view);
        builder.setPositiveButton("确定", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (selectList.size() == 0 && song.size() != 0) {
                    recentBiz.deleteByName(RecentMusicActivity.this, song.get("songName").toString());
                } else {
                    for (int index : selectList) {
                        //Log.i("test", index + "");
                        recentBiz.deleteByName(RecentMusicActivity.this, listItems.get(index).get("songName").toString());
                    }
                }
                selectList.clear();
                listItems = init();
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                selectList.clear();
            }
        });
        AlertDialog ad = builder.create();
        builder.show();
    }

    //点击清空弹出的popwindow
    public class MorePopWindow extends PopupWindow implements View.OnClickListener {
        private View conentView;
        private LinearLayout clearAll;
        private Context context;

        private RecentBiz recentBiz = new RecentBizImpl();

        public MorePopWindow(final Activity context) {
            this.context = context;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            conentView = inflater.inflate(R.layout.more_popup_dialog, null);
            int h = context.getWindowManager().getDefaultDisplay().getHeight();
            int w = context.getWindowManager().getDefaultDisplay().getWidth();
            // 设置SelectPicPopupWindow的View
            this.setContentView(conentView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(w / 2 + 50);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            // 刷新状态
            this.update();
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0000000000);
            // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
            this.setBackgroundDrawable(dw);
            // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            // 设置SelectPicPopupWindow弹出窗体动画效果
            this.setAnimationStyle(R.style.AnimationPreview);
            clearAll = (LinearLayout) conentView.findViewById(R.id.clearAll);
            clearAll.setOnClickListener(this);
        }

        public void showPopupWindow(View parent) {
            if (!this.isShowing()) {
                this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
            } else {
                this.dismiss();
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.clearAll:
                    //Toast.makeText(context, "清空", Toast.LENGTH_SHORT).show();
                    this.dismiss();
                    recentBiz.deleteAll(this.context);
                    listItems = init();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
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
