package com.designers.kuwo.activitys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.designers.kuwo.R;

import java.util.ArrayList;

/**
 * Created by Zg on 2017/1/6.
 */
public class ListMusicPlayAdapter extends BaseAdapter {
    Context context;
    ArrayList<MusicMessage> arrayList;
    private LayoutInflater inflater;
     public ListMusicPlayAdapter (Context context,ArrayList list){
         this.context  = context;
         this.arrayList= list;
         inflater= LayoutInflater.from(context);

     }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      Holder holder;
        if (convertView==null){
            holder = new Holder();
            convertView= inflater.inflate(R.layout.playitem,null);
            holder.textTitleView = (TextView) convertView.findViewById(R.id.title);
            holder.textDeleteView = (TextView) convertView.findViewById(R.id.deleteAll);
            holder.musicName= (TextView) convertView.findViewById(R.id.musicName);
            holder.musicAuthor= (TextView) convertView.findViewById(R.id.musicAuhtor);
            holder.musicDelete = (ImageView) convertView.findViewById(R.id.musicDelete);
            convertView.setTag(holder);
        }
        else {
            holder= (Holder) convertView.getTag();

        }
        holder.musicName.setText(arrayList.get(position).getMusicName());
        holder.musicAuthor.setText(arrayList.get(position).getMusicAuthor());
        holder.musicDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "您选择删除的是" + arrayList.get(position).getMusicAuthor(), Toast.LENGTH_LONG).show();
            }

        });
       /* holder.textDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "您选择全部清空" , Toast.LENGTH_LONG).show();
            }
        });*/
return  convertView;
    }
    public  class Holder{
      public   TextView musicName;
        public  TextView musicAuthor;
       public ImageView musicDelete;
        public TextView textTitleView;
        public  TextView  textDeleteView;
    }






}
