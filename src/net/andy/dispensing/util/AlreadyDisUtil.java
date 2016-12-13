package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guang on 2016/6/22.
 */
public class AlreadyDisUtil {
    private ReturnDomain returnDomain;
    public List getPrescriptionByUserId(Integer userId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("userId", String.valueOf(userId)));
        returnDomain = (ReturnDomain) Http.post("prescription/getPrescriptionByUserId.do", pairs, ReturnDomain.class);
        if (returnDomain.getSuccess()) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  List.class);
        } else {
            throw new Exception(returnDomain.getException());
        }
    }
}
