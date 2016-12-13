package net.andy.dispensing.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.com.AppOption;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guang on 2016/3/15.
 */
public class ShelfUtil {
    private ReturnDomain returnDomain;
    /**
     获取所有规则
     */
    public List getShelfList() throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("userId", new AppOption().getOption(AppOption.APP_OPTION_USER)));
        returnDomain = ( ReturnDomain ) Http.post ( "shelf/getShelfAll.do", pairs, ReturnDomain.class );
        if ( returnDomain.getSuccess () ) {
            return JSON.parseObject ( returnDomain.getObject ().toString (),  List.class);
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
