package net.andy.dispensing.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件 操作工具
 */
public class FileUtil {
    private final static String WORKPLACE_SDCARD_PATH="/herbal/";
    /**
     * 判断SD卡是否存在
     */
    public static boolean hasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**创建目录  */
    public static void createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 临时目录
     * @return
     */
    public static String getProjectTemp(){
        File rootdir = Environment.getExternalStorageDirectory();
        String filePath = rootdir.getPath() + WORKPLACE_SDCARD_PATH+"temp";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        return filePath;
    }

    /**
     * 项目目录
     * @return
     */
    public static String getProjectPath(){
        // 如果文件夹不存在则创建文件夹，并将bitmap图像文件保存
        File rootdir = Environment.getExternalStorageDirectory();
        String filePath = rootdir.getPath() + WORKPLACE_SDCARD_PATH;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        return filePath;
    }
    /**
     * 保存图片到本地
     * @param photoBitmap
     * @param fileName
     * @param path
     */
    public static void saveFileToSDCard(Bitmap photoBitmap,String path,String fileName){
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()){
                dir.mkdirs();
            }
            File file = new File(path , fileName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                file.delete();
                e.printStackTrace();
            } catch (IOException e) {
                file.delete();
                e.printStackTrace();
            } finally{
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     * @param path
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String path,String fileName){
        if (checkSDCardAvailable()) {
            File photoFile = new File(path, fileName );
            if(photoFile.isFile()) {
                photoFile.delete();
            }
            return true;
        }else {
            return false;
        }
    }
    /**
     * 检查存储卡
     * @return
     */
    public static boolean checkSDCardAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
