package net.andy.hos.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guang on 2016/5/18.
 */
public class ExtInpatientUtil {
    private ReturnDomain returnDomain;
    /**
     */
    public List getExtDept() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        returnDomain = ( ReturnDomain ) Http.post ( "getExtDept.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  List.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
    /**
     */
    public List getPatient(String patName,String deptId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "patName", patName ) );
        if(deptId!=null)
        pairs.add ( new BasicNameValuePair ( "deptId", deptId ) );
        returnDomain = ( ReturnDomain ) Http.post ( "getExtInPatient.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  List.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
