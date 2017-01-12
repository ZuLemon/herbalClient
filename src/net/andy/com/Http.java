package net.andy.com;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * http通信组件
 */
public class Http {
    private static AppOption appOption = new AppOption();
    public static void setUri(String ip) {
        Http.uri = "http://"+ip.trim()+"/herbal/";
    }
    private static String uri="http://"+appOption.getOption(AppOption.APP_OPTION_SERVER).trim()+"/herbal/";
    public static Object post(String url, List<NameValuePair> pairs,Class clzz) throws Exception {
        String json;
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.soaket.timeout",10000);
        client.getParams().setParameter("http.connection.timeout",10000);
        client.getParams().setParameter("http.connection-manager.timeout",60*60L);
//        client.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
        HttpPost httpPost = new HttpPost(uri+ url);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
//        httpPost.setHeader("Content-Type", "text/html;charset=UTF-8");
//        httpPost.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
        httpPost.setHeader("User-Agent", "herbalClient Of Android");
        httpPost.setHeader("userId", appOption.getOption(AppOption.APP_OPTION_USER));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            try {
                Log.e("请求路径", String.valueOf(httpPost.getURI()));
                Log.e("参数",String.valueOf(pairs));
                HttpResponse httpResponse = client.execute(httpPost);
                json = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
            } catch (IOException e) {
                throw new Exception(e);
            }
        } catch (UnsupportedEncodingException e) {
            throw new Exception(e);
        } catch (IllegalArgumentException ex){
            throw new Exception(ex);
        }
        Log.i("返回数据:",json);
        return JSON.parseObject ( json, clzz );
    }
//    public Object xUtilsPost(String url, List<NameValuePair> pairs,Class clzz){
//        RequestParams params = new RequestParams(url);
//        params.addParameter("http.soaket.timeout",10000);
//        params.addParameter("http.connection.timeout",10000);
//        params.addParameter("http.connection-manager.timeout",60*60L);
//        params.addHeader("User-Agent", "boilingClient Of Android"); //为当前请求添加一个头
//        params.addHeader("userId", String.valueOf(Application.getUsers().getId())); //为当前请求添加一个头
//
//    }
    public static Object get(String url) throws Exception {
        Log.e("请求路径", String.valueOf(url));
        HttpClient client = new DefaultHttpClient ();
//        client.getParams().setParameter(h.HTTP_CONTENT_CHARSET, "UTF-8");
        HttpGet httpGet = new HttpGet (uri + url );
//        httpGet.setHeader ( "userId", appOption.getOption ( AppOption.APP_OPTION_USER ) );
        httpGet.addHeader("Content-Type", "text/html;charset=UTF-8");
        httpGet.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
        HttpResponse httpResponse = client.execute(httpGet);
        Log.e("请求路径", String.valueOf(httpGet.getURI()));
        HttpEntity httpEntity =httpResponse.getEntity();
        String data = EntityUtils.toString(httpEntity);
        System.out.println(">$>"+data);
        return data;
//        return JSON.parseObject(json,clzz);
    }
    /**
     * 上传文件
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable UpLoadFile(Map<String, Object> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(uri+ "file/upload.do");
        Log.e("URI",uri+ "file/upload.do");
        Log.e("Map", String.valueOf(map.get("file")));
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        params.setMultipart(true);
        Callback.Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }



}
