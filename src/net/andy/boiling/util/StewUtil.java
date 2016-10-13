package net.andy.boiling.util;

import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guang on 2016/10/11.
 */
public class StewUtil  {
    private ReturnDomain returnDomain;
    public String stewBegin(String planId,String tagId,Integer userId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add ( new BasicNameValuePair( "planId", planId ) );
        pairs.add ( new BasicNameValuePair( "tagId", tagId ) );
        pairs.add ( new BasicNameValuePair( "userId", String.valueOf(userId) ) );
        returnDomain = ( ReturnDomain ) new Http().post ( "extract/stewBegin.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return "开始煎膏";
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    public String stewEnd(String planId,String tagId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add ( new BasicNameValuePair( "planId", planId ) );
        pairs.add ( new BasicNameValuePair( "tagId", tagId ) );
        returnDomain = ( ReturnDomain ) new Http().post ( "extract/stewEnd.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return "结束煎膏";
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
