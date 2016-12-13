package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.boiling.domain.SoakDomain;
import net.andy.boiling.domain.SolutionDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * SolutionUtil
 * 煎制方案
 * @author RongGuang
 * @date 2016/8/16
 */
public class SolutionUtil {
    private ReturnDomain returnDomain;

    public List getSolutionForAll(String status) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "status", status ) );
        returnDomain = ( ReturnDomain ) ( Http.post ( "solution/getSolutionForAll.do", pairs, ReturnDomain.class ) );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), List.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
