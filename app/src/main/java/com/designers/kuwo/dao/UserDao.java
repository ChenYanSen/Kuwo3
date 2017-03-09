package com.designers.kuwo.dao;

import android.database.sqlite.SQLiteDatabase;

import com.designers.kuwo.eneity.User;

import java.sql.SQLException;

/**
 * Created by 跃 on 2017/1/12.
 */
public interface UserDao {

    /**
     * 判断用户是否存在
     */
    public boolean findByName(String account, final SQLiteDatabase sqLiteDatabase)
            throws SQLException;

    //用户注册
    public boolean insertUser(final User user,final SQLiteDatabase sqLiteDatabase)throws SQLException;
    //用户验证登录
    public boolean LoginSelect(final User user,final SQLiteDatabase sqLiteDatabase)throws SQLException;
    //添加用户个人信息
    public boolean alterUserInfor(final User user,final String account,SQLiteDatabase sqLiteDatabase)throws SQLException;
    //查询用户个人信息
    public User    selectUserInfor(final String account,SQLiteDatabase sqLiteDatabase)throws SQLException;
}
