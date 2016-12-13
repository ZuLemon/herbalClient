package net.andy.boiling.util;

import com.alibaba.fastjson.JSON;
import net.andy.boiling.domain.ReturnDomain;
import net.andy.boiling.domain.TagDomain;
import net.andy.com.Http;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PermissionUtil
 *
 * @author RongGuang
 * @date 2016/3/11
 */
public class PermissionUtil {
    private ReturnDomain returnDomain;

    /**
     * @param userId
     * @return
     * @throws Exception
     */
    public List getPermissionByuserId(String userId) throws Exception {
        List<NameValuePair> pairs = new ArrayList<NameValuePair> ();
        pairs.add ( new BasicNameValuePair ( "userId", userId ) );
        List listMap = ( List ) Http.post ( "permission/userMobilePermission.do", pairs, List.class );
        if ( listMap!=null) {
          return  listMap;
        } else {
            throw new Exception ( returnDomain.getException () );
        }
    }
}
