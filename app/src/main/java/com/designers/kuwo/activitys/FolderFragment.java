package com.designers.kuwo.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.designers.kuwo.R;
import com.designers.kuwo.biz.SongBiz;
import com.designers.kuwo.biz.bizimpl.SongBizImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FolderFragment extends Fragment {

    private ListView listViewFolder;
    private String[] folder = {"文件夹1", "文件夹2", "文件夹3"};
    private FolderFragmentAdapter adapter;
    private List<Map<String,Object>> folderList=new ArrayList<Map<String,Object>>();
    private SongBiz songBiz;
    private List<Map<String,Integer>> songNumList=new ArrayList<Map<String,Integer>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_folder, null);
        this.listViewFolder= (ListView) view.findViewById(R.id.listViewFolder);
        songBiz=new SongBizImpl();
        folderList=songBiz.findFolder(getActivity());
        songNumList=songBiz.findSongNum(getActivity());
        adapter=new FolderFragmentAdapter(this.getActivity(),getData());
        this.listViewFolder.setAdapter(adapter);
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

    //初始化数据源，两个textview
    private ArrayList<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < folderList.size(); i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("itemFolder", folder[i]);
            items.add(item);
        }
        return items;
    }

    public class FolderFragmentAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<HashMap<String, Object>> listitems;
        private LayoutInflater inflater;

        public FolderFragmentAdapter(Context context, ArrayList<HashMap<String, Object>> listitems) {
            this.context = context;
            this.listitems = listitems;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return listitems.size();
        }

        @Override
        public Object getItem(int position) {
            return listitems.get(position);
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
                convertView = inflater.inflate(R.layout.item_folder, null);
                holder.txtItemFolder = (TextView) convertView.findViewById(R.id.txtItemFolder);
                holder.txtItemSongPath = (TextView) convertView.findViewById(R.id.txtItemSongPath);
                holder.txtFolderNum = (TextView) convertView.findViewById(R.id.txtFolderNum);
                holder.cirImgFolder= (ImageView) convertView.findViewById(R.id.cirImgFolder);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //视图上赋值
            holder.txtItemFolder.setText(listitems.get(position).get("itemFolder").toString());
            holder.txtItemSongPath.setText(folderList.get(position).get("folder").toString());
            holder.txtFolderNum.setText(songNumList.get(position).get("songNum").toString()+"首");
            holder.cirImgFolder.setImageResource(R.drawable.folder1);
            return convertView;
        }

        private class ViewHolder {
            TextView txtItemFolder, txtItemSongPath, txtFolderNum;
            ImageView cirImgFolder;
        }
    }

}
