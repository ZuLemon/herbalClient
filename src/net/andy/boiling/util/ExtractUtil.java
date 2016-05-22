package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
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

    public ExtractingDomain getExtractingByPlanId(String planId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "extracting/getExtractingByPlanId.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), ExtractingDomain.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }

    public ExtractingDomain insert(String planId, String equipId, String extractStatus) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        pairs.add ( new BasicNameValuePair ( "equipId", equipId ) );
        pairs.add ( new BasicNameValuePair ( "extractStatus", extractStatus ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "extract/insert.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), ExtractingDomain.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }

    public Integer getWater(String planId, String type) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        pairs.add ( new BasicNameValuePair ( "soakType", type ) );
        try {
            returnDomain = ( ReturnDomain ) ( new Http ().post ( "extracting/calcWater.do", pairs, ReturnDomain.class ) );
            if ( returnDomain.getSuccess () ) {
                if ( returnDomain.getObject () != null ) {
                    return Integer.parseInt ( returnDomain.getObject ().toString () );
                } else {
                    throw new Exception ( "没有加液量" );
                }
            } else {
                throw new Exception ( returnDomain.getException () );
            }
        } catch (Exception e) {
            throw new Exception ( e.getMessage () );
        }
    }
}
