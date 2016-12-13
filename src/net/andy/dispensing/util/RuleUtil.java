package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import net.andy.dispensing.domain.RulesDomain;
import net.andy.dispensing.domain.StationDomain;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * RuleUtil
 *
 * @author RongGuang
 * @date 2016/03/09
 */
public class RuleUtil {
    private ReturnDomain returnDomain;
    /**
     获取所有规则
     */
    public List getRulesList() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        returnDomain = ( ReturnDomain ) Http.post ( "rules/getRulesList.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  List.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    /**
     获取所有规则
     */
    public RulesDomain getRules(Integer id) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair( "id", String.valueOf(id) ));
        returnDomain = ( ReturnDomain ) Http.post ( "rules/getRules.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  RulesDomain.class);
        } else {
            throw new Exception ( returnDomain.getException ());
        }
    }
}
