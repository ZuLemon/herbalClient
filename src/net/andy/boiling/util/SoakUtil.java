package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.boiling.domain.SoakDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * SoakUtil
 *
 * @author RongGuang
 * @date 2015/12/9
 */
public class SoakUtil {
    private ReturnDomain returnDomain;

    public SoakDomain getSoakByPlanId(String planId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        returnDomain = ( ReturnDomain ) ( new Http ().post ( "soak/getSoakByPlanId.do", pairs, ReturnDomain.class ) );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), SoakDomain.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
