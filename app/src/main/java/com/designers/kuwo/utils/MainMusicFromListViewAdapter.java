package com.designers.kuwo.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.designers.kuwo.R;

import java.util.List;
import java.util.Map;

/**
 * 主页歌单列表
 */
public class MainMusicFromListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, ?>> listItems;
    private LayoutInflater inflater;

    public MainMusicFromListViewAdapter(Context context, List<Map<String, ?>> listItems) {
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
            convertView = inflater.inflate(R.layout.main_item_musicformlistview, null);
            viewHolder.itemMusicForm_first_txt = (TextView) convertView.findViewById(R.id.itemMusicForm_first_txt);
            viewHolder.itemMusicForm_second_txt = (TextView) convertView.findViewById(R.id.itemMusicForm_second_txt);
            viewHolder.itemMusicForm_Third_txt = (TextView) convertView.findViewById(R.id.itemMusicForm_third_txt);
            viewHolder.itemMusicForm_photo_imageview = (ImageView) convertView.findViewById(R.id.itemMusicForm_photo_imageview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.itemMusicForm_first_txt.setText(listItems.get(position).get("listTextFrist").toString());
        viewHolder.itemMusicForm_second_txt.setText(listItems.get(position).get("listTextSecond").toString());
        viewHolder.itemMusicForm_Third_txt.setText(listItems.get(position).get("listTextThird").toString());
        viewHolder.itemMusicForm_photo_imageview.setImageResource(Integer.parseInt(
                listItems.get(position).get("formImageId").toString()
        ));

        return convertView;
    }

    private class ViewHolder {
        public TextView itemMusicForm_first_txt;
        public TextView itemMusicForm_second_txt;
        public TextView itemMusicForm_Third_txt;
        public ImageView itemMusicForm_photo_imageview;
    }

}
