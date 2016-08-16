package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 处方流程状态
 * User: Guang
 * Date: 2016/7/4
 */
public class PrescriptionStatusUtil {
    private ReturnDomain returnDomain;

    public Map getPrescriptionStatus(Integer pId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add ( new BasicNameValuePair( "pId", String.valueOf(pId ) ));
        returnDomain = (ReturnDomain) new Http().post ( "report/prescriptionStatus/getPrescriptionStatus.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  Map.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
