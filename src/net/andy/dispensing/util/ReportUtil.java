package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guang on 2016/5/18.
 */
public class ReportUtil {
    private ReturnDomain returnDomain;
    /**
     获取队列未调剂 处方数
     */
    public Integer getNoDispensingByRule(Integer ruleId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add ( new BasicNameValuePair( "ruleId", String.valueOf(ruleId ) ));
        returnDomain = (ReturnDomain) new Http().post ( "report/dispensing/getNoDispensingByRule.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  Integer.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    /**
     获取队列未调剂 处方数
     */
    public List getNoDispensing() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        returnDomain = (ReturnDomain) new Http().post ( "report/dispensing/getNoDispensing.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  List.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    /**
     获取今天已经调剂的 处方数
     */
    public Integer getDispensingByUser(String userId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add ( new BasicNameValuePair( "userId", userId ));
        returnDomain = (ReturnDomain) new Http().post ( "report/dispensing/getDispensingByUser.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  Integer.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
