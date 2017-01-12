package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ExtractingDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import net.andy.dispensing.util.HerbalUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * ExtractingUtil
 *
 * @author RongGuang
 * @date 2015/12/9
 */
public class ExtractingUtil {
    private ReturnDomain returnDomain;

    public ExtractingDomain getExtractingByPlanId(String planId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        returnDomain = ( ReturnDomain ) Http.post ( "extracting/getExtractingByPlanId.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), ExtractingDomain.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    public ExtractingDomain importExtracting(String tagId,
                                             Integer userId,
                                             String barcode) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "tagId", tagId ) );
        pairs.add ( new BasicNameValuePair ( "userId", String.valueOf(userId) ) );
        pairs.add ( new BasicNameValuePair ( "barcode", barcode ) );
        returnDomain = ( ReturnDomain ) Http.post ( "extracting/importExtracting.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), ExtractingDomain.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    public String subExtracting(ExtractingDomain extractingDomain, boolean newTag) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        HerbalUtil.getNameValuePair(extractingDomain,pairs);
        pairs.add ( new BasicNameValuePair ( "newTag", String.valueOf(newTag)) );
        returnDomain = ( ReturnDomain ) Http.post ( "extracting/subExtracting.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return "保存成功";
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    public Integer calcWater(String planId, String soakType) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        pairs.add ( new BasicNameValuePair ( "soakType", soakType ) );
        try {
            returnDomain = ( ReturnDomain ) ( Http.post ( "extracting/calcWater.do", pairs, ReturnDomain.class ) );
            if ( returnDomain.getSuccess () ) {
                if ( returnDomain.getObject () != null ) {
                    return Integer.parseInt ( returnDomain.getObject ().toString () );
                } else {
                    throw new Exception ( "没有加液量" );
                }
            } else {
                return 0;
//                throw new Exception ( returnDomain.getException () );
            }
        } catch (Exception e) {
            throw new Exception ( e.getMessage () );
        }
    }
    public String setStatus(String planId,String status) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "planId", planId) );
        pairs.add ( new BasicNameValuePair ( "status", status) );
        returnDomain = ( ReturnDomain ) Http.post ( "extracting/setStatus.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return status;
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
