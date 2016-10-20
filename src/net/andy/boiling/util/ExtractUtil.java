package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ExtractDomain;
import net.andy.boiling.domain.ExtractingDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * ExtractUtil
 *
 * @author RongGuang
 * @date 2015/12/9
 */
public class ExtractUtil {
    private ReturnDomain returnDomain;

    public ExtractingDomain insert(String planId, String equipId,String userId, String extractStatus) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        pairs.add ( new BasicNameValuePair ( "equipId", equipId ) );
        pairs.add ( new BasicNameValuePair ( "userId", userId ) );
        pairs.add ( new BasicNameValuePair ( "extractStatus", extractStatus ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "extract/insert.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), ExtractingDomain.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    public List getExtractByPlanId(String planId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "extract/getExtractByPlanId.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), List.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    public ExtractDomain getExtractByPlanIdStatus(String planId,String extractStatus) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        pairs.add ( new BasicNameValuePair ( "extractStatus", extractStatus ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "extract/getExtractByPlanIdStatus.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), ExtractDomain.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
