package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * BoilingEffortUtil
 *
 * @author RongGuang
 * @date 2016/09/22
 */
public class BoilingEffortUtil {
    private ReturnDomain returnDomain;
    /**
     获取煎制工作量
     */
    public Map getBoilingEffortByUserIdAndTime(String userId,String startTime,String endTime) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add(new BasicNameValuePair("userId",userId));
        pairs.add(new BasicNameValuePair("startTime",startTime));
        pairs.add(new BasicNameValuePair("endTime",endTime));
        returnDomain = ( ReturnDomain ) new Http ().post ( "report/effort/getBoilingEffortByUserIdAndTime.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  Map.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
