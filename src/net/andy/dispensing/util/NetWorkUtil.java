package net.andy.dispensing.util;

import android.util.Log;
import net.andy.com.Http;

import java.io.File;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Guang on 2016/12/12.
 */
public class NetWorkUtil {
    public String putFile(String filePath) throws Exception{
        Map<String,Object> map=new HashMap<>();
        map.put("file", new File(filePath));
        Http.UpLoadFile(map, new XCallBack<String>(){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
        return "上传成功";
    }
}
