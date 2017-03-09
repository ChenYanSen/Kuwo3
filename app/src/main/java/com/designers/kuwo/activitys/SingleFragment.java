package com.designers.kuwo.activitys;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.designers.kuwo.R;
import com.designers.kuwo.biz.CollectionBiz;
import com.designers.kuwo.biz.SongBiz;
import com.designers.kuwo.biz.SongListBiz;
import com.designers.kuwo.biz.bizimpl.CollectionBizImp;
import com.designers.kuwo.biz.bizimpl.SongBizImpl;
import com.designers.kuwo.biz.bizimpl.SongListBizImpl;
import com.designers.kuwo.eneity.ICollection;
import com.designers.kuwo.eneity.SongList;
import com.designers.kuwo.utils.CustomApplication;
import com.designers.kuwo.utils.MusicFromListViewAdapter;
import com.designers.kuwo.utils.MusicPlayer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SingleFragment extends Fragment {

    //试图组件
    private ListView listViewSingle;
    private SingleFragmentAdapter adapter;
    private SongBiz songBiz;
    private CollectionBiz collectionBiz;
    private List<Map<String, Object>> songList;
    private List<Map<String, Object>> songPlayList;
    private List<Map<String, Object>> formListItems;
    private SongListBiz songListBiz;
    private String account;
    private int oldPosition = -1;
    private Dialog infoDialog;
    private Dialog addDialog;
    private CustomApplication customApplication;

    private MusicPlayer musicPlayer;

    //playbar中的一些组件
    private ImageView playimg;
    private ImageView playlist_menu;//播放列表按钮
    private TextView playbar_songName;
    private TextView playbar_singer;
    private ImageView playbar_play;
    private ImageView playbar_next;

    private MusicFromListViewAdapter musicFromListViewAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single, null);
        View playbar = inflater.inflate(R.layout.main_playbar, null);

        playimg = (ImageView) playbar.findViewById(R.id.playimg);
        playbar_songName = (TextView) playbar.findViewById(R.id.playbar_songName);
        playbar_singer = (TextView) playbar.findViewById(R.id.playbar_singer);
        playbar_play = (ImageView) playbar.findViewById(R.id.playbar_play);
        playbar_next = (ImageView) playbar.findViewById(R.id.playbar_next);
        playlist_menu = (ImageView) playbar.findViewById(R.id.playlist_menu);
        this.listViewSingle = (ListView) view.findViewById(R.id.listViewSingle);

        songBiz = new SongBizImpl();
        songListBiz = new SongListBizImpl();
        songList = songBiz.findAll(getActivity());


        adapter = new SingleFragmentAdapter(this.getActivity());
        listViewSingle.setAdapter(adapter);
        listViewSingle.setOnItemClickListener(new ItemClickEvent());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //获取customApplication中的数据
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customApplication = (CustomApplication) getActivity().getApplication();
        account = customApplication.getUserName();  //拿到用户信息

    }

    class ItemClickEvent implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String itemSongUri = songList.get(position).get("songUri").toString();
            Log.i("listview item 的监听事件==", "itemSongUri:" + itemSongUri);

            Map<String, Object> song = songList.get(position);
            Log.i("song", "song==:" + song.get("songName").toString());
            byte[] in = (byte[]) song.get("songImage");
            Bitmap bm = BitmapFactory.decodeByteArray(in, 0, in.length);
            playimg.setImageBitmap(bm);
            playbar_songName.setText(song.get("songName").toString());
            playbar_singer.setText(song.get("singer").toString());
            customApplication.setPlayingSong(song);
            customApplication.setPlayList(songList);

            musicPlayer = new MusicPlayer(getActivity(), customApplication, playimg, playbar_play,
                    playbar_songName, playbar_singer);
            try {
                CustomApplication.mediaPlayer.reset();
                CustomApplication.mediaPlayer.setDataSource(getActivity(), Uri.parse(song.get("songUri").toString()));
                CustomApplication.mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            musicPlayer.play();
        }
    }

    private class SingleFragmentAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private boolean expend = false;
        ViewHolder holder = null;

        public SingleFragmentAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return songList.size();
        }

        @Override
        public Object getItem(int position) {
            return songList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //拿到视图
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.item_single, null);
                holder = new ViewHolder();
                holder.txtItemSingleSong = (TextView) convertView.findViewById(R.id.txtItemSingleSong);
                holder.txtItemSingleSinger = (TextView) convertView.findViewById(R.id.txtItemSingleSinger);
                holder.imgBtnItemSingleDown = (ImageButton) convertView.findViewById(R.id.imgBtnItemSingleDown);
                holder.itemHideLinearLayoutAdd = (LinearLayout) convertView.findViewById(R.id.itemHideLinearLayoutAdd);
                holder.itemHideLinearLayoutCollect = (LinearLayout) convertView.findViewById(R.id.itemHideLinearLayoutCollect);
                holder.itemHideLinearLayoutRemove = (LinearLayout) convertView.findViewById(R.id.itemHideLinearLayoutRemove);
                holder.itemHideLinearLayoutMessage = (LinearLayout) convertView.findViewById(R.id.itemHideLinearLayoutMessage);
                holder.itemHideLinearLayout = (LinearLayout) convertView.findViewById(R.id.itemHideLinearLayout);
                holder.imgHideCollect1 = (ImageView) convertView.findViewById(R.id.imgHideCollect1);
                holder.txtHideCollect = (TextView) convertView.findViewById(R.id.txtHideCollect);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //视图上赋值
            holder.txtItemSingleSong.setText(songList.get(position).get("songName").toString());
            holder.txtItemSingleSong.setTextColor(getResources().getColor(R.color.textcolor_black));
            holder.txtItemSingleSinger.setText(songList.get(position).get("singer").toString());
            holder.txtItemSingleSinger.setTextColor(getResources().getColor(R.color.textcolor_black));

            //对于按钮，检测它的状态，看是处于隐藏状态还是处于伸展状态
            if ((boolean) songList.get(position).get("expend")) {
                holder.itemHideLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.itemHideLinearLayout.setVisibility(View.GONE);
            }

            holder.imgBtnItemSingleDown.setOnClickListener(new View.OnClickListener() {
                private int totalHight = 0;

                @Override
                public void onClick(View v) {
                    //拿到位置
                    expend = (boolean) songList.get(position).get("expend");
                    if (oldPosition == position) {
                        if (expend) {
                            oldPosition = -1;
                        }
                        songList.get(position).put("expend", !expend);
                    } else {
                        if (oldPosition >= 0) {
                            songList.get(oldPosition).put("expend", false);
                        }
                        oldPosition = position;
                        songList.get(position).put("expend", true);
                    }

                    for (int i = 0; i < 6; i++) {
                        View viewItem = adapter.getView(i, null, listViewSingle);
                        viewItem.measure(0, 0);
                        totalHight += viewItem.getMeasuredHeight();
                    }

                    ViewGroup.LayoutParams params = listViewSingle.getLayoutParams();
                    params.height = totalHight + (listViewSingle.getDividerHeight() * (listViewSingle.getCount() - 1));
                    listViewSingle.setLayoutParams(params);
                    adapter.notifyDataSetChanged();
                }
            });
            holder.itemHideLinearLayoutAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDialog(position);
                }

            });
            holder.itemHideLinearLayoutCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean flagExist;//判断是否收藏标记
                    String songName = songList.get(position).get("songName").toString();
                    String singer = songList.get(position).get("singer").toString();
                    String songUri = songList.get(position).get("songUri").toString();
                    byte[] songImage = (byte[]) songList.get(position).get("songImage");
                    String singlyrics = null;
                    String time = songList.get(position).get("time").toString();
                    String information = null;
                    String folder = null;
                    String rank = songList.get(position).get("rank").toString();
                    ICollection c = new ICollection();
                    c.setAccount(account);
                    c.setSongName(songName);
                    c.setSinger(singer);
                    c.setSongUri(songUri);
                    c.setSongImage(songImage);
                    c.setSinglyrics(singlyrics);
                    c.setTime(time);
                    c.setInformation(information);
                    c.setRank(rank);
                    c.setFolder(folder);
                    collectionBiz = new CollectionBizImp();
                    flagExist = collectionBiz.findCollection(songName, singer, account, getActivity());
                    if (flagExist) {
                        collectionBiz.removeCollection(songName, singer, account, getActivity());
                        Toast.makeText(context, "恭喜你取消收藏成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        collectionBiz.alterCollection(c, getActivity());
                        Toast.makeText(context, "恭喜你收藏成功！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.itemHideLinearLayoutRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String songName = songList.get(position).get("songName").toString();
                    String singer = songList.get(position).get("singer").toString();
                    songBiz = new SongBizImpl();
                    songBiz.removeSong(context, songName, singer);
                    Toast.makeText(context, "恭喜你删除成功！", Toast.LENGTH_SHORT).show();
                    songList = songBiz.findAll(getActivity());
                    adapter.notifyDataSetChanged();//刷新列表
                }
            });
            holder.itemHideLinearLayoutMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    infoDialog = new Dialog(getActivity(), R.style.BottomAddDialog);
                    LinearLayout root = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.info_dialog, null);
                    TextView songName = (TextView) root.findViewById(R.id.songName);
                    TextView singer = (TextView) root.findViewById(R.id.singer);
                    TextView songUri = (TextView) root.findViewById(R.id.songUri);
                    TextView time = (TextView) root.findViewById(R.id.time);
                    songName.setText(songList.get(position).get("songName").toString());
                    singer.setText(songList.get(position).get("singer").toString());
                    songUri.setText(songList.get(position).get("songUri").toString());
                    time.setText(new SimpleDateFormat("mm:ss").format(new Date(Integer.parseInt(songList.get(position).get("time").toString()))));
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
            });
            return convertView;
        }
    }

    private class ViewHolder {
        LinearLayout itemHideLinearLayout, itemHideLinearLayoutAdd, itemHideLinearLayoutRemove,
                itemHideLinearLayoutMessage, itemHideLinearLayoutCollect;
        ImageView imgHideCollect1;
        TextView txtItemSingleSong, txtItemSingleSinger, txtHideCollect;
        ImageButton imgBtnItemSingleDown;
    }

    //点击添加按钮弹出的对话框
    public void addDialog(int index) {
        addDialog = new Dialog(getActivity(), R.style.BottomAddDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.add_dialog, null);
        ListView add_dialog_musicform = (ListView) root.findViewById(R.id.add_dialog_musicform);
        formListItems = initSongForm();
        musicFromListViewAdapter = new MusicFromListViewAdapter(getActivity(), formListItems);
        add_dialog_musicform.setAdapter(musicFromListViewAdapter);
        add_dialog_musicform.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> songList = formListItems.get(position);
                String songListName = songList.get("name").toString();
                String songName = SingleFragment.this.songList.get(index).get("songName").toString();
                String singer = SingleFragment.this.songList.get(index).get("singer").toString();
                if (!songListBiz.selectByName(getActivity(), songName, singer, songListName, account)) {
                    songListBiz.insert(getActivity(), new SongList(songListName, songName, singer, account));
                }
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

    public List<Map<String, Object>> initSongForm() {
        List<Map<String, Object>> items = songListBiz.findNameAndSize(getActivity(), account);
        return items;
    }

}
