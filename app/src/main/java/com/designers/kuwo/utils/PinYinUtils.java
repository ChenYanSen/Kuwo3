package com.designers.kuwo.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by PC-CWB on 2017/2/20.
 */
public class PinYinUtils {

    public static String getPinYin(String hanzi) {
        String pinyin = "";

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);


        char[] arr = hanzi.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (Character.isWhitespace(arr[i]))
                continue;
            if (arr[i] > 127) {
                try {
                    String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(arr[i], format);
                    if (pinyinArr != null) {
                        pinyin += pinyinArr[0];
                    } else {
                        pinyin += pinyinArr[i];
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                    pinyin += arr[i];
                }
            } else {

                pinyin += arr[i];
            }
        }

        return pinyin;
    }

    public static String getHeadChar(String str) {

        String convert = "";

        char word = str.charAt(0);

        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
        if (pinyinArray != null) {
            convert += pinyinArray[0].charAt(0);
        } else {
            convert += word;
        }
        return convert.toUpperCase();
    }

    public static String getPinYinHeadChar(String str) {

        String convert = "";

        for (int j = 0; j < str.length(); j++) {

            char word = str.charAt(j);

            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);

            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert.toUpperCase();
    }
}
