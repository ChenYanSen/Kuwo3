package com.designers.kuwo.biz;

import android.content.Context;

import com.designers.kuwo.eneity.ICollection;

import java.util.List;
import java.util.Map;


/**
 * Created by 跃 on 2017/2/22.
 */
public interface CollectionBiz {
    /**
     * 添加收藏
     *
     * @param iCollection
     * @param context
     * @return
     */
    public void alterCollection(final ICollection iCollection, final Context context);

    /**
     * 取消收藏
     *
     * @param song
     * @param singer
     * @param account
     * @param context
     * @return
     */
    public void removeCollection(final String song, final String singer, final String account, final Context context);

    /**
     * 查看是否收藏
     *
     * @param song
     * @param singer
     * @param account
     * @param context
     * @return
     */
    public boolean findCollection(final String song, final String singer, final String account, final Context context);


    public List<ICollection> findCollectionAllSongs(final Context context);

    public List<Map<String, Object>> findAllCollectionsong(final Context context);


}
