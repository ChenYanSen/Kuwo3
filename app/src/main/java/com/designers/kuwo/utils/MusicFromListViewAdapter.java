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
public class MusicFromListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> listItems;
    private LayoutInflater inflater;

    public MusicFromListViewAdapter(Context context, List<Map<String, Object>> listItems) {
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
            convertView = inflater.inflate(R.layout.add_dialog_item, null);
            viewHolder.itemMusicForm_name_txt = (TextView) convertView.findViewById(R.id.itemMusicForm_name_txt);
            viewHolder.itemMusicForm_num_txt = (TextView) convertView.findViewById(R.id.itemMusicForm_num_txt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemMusicForm_name_txt.setText(listItems.get(position).get("name").toString());
        viewHolder.itemMusicForm_num_txt.setText(listItems.get(position).get("size").toString());

        //按钮绑定点击事件

        return convertView;
    }

    private class ViewHolder {
        public TextView itemMusicForm_name_txt;
        public TextView itemMusicForm_num_txt;
    }

}
