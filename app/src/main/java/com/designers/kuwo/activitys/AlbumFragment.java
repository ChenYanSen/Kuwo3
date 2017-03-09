package com.designers.kuwo.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.designers.kuwo.R;
import com.designers.kuwo.biz.AlbumBiz;
import com.designers.kuwo.biz.bizimpl.AlbumBizImpl;
import com.designers.kuwo.eneity.Album;
import com.designers.kuwo.utils.CircularImage;

import java.util.List;

public class AlbumFragment extends Fragment {

    private ListView listViewSinger;
    private AlbumFragmentAdapter adapter;
    private List<Album> albumFindList;
    private AlbumBiz albumBiz;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_album, null);

        this.listViewSinger = (ListView) view.findViewById(R.id.listViewAlbum);
        //从数据库中读出来数据
        albumBiz=new AlbumBizImpl();
        albumFindList=albumBiz.AlbumFind(getActivity());
        adapter = new AlbumFragmentAdapter(this.getActivity());
        this.listViewSinger.setAdapter(adapter);
        this.listViewSinger.setOnItemClickListener(new ItemClickEvent());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    //listView的点击监听事件
    private class ItemClickEvent implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //拿到position，跳转过去，将值传到布局中去
            TextView tv= (TextView) view.findViewById(R.id.txtItemSinger);
            String albums=tv.getText().toString();
            byte [] albumImage=albumFindList.get(position).getAlbumUri();
            Intent intent = new Intent(getActivity(), MusicAlbumPageActivity.class);
            intent.putExtra("tvalbums",albums);
            intent.putExtra("albumImage",albumImage);
            startActivity(intent);
        }
    }

    public class AlbumFragmentAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater inflater;

        public AlbumFragmentAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return albumFindList.size();
        }

        @Override
        public Object getItem(int position) {
            return albumFindList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //拿到视图
            if (null == convertView) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_singer, null);
                holder.txtItemSinger = (TextView) convertView.findViewById(R.id.txtItemSinger);
                holder.txtItemSongNum = (TextView) convertView.findViewById(R.id.txtItemSongNum);
                holder.circularImageSinger = (CircularImage) convertView.findViewById(R.id.circularImageSinger);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //视图上赋值
            holder.txtItemSinger.setText(albumFindList.get(position).getAlbumName().toString());
            holder.txtItemSongNum.setText(albumFindList.get(position).getSongNum() + "首");
            //头像
            byte[] in = (byte[]) albumFindList.get(position).getAlbumUri();
            Bitmap bm = BitmapFactory.decodeByteArray(in, 0, in.length);
            holder.circularImageSinger.setImageBitmap(bm);
            holder.circularImageSinger.setBackgroundResource(R.drawable.album);
            return convertView;
        }

        private class ViewHolder {
            CircularImage circularImageSinger;
            TextView txtItemSinger, txtItemSongNum;
        }
    }
}
