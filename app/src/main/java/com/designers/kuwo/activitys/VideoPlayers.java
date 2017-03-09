package com.designers.kuwo.activitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.designers.kuwo.R;
import com.designers.kuwo.utils.ScreenSwitchUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 跃 on 2017/3/3.
 */
public class VideoPlayers extends Activity /*implements View.OnClickListener, AdapterView.OnItemClickListener*/ {
    private ScreenSwitchUtils instance;
    CommonVideoView videoView;
    private int screenWidth;
    private int screenHeight;
    private List<HashMap<String, String>> videoList;
    private List<String> videoUri = new ArrayList<String>();//获取mv
    private MyAdapter myAdapter;
    private EditText vedio_internet;
    private Button video_internet_require;
    private TextView video_num;
    private ListView video_listview;
    private ImageView video_back;
    private LinearLayout video_line;
    private Toolbar  toolBar;
    private TextView toolbar_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         requestWindowFeature(Window.FEATURE_NO_TITLE);
        //屏幕获取
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        setContentView(R.layout.content_main);

        init();
        instance = ScreenSwitchUtils.init(this.getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取列表信息，mv名称，歌手，视频时间
        videoList = getVideo();
        if (videoUri.size() != 0 && null != videoUri) {
            videoView.start(videoUri.get(0));
            Log.i("-----------------------", videoUri.get(0));
        }
        videoView.getUriList(videoUri);
        myAdapter = new MyAdapter(this, videoList);
        //绑定视频视频器
        video_listview.setAdapter(myAdapter);
        //绑定监听
        video_listview.setOnItemClickListener(new MyItemClick());
        video_internet_require.setOnClickListener(new MyClick());
        video_back.setOnClickListener(x->{startActivity(new Intent(VideoPlayers.this,MainActivity.class));});
        toolbar_txt.setText("视频播放");
    }

    //布局加载
    void init() {
        videoView = (CommonVideoView) findViewById(R.id.common_videoView);
        videoView.setHeightandWeth(screenHeight, screenWidth);
        vedio_internet = (EditText) findViewById(R.id.vedio_internet);
        video_internet_require = (Button) findViewById(R.id.video_internet_require);
        video_num = (TextView) findViewById(R.id.vedio_num);
        video_listview = (ListView) findViewById(R.id.video_listview);
        video_back= (ImageView) findViewById(R.id.back);
       toolBar= (Toolbar) findViewById(R.id.toolBar);
        toolbar_txt= (TextView) findViewById(R.id.toolbar_txt);
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance.start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        instance.stop();
    }

    /**
     * @param newConfig
     */
    @SuppressLint("NewApi")
    /**
     * 重力感应及手动切换横竖屏 */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("test", "onConfigurationChanged");

        if (instance.isPortrait()) {
            toolBar.setVisibility(View.VISIBLE);
            videoView.setNormalScreen(screenWidth, this);

            Log.i("test", "onConfigurationChanged,竖屏");
        } else {
            toolBar.setVisibility(View.GONE);
            videoView.setFullScreen(screenHeight,screenWidth);
            Log.i("test", "onConfigurationChanged,横屏");
        }

    }


  class   MyItemClick implements AdapterView.OnItemClickListener {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          Log.i("item", "--------------------------");
                  if (videoUri.get(0).length() == 0 || null == videoUri.get(0)) {
                      showCustomToast("没有视频不能播放");
                  } else {
                    videoView.nextVideo(videoUri.get(position));

                      Log.i("itemclick", videoUri.get(position).toString());
                  }

      }
  }


 class MyClick implements View.OnClickListener {
     @Override
     public void onClick(View v) {
         switch (v.getId()) {
             case R.id.video_internet_require:
                 String uri = vedio_internet.getText().toString().trim();
                 if (uri.length() == 0 && null == uri) {
                     showCustomToast("地址不能为空");
                 } else {
                     videoView.nextVideo(uri);
                 }
         }
     }
 }

    class MyAdapter extends BaseAdapter {
        List<HashMap<String, String>> list;
        Context context;

        MyAdapter(Context context, List<HashMap<String, String>> list) {
            this.list = list;
            this.context = context;

        }

        @Override
        public int getCount() {
            if (list.size() > 0) {
                //显示视频数量
                video_num.setText(Integer.toString(list.size()));
            }
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_video_list, null);
                viewHolder = new ViewHolder();
                viewHolder.video_singer = (TextView) convertView.findViewById(R.id.video_singer);
                viewHolder.video_title = (TextView) convertView.findViewById(R.id.video_title);
                viewHolder.video_time = (TextView) convertView.findViewById(R.id.video_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            HashMap<String, String> map = (HashMap<String, String>) list.get(position);
            viewHolder.video_title.setText(map.get("title"));
            viewHolder.video_singer.setText(map.get("singer"));
            viewHolder.video_time.setText(map.get("time"));
            return convertView;
        }
    }

    class ViewHolder {
        private TextView video_title, video_singer, video_time;
    }

    //---获取视频列表标题，歌手，及路径,时间
    public List<HashMap<String, String>> getVideo() {
        //
        String[] projection = {MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.ARTIST,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATA
        };
        String orderBy = MediaStore.Video.Media.DISPLAY_NAME;
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        return getContentProvider(uri, projection, orderBy);
    }

    /**
     * 获取ContentProvider
     *
     * @param projection
     * @param orderBy
     */
    public List<HashMap<String, String>> getContentProvider(Uri uri, String[] projection, String orderBy) {
        //
        List<HashMap<String, String>> listImage = new ArrayList<HashMap<String, String>>();
        String[] videos = {"title", "singer", "time"};
        Cursor cursor = getContentResolver().query(uri, projection, null,
                null, orderBy);

        if (null == cursor) {
            return null;
        }
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(videos[0], cursor.getString(0));
            Log.i("title---", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
            map.put(videos[1], cursor.getString(1));
            Log.i("singer---", cursor.getString(1));
            map.put(videos[0], Integer.toString(cursor.getInt(2)));
            Log.i("uri---", cursor.getString(3));
            listImage.add(map);
            //获得视频路径

            videoUri.add(cursor.getString(3));
        }
        return listImage;
    }

    // 自定义toast信息显示
    public void showCustomToast(String message) {
        // 1.创建一个LayoutInflater接口对象
        LayoutInflater inflater = getLayoutInflater();
        // 2.使用inflater对象中的inflate方法绑定自定义Toast布局文件,同事指向该布局文件中的根标记节点
        View layout = inflater.inflate(R.layout.toast_custom, null);

        // 3.获取该布局文件中的TextView组件
        TextView toastMessage = (TextView) layout
                .findViewById(R.id.toastMessage);
        // 4.为TextView赋值
        toastMessage.setText(message);
        // 5.实例化一个Toast组件对象，并进行显示
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        // 将步骤4设置好的定制布局与当前的Toast对象进行绑定
        toast.setView(layout);
        // 6.显示Toast组件
        toast.show();
    }
}
