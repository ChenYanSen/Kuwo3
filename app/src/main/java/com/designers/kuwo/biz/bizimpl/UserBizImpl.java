package com.designers.kuwo.biz.bizimpl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.designers.kuwo.biz.UserBiz;
import com.designers.kuwo.dao.UserDao;
import com.designers.kuwo.dao.daoimpl.UserDaoImpl;
import com.designers.kuwo.eneity.User;
import com.designers.kuwo.sqlite.SQLiteDatabaseManager;
import com.designers.kuwo.sqlite.TransactionManager;

import java.sql.SQLException;

/**
 * Created by 跃 on 2017/1/12.
 */
public class UserBizImpl implements UserBiz {
    private UserDao userDao;
    private  boolean flag=false;
    private  SQLiteDatabaseManager sqLiteDatabaseManager;
    public UserBizImpl() {
        this.userDao =new UserDaoImpl();
        this.sqLiteDatabaseManager=new SQLiteDatabaseManager();
    }
        //用于判断是否已注册过了
    @Override
    public boolean userExists(Context context, String account) {

        //获取数据库连接
        SQLiteDatabase sqLiteDatabase=sqLiteDatabaseManager.getDatabaseByRead(context);
        try {
            flag=userDao.findByName(account,sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
        return flag;
    }
          //注册
    @Override
    public boolean register(Context context, User user) {
        //获取数据库连接
        SQLiteDatabase sqLiteDatabase=sqLiteDatabaseManager.getDatabaseByRead(context);
        //开启事务
        TransactionManager transactionManager=new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);
        try {
            flag = userDao.insertUser(user,sqLiteDatabase);
            if(flag)
            {
                transactionManager.commitTransaction(sqLiteDatabase);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            transactionManager.endTransaction(sqLiteDatabase);
           sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }

        return flag;
    }

    @Override
    public boolean LoginFind(Context context, User user) {
        //获取数据库连接
        SQLiteDatabase sqLiteDatabase=sqLiteDatabaseManager.getDatabaseByRead(context);
        try {
            flag=userDao.LoginSelect(user,sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
        return flag;
    }
    //添加个人信息
    @Override
    public boolean AlterPersonalInfor(String account, User user, Context context) {
        //获取数据库连接
        SQLiteDatabase sqLiteDatabase=sqLiteDatabaseManager.getDatabaseByRead(context);
        //开启事务
        TransactionManager transactionManager=new TransactionManager();
        transactionManager.beginTransaction(sqLiteDatabase);
        try {
            flag = userDao.alterUserInfor(user,account,sqLiteDatabase);
            if(flag)
            {
                transactionManager.commitTransaction(sqLiteDatabase);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            transactionManager.endTransaction(sqLiteDatabase);
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
        return flag;
    }
          //查询显示个人信息
    @Override
    public User displayPersonalInfro(String account, Context context) {
        User user=new User();
        SQLiteDatabase sqLiteDatabase=sqLiteDatabaseManager.getDatabaseByRead(context);
        try {
           user=userDao.selectUserInfor(account,sqLiteDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sqLiteDatabaseManager.closeSQLiteDatabase(sqLiteDatabase);
        }
        if(user==null)
        {
            return null;
        }
        return user;
    }
}
