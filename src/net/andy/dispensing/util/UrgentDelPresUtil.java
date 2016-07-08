package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guang on 2016/5/19.
 */
public class UrgentDelPresUtil {
    private ReturnDomain returnDomain;

    /**
     置顶处方

     */
    public String setUrgent(Integer id,String urgent) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add ( new BasicNameValuePair( "id", String.valueOf(id)));
        pairs.add ( new BasicNameValuePair( "urgent", urgent));
        returnDomain = (ReturnDomain) new Http().post ( "prescription/setUrgent.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return "设置成功！";
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }


    public String delPres(Integer id) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add ( new BasicNameValuePair( "id", String.valueOf(id)));
        returnDomain = (ReturnDomain) new Http().post ( "prescription/powerDelete.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return "删除成功！";
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
