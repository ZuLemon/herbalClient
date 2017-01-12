package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.AppOption;
import net.andy.com.Application;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guang on 2016/12/22.
 */
public class DeviceUtil {
    private ReturnDomain returnDomain;
    /**
     添加设备信息
     */
    public void insert(String imei,String meid,String mac,String model,String loginIP) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("imei", imei));
        pairs.add(new BasicNameValuePair("meid", meid));
        pairs.add(new BasicNameValuePair("mac", mac));
        pairs.add(new BasicNameValuePair("model", model));
        pairs.add(new BasicNameValuePair("loginIP", loginIP));
        pairs.add(new BasicNameValuePair("userId", String.valueOf(Application.getUsers().getId())));

        returnDomain = ( ReturnDomain ) Http.post ( "device/insert.do", pairs, ReturnDomain.class );
//        if ( returnDomain.getSuccess () ) {
//        } else {
//            throw new Exception ( returnDomain.getException () );
//        }
    }
}
