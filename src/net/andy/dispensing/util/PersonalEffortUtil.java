package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import net.andy.dispensing.domain.RulesDomain;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * PersonalEffortUtil
 *
 * @author RongGuang
 * @date 2016/06/07
 */
public class PersonalEffortUtil {
    private ReturnDomain returnDomain;
    /**
     获取工作量
     */
    public Map getEffortByUserIdAndTime(String userId,String startTime,String endTime) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add(new BasicNameValuePair("userId",userId));
        pairs.add(new BasicNameValuePair("startTime",startTime));
        pairs.add(new BasicNameValuePair("endTime",endTime));
        returnDomain = ( ReturnDomain ) new Http ().post ( "report/effort/getEffortByUserIdAndTime.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  Map.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
