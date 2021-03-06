package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Guang
 * Date: 2016/7/13
 */
public class PrescriptionDetailUtil {
    private ReturnDomain returnDomain;

    public List getPrescriptionDetailByPresId(String presId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add ( new BasicNameValuePair( "presId", presId));
        returnDomain = (ReturnDomain) Http.post ( "prescriptionDetail/getPrescriptionDetailByPresId.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject().toString(),List.class);
        } else {
            throw new Exception (returnDomain.getException () );
        }
    }
    public List getAddMedicine(String planId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add ( new BasicNameValuePair( "planId", planId));
        returnDomain = (ReturnDomain) Http.post ( "prescriptionDetail/getAddMedicine.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject().toString(),List.class);
        } else {
            throw new Exception (returnDomain.getException () );
        }
    }
}
