package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ArgumentDomain;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guang on 2017/1/9.
 */
public class ArgumentUtil {
    private ReturnDomain returnDomain;
    public ArgumentDomain getArgument(String name,String values,String values1,String type) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add ( new BasicNameValuePair( "name", name ) );
        pairs.add ( new BasicNameValuePair ( "values", values ) );
        pairs.add ( new BasicNameValuePair ( "values1", values1 ) );
        pairs.add ( new BasicNameValuePair ( "type", type ) );
        returnDomain = (ReturnDomain) Http.post ( "argument/getArgument.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (), ArgumentDomain.class );
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
