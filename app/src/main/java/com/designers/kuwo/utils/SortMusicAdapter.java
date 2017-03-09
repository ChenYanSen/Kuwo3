package com.designers.kuwo.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.designers.kuwo.R;

import java.util.List;
import java.util.Map;

/**
 * 主页歌单列表
 */
public class SortMusicAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> listItems;
    private LayoutInflater inflater;

    public SortMusicAdapter(Context context, List<Map<String, Object>> listItems) {
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
            convertView = inflater.inflate(R.layout.sortmusic_item, null);
            viewHolder.rank = (TextView) convertView.findViewById(R.id.rank);
            viewHolder.sortmusic_name = (TextView) convertView.findViewById(R.id.sortmusic_name);
            viewHolder.sortmusic_singer = (TextView) convertView.findViewById(R.id.sortmusic_singer);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.rank.setText(listItems.get(position).get("rank").toString());
        viewHolder.sortmusic_name.setText(listItems.get(position).get("songName").toString());
        viewHolder.sortmusic_singer.setText(listItems.get(position).get("singer").toString());

        //按钮绑定点击事件

        return convertView;
    }

    private class ViewHolder {
        public TextView rank;
        public TextView sortmusic_name;
        public TextView sortmusic_singer;
    }

}
