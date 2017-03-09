package com.designers.kuwo.biz;

import android.content.Context;

import com.designers.kuwo.eneity.User;

/**
 * Created by 跃 on 2017/1/12.
 */
public interface UserBiz {
    /**
     * 判断用户是否存在
     */
    public boolean userExists(final Context context, String account);

    /**
     * 用户注册
     */
    public boolean register(final Context context, User user);
    //用户验证
    public boolean LoginFind(final Context context,User user);
    //添加用户信息
    public boolean AlterPersonalInfor(final String account,final User user,final Context context);
    //显示用户信息
    public User displayPersonalInfro(final String account,final Context context);

}
