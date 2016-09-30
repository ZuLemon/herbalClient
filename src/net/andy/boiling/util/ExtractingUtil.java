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
        returnDomain = ( ReturnDomain ) new Http ().post ( "extracting/getExtractingByPlanId.do", pairs, ReturnDomain.class );
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
        returnDomain = ( ReturnDomain ) new Http ().post ( "extracting/importExtracting.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), ExtractingDomain.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    public String subExtracting(ExtractingDomain extractingDomain, boolean newTag) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
//        pairs.add ( new BasicNameValuePair ( "id", String.valueOf(extractingDomain.getId() ) ));
//        pairs.add ( new BasicNameValuePair ( "planId", extractingDomain.getPlanId()) );
//        pairs.add ( new BasicNameValuePair ( "tagId", extractingDomain.getTagId()) );
//        pairs.add ( new BasicNameValuePair ( "presId", extractingDomain.getPresId()) );
//        pairs.add ( new BasicNameValuePair ( "solutionId", extractingDomain.getSolutionId()) );
//        pairs.add ( new BasicNameValuePair ( "userId", String.valueOf(extractingDomain.getUserId()) ));
//        pairs.add ( new BasicNameValuePair ( "deptId", extractingDomain.getDeptId()) );
////        pairs.add ( new BasicNameValuePair ( "operationTime", extractingDomain.getOperationTime()) );
//        pairs.add ( new BasicNameValuePair ( "planId", extractingDomain.getPlanId()) );
        HerbalUtil.getNameValuePair(extractingDomain,pairs);
        pairs.add ( new BasicNameValuePair ( "newTag", String.valueOf(newTag)) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "extracting/subExtracting.do", pairs, ReturnDomain.class );
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
            returnDomain = ( ReturnDomain ) ( new Http ().post ( "extracting/calcWater.do", pairs, ReturnDomain.class ) );
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
}
