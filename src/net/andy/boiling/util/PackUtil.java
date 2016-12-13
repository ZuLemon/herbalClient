package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.PackDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * PackUtil
 *
 * @author RongGuang
 * @date 2015/12/9
 */
public class PackUtil {
    private ReturnDomain returnDomain;
    public String insert(String planId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "planId", planId ) );
        returnDomain = ( ReturnDomain ) Http.post ( "pack/insert.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return "包装成功";
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
