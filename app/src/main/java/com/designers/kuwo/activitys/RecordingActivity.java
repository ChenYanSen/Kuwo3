package com.designers.kuwo.activitys;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.designers.kuwo.R;
import com.designers.kuwo.utils.CircularImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RecordingActivity extends Activity
{
  private CircularImage myButton1;


  private ListView myListView1;
  private String strTempFile = "ex07_11_";
  private File myRecAudioFile;
  private File myRecAudioDir;
  private File myPlayFile;
  private MediaRecorder mMediaRecorder01;
  private boolean click=true;
  private ArrayList<String> recordFiles;
  private ArrayAdapter<String> adapter;
  //private TextView myTextView1;
  private boolean sdCardExit;
  private boolean isStopRecord;
  private int listpostion=111111111;
  private boolean startAndStop=true;
  private ImageView recording_back;
  private TextView toolbar_txt;
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recording);

    myButton1 = (CircularImage) findViewById(R.id.ImageButton01);
    myButton1.setImageResource(R.drawable.image_button01);
    toolbar_txt= (TextView) findViewById(R.id.toolbar_txt);
    myListView1 = (ListView) findViewById(R.id.ListView01);
   // myTextView1 = (TextView) findViewById(R.id.TextView01);
    
    myButton1.setEnabled(true);
    recording_back= (ImageView) findViewById(R.id.back);
    toolbar_txt.setText("录音");
    recording_back.setOnClickListener(x->{startActivity(new Intent(RecordingActivity.this,MainActivity.class));});


    /* �ж�SD Card�Ƿ���� */
    sdCardExit = Environment.getExternalStorageState().equals(
        Environment.MEDIA_MOUNTED);
    /* ȡ��SD Card·����Ϊ¼�����ļ�λ�� */
    if (sdCardExit)
      myRecAudioDir = Environment.getExternalStorageDirectory();

    /* ȡ��SD CardĿ¼�������.amr�ļ� */
    getRecordFiles();

    adapter = new ArrayAdapter<String>(this,
        R.layout.activity_childview, recordFiles);
    /* ��ArrayAdapter����ListView������ */
     myListView1.setAdapter(adapter);

    /* ¼�� */
    myButton1.setOnClickListener(new ImageButton.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {
              if(startAndStop){
                starRecording();
                myButton1.setImageResource(R.drawable.image_button01);
                startAndStop=false;
              }else {
                stopRecording();
                myButton1.setImageResource(R.drawable.image_button02);
                startAndStop=true;
              }

      }
    });
    /* ֹͣ */
    /*myButton2.setOnClickListener(new ImageButton.OnClickListener() {

      @Override
      public void onClick(View arg0) {
        // TODO Auto-generated method stub

      }
    });
*/

           myListView1.setOnItemClickListener(new ItemClick());

    myListView1.setOnItemLongClickListener(new LongTtemClick());

  }
  void starRecording(){
    try
    {
      if (!sdCardExit)
      {
        Toast toast=Toast.makeText(RecordingActivity.this, "没有sdCard",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 10);
        toast.show();
        return;
      }


      myRecAudioFile = File.createTempFile(strTempFile, ".amr",
              myRecAudioDir);

      mMediaRecorder01 = new MediaRecorder();

      mMediaRecorder01.setAudioSource(MediaRecorder.AudioSource.MIC);
      mMediaRecorder01.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
      mMediaRecorder01.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
      mMediaRecorder01.setOutputFile(myRecAudioFile.getAbsolutePath());

      mMediaRecorder01.prepare();

      mMediaRecorder01.start();


      Toast toast=Toast.makeText(RecordingActivity.this, "开始录音",
              Toast.LENGTH_LONG);
      toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 10);
      toast.show();
      isStopRecord = false;

    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  };
  void stopRecording(){
    if (myRecAudioFile != null) {
      mMediaRecorder01.stop();
      adapter.add(myRecAudioFile.getName());
      mMediaRecorder01.release();
      mMediaRecorder01 = null;

      Toast toast = Toast.makeText(RecordingActivity.this, "" + myRecAudioFile.getName(),
              Toast.LENGTH_LONG);
      toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 10);
      toast.show();



      isStopRecord = true;
    }
  }
class ItemClick implements AdapterView.OnItemClickListener{
  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
click=true;
if(position!=listpostion){
  myPlayFile = new File(myRecAudioDir.getAbsolutePath()
          + File.separator
          + ((TextView) view).getText());


  if (myPlayFile != null && myPlayFile.exists()) {
    //* �������ŵĳ��� *//*
    openFile(myPlayFile);
  }
  click=true;
}





  }
}

  class LongTtemClick implements AdapterView.OnItemLongClickListener{
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
      //长点击删除事件
      listpostion=position;
      click=false;
      AlertDialog.Builder builder = new AlertDialog.Builder(RecordingActivity.this);
      View views = LayoutInflater.from(RecordingActivity.this).inflate(R.layout.record_toast, null);
      final TextView recording_title_set = (TextView) views.findViewById(R.id.recording_title_set);
      recording_title_set.setText("删除该录音");

      builder.setView(views);
      builder.setPositiveButton("确定", new Dialog.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          myPlayFile = new File(myRecAudioDir.getAbsolutePath()
                  + File.separator
                  + ((TextView) view).getText());
          if (myPlayFile != null)
          {

            adapter.remove(myPlayFile.getName());

            if (myPlayFile.exists())
              myPlayFile.delete();

          }



        }
      });
      builder.setNegativeButton("取消", new Dialog.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          dialog.dismiss();
        }
      }).show();

      return false;
    }
  }









  @Override
  protected void onStop()
  {
    if (mMediaRecorder01 != null && !isStopRecord)
    {
      /* ֹͣ¼�� */
      mMediaRecorder01.stop();
      mMediaRecorder01.release();
      mMediaRecorder01 = null;
    }
    super.onStop();
  }

  private void getRecordFiles()
  {
    recordFiles = new ArrayList<String>();
    if (sdCardExit)
    {
      File files[] = myRecAudioDir.listFiles();
      if (files != null)
      {

        for (int i = 0; i < files.length; i++)
        {
          if (files[i].getName().indexOf(".") >= 0)
          {
            /* ��ȡ.amr�ļ� */
            String fileS = files[i].getName().substring(
                files[i].getName().indexOf("."));
            if (fileS.toLowerCase().equals(".amr"))
              recordFiles.add(files[i].getName());

          }
        }
      }
    }
  }


  private void openFile(File f)
  {

    Intent intent = new Intent();
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setAction(Intent.ACTION_VIEW);

    String type = getMIMEType(f);
    intent.setDataAndType(Uri.fromFile(f), type);
    startActivity(intent);
  }

  private String getMIMEType(File f)
  {
    String end = f.getName().substring(
        f.getName().lastIndexOf(".") + 1, f.getName().length())
        .toLowerCase();
    String type = "";
    if (end.equals("mp3") || end.equals("aac") || end.equals("aac")
        || end.equals("amr") || end.equals("mpeg")
        || end.equals("mp4"))
    	//��Ƶ
    {
      type = "audio";
    } else if (end.equals("jpg") || end.equals("gif")
        || end.equals("png") || end.equals("jpeg"))
    	//ͼƬ
    {
      type = "image";
    } else
    {
      type = "*";
    }
    type += "/*";
    return type;
  }
 
}
  