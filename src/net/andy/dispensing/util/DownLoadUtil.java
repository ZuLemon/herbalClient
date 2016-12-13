package net.andy.dispensing.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import net.andy.com.Application;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Guang on 2016/12/9.
 */
public class DownLoadUtil {
    private static String uri="http://"+ Application.getServerIP().trim()+"/herbal/";
    /**
     * 下载文件
     * @param fileName
     * @return
     */
    public static Bitmap getBitmapByFileName(String fileName){
        //创建一个url对象
        //打开URL对应的资源输入流
        InputStream is= null;
        Bitmap bitmap=null;
        try {
            URL urlx=new URL(uri+"file/get.do?fileName="+fileName);
            is = urlx.openStream();
            bitmap= BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
