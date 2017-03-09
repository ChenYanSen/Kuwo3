package com.designers.kuwo.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.designers.kuwo.R;
import com.designers.kuwo.biz.SongBiz;
import com.designers.kuwo.biz.bizimpl.SongBizImpl;
import com.designers.kuwo.utils.CircularImage;
import com.designers.kuwo.utils.CustomApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FuzzySearchActivity extends Activity {
    private CustomApplication customApplication;
    private EditText search_input;
    private TextView search_cancle;
    private ListView fuzzyList;
    private List<Map<String, Object>> fuzzySongList;
    private SongBiz songBiz;
    private String input;
    private ListViewAdapter adapter;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuzzy_search);
        customApplication = (CustomApplication) getApplication();
        InitView();
        set_eSearch_TextChanged();
        search_cancle.setOnClickListener(new ClickEvent());
    }

    private void InitView() {
        search_input = (EditText) findViewById(R.id.search_input);
        search_cancle = (TextView) findViewById(R.id.search_cancle);
        fuzzyList = (ListView) findViewById(R.id.fuzzyList);
    }

    private List<Map<String, Object>> getData(String str) {
        songBiz = new SongBizImpl();
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        items = songBiz.similarSong(this, str);
        return items;
    }

    //listView点击监听事件
    private class ItemClickEvent implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(FuzzySearchActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
            Map<String, Object> map = fuzzySongList.get(position);
            customApplication.setPlayingSong(map);
            customApplication.setPlayList(fuzzySongList);
            CustomApplication.mediaPlayer.reset();
            try {
                CustomApplication.mediaPlayer.setDataSource(map.get("songUri").toString());
                CustomApplication.mediaPlayer.prepare();
                CustomApplication.mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(FuzzySearchActivity.this, KuMuiscActivity.class);
            startActivity(intent);
        }
    }

    private class ClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.search_cancle:
                    finish();
                    break;
            }
        }
    }

    /**
     * 设置搜索框的文本更改时的监听器
     */
    private void set_eSearch_TextChanged() {
        search_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                //这是文本框改变之前会执行的动作
            }

            @Override
            public void afterTextChanged(Editable s) {
                handler.post(eChanged);
            }
        });

    }

    Runnable eChanged = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String data = search_input.getText().toString();

            if (!("").equals(data)) {
                fuzzyList.setVisibility(View.VISIBLE);
                fuzzySongList = getData(search_input.getText().toString().trim());
                if (fuzzySongList.size() != 0) {
                    adapter = new ListViewAdapter(FuzzySearchActivity.this);
                    fuzzyList.setAdapter(adapter);
                    fuzzyList.setOnItemClickListener(new ItemClickEvent());
                }
            } else {
                fuzzySongList = null;
                fuzzyList.setVisibility(View.GONE);
            }
        }
    };


    //歌手内容填充的自定义适配器
    public class ListViewAdapter extends BaseAdapter {

        private Context context;
        private ViewHolder viewHolder;
        private LayoutInflater inflater;

        public ListViewAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return fuzzySongList.size();
        }

        @Override
        public Object getItem(int position) {
            return fuzzySongList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = inflater.inflate(R.layout.item_fuzzy, null);
                viewHolder = new ViewHolder();
                viewHolder.txtItemSongfuzzy = (TextView) convertView.findViewById(R.id.txtItemSongfuzzy);
                viewHolder.txtItemsingerfuzzy = (TextView) convertView.findViewById(R.id.txtItemsingerfuzzy);
                viewHolder.circularImagefuzzy = (CircularImage) convertView.findViewById(R.id.circularImagefuzzy);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txtItemSongfuzzy.setText(fuzzySongList.get(position).get("songName").toString());
            viewHolder.txtItemsingerfuzzy.setText(fuzzySongList.get(position).get("singer").toString());
            byte[] in = (byte[]) fuzzySongList.get(position).get("songImage");
            Bitmap bm = BitmapFactory.decodeByteArray(in, 0, in.length);
            viewHolder.circularImagefuzzy.setImageBitmap(bm);
            return convertView;
        }

        private class ViewHolder {
            private TextView txtItemSongfuzzy, txtItemsingerfuzzy;
            CircularImage circularImagefuzzy;
        }
    }


}
