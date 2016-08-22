package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.dispensing.domain.DispensingDomain;
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
 * @date 2016/03/02
 */
public class DispensingUtil {
    private ReturnDomain returnDomain;
    /**
     获取待调剂处方
     */
    public DispensingDomain getDispensingByPlanId(String device,String planId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "device", device ) );
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "dispensing/getDispensingByPlanId.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            if(returnDomain.getObject ()==null){
                return null;
            }
            return JSON.parseObject ( returnDomain.getObject ().toString (),  DispensingDomain.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    /**
     获取处方总量
     */
    public DispensingDomain getDispensing(String disId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "id", disId ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "dispensing/getDispensing.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  DispensingDomain.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    /**
     完成处方
     */
    public String updateToFinish(String id,String tagId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "id", id ) );
        pairs.add ( new BasicNameValuePair ( "tagId", tagId ) );
        returnDomain = ( ReturnDomain ) new Http ().post ( "dispensing/updateToFinish.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return "绑定成功";
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
