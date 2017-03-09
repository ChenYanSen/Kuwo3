package com.designers.kuwo.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zg on 2017/2/21.
 * 处理歌词的类
 */
public class LrcProcess {
    private List<LrcRow> lrcList;//List集合存放歌词内容对象
    private LrcRow lrcRow;//声明一个歌词内容

    public LrcProcess() {
        lrcRow = new LrcRow();
        lrcList = new ArrayList<>();

    }

    /**
     * 读取歌词
     */
    public void readLrc(String path) throws IOException {
        String s = "";


        File file = new File(path);



        FileInputStream fileInputStream = null;

        fileInputStream = new FileInputStream(file);

        InputStreamReader inputStreamReader = null;

        inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


        while ((s = bufferedReader.readLine()) != null) {
            //替换字符
            s = s.replace("[", "");
            s = s.replace("]", "@");
            Log.i("cccccc", s);
            //分离“@”字符
            String splitLrcData[] = s.split("@");
            if (splitLrcData.length > 1) {
                lrcRow.setContent(splitLrcData[1]);
                Log.i("ccc", splitLrcData[1]);
                //处理歌词去的歌曲的时间
                int lrcTime = time2Str(splitLrcData[0]);

                lrcRow.setTime(lrcTime);
                //添加进列表数字
                lrcList.add(lrcRow);
                //新创建歌词内容对象
                Log.i("ccc", "添加进去了");
                lrcRow = new LrcRow();
                Log.i("ccc", "cccccccccccc");


            }


        }
        bufferedReader.close();
        inputStreamReader.close();
    }

    public int time2Str(String timeStr) {
        String minute = "";
        String seconds = "";
        String millisecond = "";
        int currentTime = 0;
        timeStr = timeStr.replace(":", ".");
        timeStr = timeStr.replace(".", "@");
        String timeData[] = timeStr.split("@");
        if (timeData.length == 3) {
            if ("00".equals(timeData[0])){
                minute="0";
            }
            else {
                minute = timeData[0];
            }
            Log.i("cccc", minute + "");
            seconds = timeData[1];
            Log.i("cccc", seconds + "");
            millisecond = timeData[2];
            Log.i("cccc", millisecond + "");
        }
        //计算上一行与下一行时间转化为毫秒数
        try {

             currentTime    =((Integer.parseInt(minute) * 60 + Integer
                     .parseInt(seconds)) * 1000 + Integer.parseInt(millisecond) * 10);

        }catch ( NumberFormatException e){
            e.printStackTrace();

        }


        return currentTime;
    }

    public List<LrcRow> getLrcList() {
        return lrcList;
    }
}
