package com.designers.kuwo.utils;

/**
 * Created by Zg on 2017/2/28.
 */
public interface ILrcViewListener {
     /**
      * 当歌词被用户上下拖动的时候回调该方法
      */
     void onLrcSeeked(int position, LrcRow lrcRow);
}
