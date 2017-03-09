package com.designers.kuwo.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.designers.kuwo.R;



public class Waiting extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);


        //* 欢迎页延迟
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                // 跳转到登录页面
                startActivity(new Intent(Waiting.this,LoginActivity.class));
                finish();
                return false;
            }
        }).sendEmptyMessageDelayed(0, 1000);

    }

}
