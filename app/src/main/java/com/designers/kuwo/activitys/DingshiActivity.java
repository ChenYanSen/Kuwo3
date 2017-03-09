package com.designers.kuwo.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.designers.kuwo.R;

public class DingshiActivity extends Activity{
	private boolean isTick;
    private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dingshi);
		final TextView wenzi=(TextView) findViewById(R.id.settext);
		LinearLayout linear01=(LinearLayout) findViewById(R.id.linear01);
		LinearLayout linear02=(LinearLayout) findViewById(R.id.linear02);
		LinearLayout linear03=(LinearLayout) findViewById(R.id.linear03);
		LinearLayout linear04=(LinearLayout) findViewById(R.id.linear04);
		LinearLayout linear05=(LinearLayout) findViewById(R.id.linear05);
		LinearLayout linear06=(LinearLayout) findViewById(R.id.linear06);
		final ImageView dagou01=(ImageView) findViewById(R.id.dagou01);
		final ImageView dagou02=(ImageView) findViewById(R.id.dagou02);
		
		final ImageView dagou03=(ImageView) findViewById(R.id.dagou03);
		final ImageView dagou04=(ImageView) findViewById(R.id.dagou04);
		final ImageView dagou05=(ImageView) findViewById(R.id.dagou05);
		final ImageView dagou06=(ImageView) findViewById(R.id.dagou06);
		back= (ImageView) findViewById(R.id.back);
		back.setOnClickListener(x->{
			startActivity(new Intent(DingshiActivity.this,MainActivity.class));
		});
		linear01.setOnClickListener(new OnClickListener() {
	        	
	        	
	        	@Override
	        	public void onClick(View v) {
	        		// TODO Auto-generated method stub
	        		
	        		if(isTick==true){
	        			dagou01.setVisibility(View.VISIBLE);
	        			dagou01.setImageResource(R.drawable.deing_shi_back);
	        			wenzi.setText("��ʱ����δ����");
	        			isTick=false;
	        		}else{
	        			dagou01.setVisibility(View.GONE);
	        			isTick=true;
	        			
	        		}
	        	}
	        });
        linear02.setOnClickListener(new OnClickListener() {
        	
        	
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		
        		if(isTick==true){
        			dagou02.setVisibility(View.VISIBLE);
        			dagou02.setImageResource(R.drawable.deing_shi_back);
        			wenzi.setText("10���Ӻ󣬽�ֹͣ���Ÿ���");
        			isTick=false;
        		}else{
        			dagou02.setVisibility(View.GONE);
        			isTick=true;
        		}
        	}
        });
        linear03.setOnClickListener(new OnClickListener() {
        	
        	
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		
        		if(isTick==true){
        			dagou03.setVisibility(View.VISIBLE);
        			dagou03.setImageResource(R.drawable.deing_shi_back);
        			wenzi.setText("20���Ӻ󣬽�ֹͣ���Ÿ���");
        			isTick=false;
        		}else{
        			dagou03.setVisibility(View.GONE);
        			isTick=true;
        		}
        	}
        });
        linear04.setOnClickListener(new OnClickListener() {
        	
        	
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		
        		if(isTick==true){
        			dagou04.setVisibility(View.VISIBLE);
        			dagou04.setImageResource(R.drawable.deing_shi_back);
        			wenzi.setText("30���Ӻ󣬽�ֹͣ���Ÿ���");
        			isTick=false;
        		}else{
        			dagou04.setVisibility(View.GONE);
        			isTick=true;
        		}
        	}
        });
        linear05.setOnClickListener(new OnClickListener() {
        	
        	
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		
        		if(isTick==true){
        			dagou05.setVisibility(View.VISIBLE);
        			dagou05.setImageResource(R.drawable.deing_shi_back);
        			wenzi.setText("1Сʱ�󣬽�ֹͣ���Ÿ���");
        			isTick=false;
        		}else{
        			dagou05.setVisibility(View.GONE);
        			isTick=true;
        		}
        	}
        });
        linear06.setOnClickListener(new OnClickListener() {
        	
        	
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		
        		if(isTick==true){
        			dagou06.setVisibility(View.VISIBLE);
        			dagou06.setImageResource(R.drawable.deing_shi_back);
        			isTick=false;
        		}else{
        			dagou06.setVisibility(View.GONE);
        			isTick=true;
        		}
        	}
        });
	}
	

}
