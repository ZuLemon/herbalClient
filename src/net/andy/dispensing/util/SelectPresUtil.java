package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Guang
 * Date: 2016/7/5
 */
public class SelectPresUtil {
    private ReturnDomain returnDomain;
    /**
     获取处方列表

     */
    public List getPrescriptionByPatientNo(String patientNo, String userId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add ( new BasicNameValuePair( "patientNo", "%"+patientNo+"%"));
        pairs.add ( new BasicNameValuePair( "userId", userId));
        returnDomain = (ReturnDomain) new Http().post ( "prescription/getPrescriptionByPatientNo.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  List.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}