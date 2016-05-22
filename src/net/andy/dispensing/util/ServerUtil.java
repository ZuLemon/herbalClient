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
 * Created by Guang on 2016/5/12.
 */
public class ServerUtil {
    private ReturnDomain returnDomain;
    /**
     * 获取系统时间
     * @return
     * @throws Exception
     */
    public String getServerTime() throws Exception {
       String st= (String) new Http().get ( "getServerTime.do");
        if (st !=null) {
            return st;
        } else {
            throw new Exception ( "获取时间错误" );
        }
    }
}
