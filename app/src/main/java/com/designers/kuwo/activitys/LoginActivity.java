package com.designers.kuwo.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.designers.kuwo.R;
import com.designers.kuwo.biz.UserBiz;
import com.designers.kuwo.biz.bizimpl.UserBizImpl;
import com.designers.kuwo.eneity.User;
import com.designers.kuwo.utils.*;
import com.designers.kuwo.utils.CustomApplication;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LoginActivity extends Activity {
    private ImageView loginHeadPicture;
    private TextView edtUserName;
    private TextView edtPassword;
    private TextView btnLogin;
    private TextView btnRegister;
    private TextView login_miss_passwrod;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    boolean flag = false;
    private   String names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        //从 SharedPreferences读出储存的账号

        this.edtPassword = (TextView) findViewById(R.id.edtPassword);
        this.btnLogin = (TextView) findViewById(R.id.btnLogin);
        this.btnRegister = (TextView) findViewById(R.id.btnRegister);
        this.login_miss_passwrod = (TextView) findViewById(R.id.login_miss_passwrod);

        this.loginHeadPicture = (CircularImage) findViewById(R.id.loginHeadPicture);

        sharedPreferences = this.getSharedPreferences("userInfor", this.MODE_PRIVATE);


        String name = sharedPreferences.getString("names", "user");
        this.edtUserName = (TextView) findViewById(R.id.edtUserName);
        Intent intent=getIntent();
        String users=intent.getStringExtra("user");
        //显示读出的账号
        if(users!=null){
            this.edtUserName.setText(users);
            UserBiz userBizs=new UserBizImpl();
            User userss=userBizs.displayPersonalInfro(users,LoginActivity.this);
            //设置元头像
            //从数据库读出头像地址
            String str=userss.getAvatarUri();
            //Log.i("数据库获得地址------------",str);
            if(str!=null){
                Uri uri = Uri.parse((String)str);
                CustomApplication customApplication= (CustomApplication) getApplication();
                customApplication.setAvatarUri(uri);
                try {

                    Bitmap bitmaps = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmaps.compress(Bitmap.CompressFormat.PNG, 50, bos);
                    Log.i("bitmap----------", bitmaps.toString());
                    byte[] bytes = bos.toByteArray();
                    this.loginHeadPicture.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }else if (!"user".equals(name)) {
            this.edtUserName.setText(name);
            //圆头像
            Bitmap bitMap = BitmapFactory.decodeResource(this.getResources(), R.drawable.login_image);
            this.loginHeadPicture.setImageBitmap(bitMap);
        }
        //登录点击按钮
        this.btnLogin.setOnClickListener(x -> {
            //保存到sharePreferncess
           // String names = this.edtUserName.getText().toString();
            names = this.edtUserName.getText().toString();
            String userPasswrod = this.edtPassword.getText().toString();
            //开启数据库

            boolean loginFlag=false;
            UserBiz userBiz = new UserBizImpl();
            User user = new User();
            user.setAccount(names);
            user.setPassword(userPasswrod);
            if(names.length()==0||"".equals(names)){
                showCustomToast("账号不能为空");
            }else if(userPasswrod.length()==0||"".equals(userPasswrod)){
                showCustomToast("密码不能为空");
            }else {
                loginFlag=true;
            }
            //从数据库读数据判断是否正确
           // new Thread(()->{
                flag = userBiz.LoginFind(LoginActivity.this, user);
         //   });
            if (flag&&loginFlag) {
                editor = sharedPreferences.edit();
                editor.putString("names", names);
                editor.commit();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                CustomApplication customApplication=(CustomApplication)getApplication();
                customApplication.setUserName(names);
                showCustomToast("登录成功");
            }else {
                if(name.length()!=0&&userPasswrod.length()!=0){

                    showCustomToast("密码或账号错误");
                }
            }

        });
        //注册
        this.btnRegister.setOnClickListener(x->{
            startActivity(new Intent(LoginActivity.this, Register.class));
            finish();
        });

    }

    // 自定义toast信息显示
    public void showCustomToast(String message) {
        // 1.创建一个LayoutInflater接口对象
        LayoutInflater inflater = getLayoutInflater();
        // 2.使用inflater对象中的inflate方法绑定自定义Toast布局文件,同事指向该布局文件中的根标记节点
        View layout = inflater.inflate(R.layout.toast_custom, null);

        // 3.获取该布局文件中的TextView组件
        TextView toastMessage = (TextView) layout
                .findViewById(R.id.toastMessage);
        // 4.为TextView赋值
        toastMessage.setText(message);
        // 5.实例化一个Toast组件对象，并进行显示
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        // 将步骤4设置好的定制布局与当前的Toast对象进行绑定
        toast.setView(layout);
        // 6.显示Toast组件
        toast.show();
    }
}
