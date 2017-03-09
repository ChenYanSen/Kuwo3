package com.designers.kuwo.utils;

import java.sql.Blob;

/**
 * Created by PC-CWB on 2017/3/2.
 */
public class BlobToByteArr {

    public static byte[] blob2ByteArr(Blob blob) throws Exception {

        byte[] b = null;
        try {
            if (blob != null) {
                long in = 0;
                b = blob.getBytes(in, (int) (blob.length()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("fault");
        }

        return b;
    }
}
