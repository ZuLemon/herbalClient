package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guang on 2016/5/20.
 */
public class OnlineUtil {
    private ReturnDomain returnDomain;
    /**
     获取在线用户列表

     */
    public List getOnline(String userId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add ( new BasicNameValuePair( "userId", userId));
        returnDomain = (ReturnDomain) new Http().post ( "readyOnline/getOnline.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  List.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    /**
     强制下线

     */
    public String offline(Integer userId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add ( new BasicNameValuePair( "userId",String.valueOf( userId)));
        returnDomain = (ReturnDomain) new Http().post ( "readyOnline/offline.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return "设置下线成功！";
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
