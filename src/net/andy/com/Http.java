package net.andy.com;

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
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.List;

/**
 * http通信组件
 */
public class Http {
    AppOption appOption = new AppOption();
    public Object post(String url, List<NameValuePair> pairs,Class clzz) throws Exception {
        String json;
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.socket.timeout",10000);
        client.getParams().setParameter("http.connection.timeout",10000);
        client.getParams().setParameter("http.connection-manager.timeout",60*60L);
//        client.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
        HttpPost httpPost = new HttpPost("http://"+Application.getServerIP().trim()+"/herbal/" + url);
//        httpPost.setHeader("Content-Type", "text/html;charset=UTF-8");
        httpPost.setHeader("User-Agent", "boilingClient Of Android");
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
        }
        Log.i("返回数据:",json);
        return JSON.parseObject ( json, clzz );
    }
    public Object get(String url) throws Exception {
        Log.e("请求路径", String.valueOf(url));
        HttpClient client = new DefaultHttpClient ();
//        client.getParams().setParameter(h.HTTP_CONTENT_CHARSET, "UTF-8");
        HttpGet httpGet = new HttpGet ("http://"+Application.getServerIP().trim()+"/herbal/" + url );
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
}
