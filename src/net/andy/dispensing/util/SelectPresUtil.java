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
    public List getPrescriptionByPatientNo(String begin,String end,String patientNo, String userId,String status) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add ( new BasicNameValuePair( "begin", begin));
        pairs.add ( new BasicNameValuePair( "end", end));
        pairs.add ( new BasicNameValuePair( "patientNo", "%"+patientNo+"%"));
        pairs.add ( new BasicNameValuePair( "userId", userId));
        pairs.add ( new BasicNameValuePair( "status", status));
        returnDomain = (ReturnDomain) Http.post ( "prescription/getPrescriptionByPatientNoDate.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  List.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
