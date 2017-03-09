package com.designers.kuwo.biz;

import android.content.Context;

import com.designers.kuwo.eneity.Recent;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/24.
 */
public interface RecentBiz {

    public abstract void insert(final Context context, final Recent recent);

    public abstract List<Map<String, Object>> findAll(final Context context);

    public abstract void update(final Context context, final Recent recent);

    public abstract boolean songExist(final Context context, final String songName);

    public abstract void deleteByName(final Context context, final String songName);

    public abstract void deleteAll(final Context context);
}
